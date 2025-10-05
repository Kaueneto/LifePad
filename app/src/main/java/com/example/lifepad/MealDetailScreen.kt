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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class NutritionInfo(val icon: Int, val label: String)
data class Ingredient(val name: String, val quantity: String, val icon: Int)
data class Step(val number: Int, val title: String, val description: String)

@Composable
fun MealDetailScreen(
    navController: NavController,
    mealImage: Int,
    mealTitle: String,
    author: String,
    nutrition: List<NutritionInfo>,
    ingredients: List<Ingredient>,
    steps: List<Step>,
    onAddClick: () -> Unit
) {
    var expandedDesc by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color(0xFF2E2B45)) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Box {
                    Image(
                        painter = painterResource(mealImage),
                        contentDescription = mealTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.clickable { navController.popBackStack() }
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Mais opções",
                            tint = Color.White
                        )
                    }
                }
            }

            item {
                // Título e autor
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF332C4B), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(mealTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("Por $author", color = Color(0xFF26A69A), fontSize = 14.sp)
                        }
                        IconButton(onClick = { favorite = !favorite }) {
                            Icon(
                                imageVector = if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favoritar",
                                tint = if (favorite) Color(0xFFE57373) else Color.LightGray
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // Nutrição
                    Text("Nutrição", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(nutrition) { info ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                                    Image(
                                        painter = painterResource(info.icon),
                                        contentDescription = info.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(info.label, color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    // Descrição
                    Text("Descrição", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (expandedDesc) steps.first().description else steps.first().description.take(120) + "...",
                        color = Color(0xFFB0B0B0),
                        fontSize = 14.sp
                    )
                    Text(
                        if (expandedDesc) "Leia Menos" else "Leia Mais...",
                        color = Color(0xFF26A69A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { expandedDesc = !expandedDesc }
                    )

                    Spacer(Modifier.height(16.dp))
                    // Ingredientes
                    Text("Ingredientes que você vai precisar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(ingredients) { ing ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
                                ) {
                                    Image(
                                        painter = painterResource(ing.icon),
                                        contentDescription = ing.name,
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(8.dp)
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(ing.name, color = Color.White, fontSize = 12.sp)
                                Text(ing.quantity, color = Color.LightGray, fontSize = 10.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    // Passo a Passo
                    Text("Passo a Passo", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Column {
                        steps.forEach { step ->
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    step.number.toString().padStart(2, '0'),
                                    color = if (step.number == 1) Color(0xFF26A69A) else Color.LightGray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("${step.number}º Passo", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text(step.description, color = Color(0xFFB0B0B0), fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                    // Botão adicionar
                    Button(
                        onClick = onAddClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8620AC)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Adicione o café da manhã", color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
