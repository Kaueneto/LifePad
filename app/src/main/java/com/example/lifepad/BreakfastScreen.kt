// kotlin
package com.example.lifepad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FoodCategory(val name: String, val icon: Int, val color: Color)

data class PopularFood(
    val name: String,
    val difficulty: String,
    val time: String,
    val calories: String,
    val image: Int
)

data class RecommendedFood(
    val name: String,
    val difficulty: String,
    val time: String,
    val calories: String,
    val image: Int,
    val buttonColor: Color
)

data class Alimento(
    val id: String = "",
    val description: String = "",
    val category: String = "",
    val kcal: Double = 0.0
)

class BuscarAlimentoViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val uid: String = auth.currentUser?.uid ?: ""

    var searchText by mutableStateOf("")
        private set

    private var todosAlimentos: List<Alimento> = emptyList()
    var alimentosFiltrados by mutableStateOf<List<Alimento>>(emptyList())
        private set

    init {
        carregarAlimentos()
    }

    private fun carregarAlimentos() {
        firestore.collection("alimentos")
            .get()
            .addOnSuccessListener { snap ->
                println("DEBUG_FIREBASE total docs: ${snap.size()}")
                val lista = snap.documents.map { doc ->
                    println(
                        "DEBUG_FIREBASE doc: ${doc.id} desc=${doc.getString("description")} " +
                                "cat=${doc.getString("category")} kcal=${doc.getDouble("energy_kcal")}"
                    )
                    Alimento(
                        id = doc.id,
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "",
                        kcal = doc.getDouble("energy_kcal") ?: 0.0
                    )
                }
                todosAlimentos = lista
                aplicarFiltro()
            }
            .addOnFailureListener { e ->
                println("DEBUG_FIREBASE erro: ${e.message}")
            }
    }

    fun onSearchChange(novo: String) {
        searchText = novo
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        val q = searchText.trim().lowercase()
        alimentosFiltrados = if (q.isEmpty()) {
            todosAlimentos
        } else {
            todosAlimentos.filter { it.description.lowercase().contains(q) }
        }
    }

    fun salvarAlimentoSelecionado(alimento: Alimento) {
        if (uid.isBlank()) {
            println("DEBUG_FIREBASE: uid vazio, não salvou alimento")
            return
        }

        val hojeId = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val docRef = firestore.collection("users")
            .document(uid)
            .collection("refeicoes")
            .document(hojeId)
            .collection("itens")
            .document() // id aleatório

        val payload = mapOf(
            "description" to alimento.description,
            "category" to alimento.category,
            "kcal" to alimento.kcal,
            "mealType" to "breakfast",
            "timestamp" to System.currentTimeMillis()
        )

        docRef.set(payload)
            .addOnSuccessListener {
                println("DEBUG_FIREBASE: alimento salvo em $hojeId")
            }
            .addOnFailureListener { e ->
                println("DEBUG_FIREBASE: erro ao salvar alimento: ${e.message}")
            }
    }
}

@Composable
fun BreakfastScreen(
    navController: NavController,
    viewModel: BuscarAlimentoViewModel = viewModel()
) {
    val searchText = viewModel.searchText
    val alimentos = viewModel.alimentosFiltrados

    val categories = listOf(
        FoodCategory("Salada", com.example.lifepad.R.drawable.salada, Color(0xFF2E7D32)),
        FoodCategory("Bolo", com.example.lifepad.R.drawable.bolo, Color(0xFF7B1FA2)),
        FoodCategory("Torta", com.example.lifepad.R.drawable.torta_maca, Color(0xFF1976D2)),
        FoodCategory("Laranja", com.example.lifepad.R.drawable.laranja, Color(0xFF388E3C))
    )

    val recommendedFoods = listOf(
        RecommendedFood(
            "Panqueca de mel",
            "Fácil",
            "30mins",
            "180kCal",
            com.example.lifepad.R.drawable.panqueca,
            Color(0xFF26A69A)
        ),
        RecommendedFood(
            "Roti Canai",
            "Fácil",
            "20mins",
            "230kCal",
            com.example.lifepad.R.drawable.iconroti,
            Color(0xFF7B1FA2)
        )
    )

    val popularFoods = listOf(
        PopularFood("Panqueca de Blueberry", "Médio", "30mins", "230kCal", com.example.lifepad.R.drawable.panqueca),
        PopularFood("Nigiri de Salmão", "Médio", "20mins", "120kCal", com.example.lifepad.R.drawable.nigiri)
    )

    Scaffold(
        containerColor = Color(0xFF2E2B45),
        bottomBar = {}
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Café da manhã",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color.White)
                }
            }

            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchChange(it) },
                    placeholder = { Text("Pesquisar alimento", color = Color(0xFF8A8A8A)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF8A8A8A)) },
                    trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, tint = Color(0xFF8A8A8A)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF4A4A4A),
                        unfocusedBorderColor = Color(0xFF4A4A4A),
                        focusedContainerColor = Color(0xFF4A4A4A),
                        unfocusedContainerColor = Color(0xFF4A4A4A)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            items(alimentos) { alimento ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            viewModel.salvarAlimentoSelecionado(alimento)
                            navController.popBackStack()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(alimento.description, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("${alimento.kcal.toInt()} kcal", color = Color(0xFFBBBBBB), fontSize = 12.sp)
                        if (alimento.category.isNotBlank()) {
                            Text(alimento.category, color = Color(0xFFBBBBBB), fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Text(
                    "Categoria",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(categories) { CategoryCard(it) }
                }
            }

            item {
                Text(
                    "Recomendação\npara Dieta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(recommendedFoods) { RecommendedFoodCard(it) }
                }
            }

            item {
                Text(
                    "Popular",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(popularFoods) { PopularFoodCard(it) }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun CategoryCard(category: FoodCategory) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(category.color, category.color.copy(alpha = 0.7f))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { /* ação ao clicar na categoria */ },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.icon),
                contentDescription = category.name,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            category.name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RecommendedFoodCard(food: RecommendedFood) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(260.dp)
            .clickable { /* ver detalhes da refeição recomendada */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(food.image),
                contentDescription = food.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(12.dp))
            Text(food.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "${food.difficulty} | ${food.time} | ${food.calories}",
                color = Color(0xFF8A8A8A),
                fontSize = 12.sp
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { /* ver receita */ },
                colors = ButtonDefaults.buttonColors(containerColor = food.buttonColor),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver", color = Color.White)
            }
        }
    }
}

@Composable
fun PopularFoodCard(food: PopularFood) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { /* abrir detalhe do popular */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(food.image),
                contentDescription = food.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(food.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${food.difficulty} | ${food.time} | ${food.calories}",
                    color = Color(0xFF8A8A8A),
                    fontSize = 14.sp
                )
            }
            Icon(
                painter = painterResource(com.example.lifepad.R.drawable.iconperfil),
                contentDescription = "Ver mais",
                tint = Color(0xFF8A8A8A),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
