package com.example.lifepad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController

// Data classes para a tela
data class FoodCategory(val name: String, val icon: Int, val color: Color)
data class PopularFood(
    val name: String,
    val difficulty: String,
    val time: String,
    val calories: String,
    val image: Int
)

@Composable
fun BreakfastScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    // Dados das categorias
    val categories = listOf(
        FoodCategory("Salada", R.drawable.salada, Color(0xFF2E7D32)),
        FoodCategory("Bolo", R.drawable.bolo, Color(0xFF7B1FA2)),
        FoodCategory("Torta", R.drawable.torta_maca, Color(0xFF1976D2)),
        FoodCategory("Laranja", R.drawable.laranja, Color(0xFF388E3C))
    )

    // Dados das recomendações
    val recommendedFoods = listOf(
        RecommendedFood(
            "Panqueca de mel",
            "Fácil",
            "30mins",
            "180kCal",
            R.drawable.panqueca,
            Color(0xFF26A69A)
        ),
        RecommendedFood(
            "Roti Canai",
            "Fácil",
            "20mins",
            "230kCal",
            R.drawable.iconroti,
            Color(0xFF7B1FA2)
        )
    )

    // Dados dos populares
    val popularFoods = listOf(
        PopularFood("Panqueca de Blueberry", "Médio", "30mins", "230kCal", R.drawable.panqueca),
        PopularFood("Nigiri de Salmão", "Médio", "20mins", "120kCal", R.drawable.nigiri)
    )

    Scaffold(
        containerColor = Color(0xFF2E2B45),
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
            // Header
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
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        tint = Color.White
                    )
                }
            }

            // Barra de pesquisa
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text("Pesquisar Panqueca", color = Color(0xFF8A8A8A))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF8A8A8A))
                    },
                    trailingIcon = {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = Color(0xFF8A8A8A))
                    },
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
                        .padding(bottom = 24.dp)
                )
            }

            // Categoria
            item {
                Text(
                    text = "Categoria",
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
                    items(categories) { category ->
                        CategoryCard(category)
                    }
                }
            }

            // Recomendação para Dieta
            item {
                Text(
                    text = "Recomendação\npara Dieta",
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
                    items(recommendedFoods) { food ->
                        RecommendedFoodCard(food)
                    }
                }
            }

            // Popular
            item {
                Text(
                    text = "Popular",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(popularFoods) { food ->
                PopularFoodCard(food)
            }

            item {
                Spacer(Modifier.height(80.dp))
            }
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
                        colors = listOf(category.color, category.color.copy(alpha = 0.7f))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { /* TODO: ação da categoria */ },
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
            text = category.name,
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
            .clickable { /* TODO: ação do item */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(food.image),
                contentDescription = food.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = food.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${food.difficulty} | ${food.time} | ${food.calories}",
                color = Color(0xFF8A8A8A),
                fontSize = 12.sp
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { /* TODO: ver receita */ },
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
            .clickable { /* TODO: ação do item */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
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
                Text(
                    text = food.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${food.difficulty} | ${food.time} | ${food.calories}",
                    color = Color(0xFF8A8A8A),
                    fontSize = 14.sp
                )
            }
            Icon(
                painter = painterResource(R.drawable.iconavatar), // Substitua pelo ícone de seta/play
                contentDescription = "Ver mais",
                tint = Color(0xFF8A8A8A),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
