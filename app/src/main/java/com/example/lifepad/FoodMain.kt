package com.example.lifepad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lifepad.RecommendedFood


data class FoodMain(val name: String, val time: String, val imageRes: Int)
data class MealSection(val title: String, val info: String, val items: List<FoodMain>)
data class RecommendedFood(
    val name: String,
    val difficulty: String,
    val time: String,
    val calories: String,
    val image: Int,
    val buttonColor: Color
)

@Composable
fun MealScheduleScreen(navController: NavController) {
    val mealSections = listOf(
        MealSection(
            "Café da manhã", "2 refeições | 230 calorias",
            listOf(
                FoodMain("Panqueca de Mel", "07:00h", R.drawable.panqueca),
                FoodMain("Café", "07:30h", R.drawable.cafe)
            )
        ),
        MealSection(
            "Almoço", "2 refeições | 500 calorias",
            listOf(
                FoodMain("Filé de Frango", "01:00h", R.drawable.frango),
                FoodMain("Leite", "01:20h", R.drawable.leite)
            )
        ),
        MealSection(
            "Lanche", "2 refeições | 140 calorias",
            listOf(
                FoodMain("Laranja", "04:30h", R.drawable.laranja),
                FoodMain("Torta de Maçã", "04:40h", R.drawable.torta_maca)
            )
        )
    )

    val recommendedFoods = listOf(
        RecommendedFood("Panqueca de Mel", "Fácil", "30mins", "180kCal", R.drawable.panqueca, Color(0xFF26A69A)),
        RecommendedFood("Roti Canai", "Fácil", "20mins", "230kCal", R.drawable.iconroti, Color(0xFF7B1FA2))
    )

    Scaffold(
        containerColor = Color(0xFF2E2B45),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("breakfast") },
                containerColor = Color(0xFF8620AC)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Refeição", tint = Color.White)
            }
        },
        bottomBar = {
            CustomBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item { DateSelector() }

            mealSections.forEach { section ->
                item {
                    SectionHeader(
                        title = section.title,
                        info = section.info,
                        onTitleClick = {
                            if (section.title == "Café da manhã") {
                                navController.navigate("breakfast")
                            }
                        }
                    )
                }
                items(section.items) { food ->
                    MealItem(food) { navController.navigate("mealDetail/${food.name}") }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Recomendação para Dieta",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(recommendedFoods) { food ->
                        RecommendedFoodCard(food) { navController.navigate("mealDetail/${food.name}") }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SectionHeader(title: String, info: String, onTitleClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTitleClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(info, color = Color.LightGray, fontSize = 14.sp)
    }
}

@Composable
fun MealItem(food: FoodMain, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = food.imageRes),
            contentDescription = food.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(food.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(food.time, color = Color(0xFF8A8A8A), fontSize = 14.sp)
        }
        Icon(
            painter = painterResource(R.drawable.icon_forward),
            contentDescription = "Detalhes",
            tint = Color(0xFF8A8A8A),
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun RecommendedFoodCard(food: RecommendedFood, onViewClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A)),
        modifier = Modifier
            .width(200.dp)
            .height(260.dp)
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
            Text("${food.difficulty} | ${food.time} | ${food.calories}", color = Color(0xFF8A8A8A), fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onViewClick,
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
            Text("Maio 2023", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dates.size) { index ->
                val selected = index == selectedIndex.value
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) Color(0xFF8620AC) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { selectedIndex.value = index },
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
