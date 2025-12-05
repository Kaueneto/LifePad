package com.example.lifepad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// item salvo em users/{uid}/refeicoes/{data}/itens
data class FoodMain(
    val name: String = "",
    val kcal: Double = 0.0,
    val mealType: String = "breakfast"
)

data class MealSection(
    val title: String,
    val items: List<FoodMain> = emptyList()
) {
    val totalMeals: Int get() = items.size
    val totalKcal: Double get() = items.sumOf { it.kcal }
    val info: String get() = "$totalMeals refeições | ${totalKcal.toInt()} kcal"
}

// ---------- VIEWMODEL DA TELA 1 ----------

class MealScheduleViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val uid: String = auth.currentUser?.uid ?: ""

    var breakfast by mutableStateOf(MealSection("Café da manhã"))
        private set
    var lunch by mutableStateOf(MealSection("Almoço"))
        private set
    var snack by mutableStateOf(MealSection("Lanche"))
        private set

    init {
        carregarRefeicoesDeHoje()
    }

    fun carregarRefeicoesDeHoje() {
        if (uid.isBlank()) {
            println("DEBUG_MEAL uid vazio")
            return
        }

        val hojeId = java.text.SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale.getDefault()
        ).format(java.util.Date())
        println("DEBUG_MEAL hojeId = $hojeId")

        firestore.collection("users")
            .document(uid)
            .collection("refeicoes")
            .document(hojeId)
            .collection("itens")
            .get()
            .addOnSuccessListener { snap ->
                println("DEBUG_MEAL total itens: ${snap.size()}")

                val cafe = mutableListOf<FoodMain>()
                val almoco = mutableListOf<FoodMain>()
                val lanche = mutableListOf<FoodMain>()

                for (doc in snap.documents) {
                    val name = doc.getString("description") ?: ""
                    val kcal = doc.getDouble("kcal")
                        ?: doc.getDouble("energy_kcal")
                        ?: 0.0
                    val mealType = doc.getString("mealType") ?: "breakfast"

                    println(
                        "DEBUG_MEAL doc=${doc.id} desc=$name kcalLida=$kcal mealType=$mealType"
                    )

                    val item = FoodMain(name = name, kcal = kcal, mealType = mealType)
                    when (mealType) {
                        "breakfast" -> cafe.add(item)
                        "lunch" -> almoco.add(item)
                        "snack" -> lanche.add(item)
                    }
                }

                breakfast = MealSection(
                    title = "Café da manhã",
                    items = cafe
                )
                lunch = MealSection(
                    title = "Almoço",
                    items = almoco
                )
                snack = MealSection(
                    title = "Lanche",
                    items = lanche
                )
            }
            .addOnFailureListener { e ->
                println("DEBUG_MEAL erro: ${e.message}")
            }
    }
}


// ---------- COMPOSABLE ----------

@Composable
fun MealScheduleScreen(
    navController: NavController,
    viewModel: MealScheduleViewModel = viewModel()
) {
    // Recarrega sempre que a tela for (re)mostrada
    LaunchedEffect(Unit) {
        viewModel.carregarRefeicoesDeHoje()
    }

    val breakfast = viewModel.breakfast
    val lunch = viewModel.lunch
    val snack = viewModel.snack

    val mealSections = listOf(breakfast, lunch, snack)

    Scaffold(
        containerColor = Color(0xFF2E2B45),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("breakfast") },
                containerColor = Color(0xFF8620AC)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adicionar Refeição",
                    tint = Color.White
                )
            }
        },
        bottomBar = { CustomBottomNavigation(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item { MealHeader(navController) }
            item { DateSelector() }

            items(mealSections.size) { index ->
                val section = mealSections[index]
                MealSectionCard(
                    section = section,
                    onAddClick = {
                        if (section.title == "Café da manhã") {
                            navController.navigate("breakfast")
                        }
                        // aqui depois você trata almoço/lanche
                    }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}


@Composable
fun MealHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }
        Text(
            text = "Horário das Refeições",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = Color.White
            )
        }
    }
}

@Composable
fun MealSectionCard(
    section: MealSection,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF3A3555))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    section.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    section.info,
                    color = Color(0xFFBEBEBE),
                    fontSize = 12.sp
                )
            }
        }

        // Lista dos alimentos dessa seção
        if (section.items.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            section.items.forEach { food ->
                Text(
                    text = "• ${food.name} (${food.kcal.toInt()} kcal)",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Adicionar",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DateSelector() {
    val dates = listOf("Ter\n11", "Qua\n12", "Qui\n13", "Sex\n14", "Sáb\n15", "Dom\n16")
    val selectedIndex = remember { mutableStateOf(3) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Maio 2023",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dates.size) { index ->
                val selected = index == selectedIndex.value
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selected) Color(0xFF8620AC) else Color.Transparent
                        )
                        .clickable { selectedIndex.value = index }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        dates[index],
                        color = if (selected) Color.White else Color(0xFF8A8A8A),
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
