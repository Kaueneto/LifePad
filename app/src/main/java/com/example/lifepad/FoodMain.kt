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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class FoodMain(val name: String, val time: String, val imageRes: Int)
data class MealSection(val title: String, val info: String, val items: List<FoodMain>)

@Composable
fun MealScheduleScreen(navController: NavController) {
    val breakfastItems: List<FoodMain> = emptyList()
    val lunchItems: List<FoodMain> = emptyList()
    val snackItems: List<FoodMain> = emptyList()

    val mealSections = listOf(
        MealSection("Café da manhã", "0 refeições | 0 kcal", breakfastItems),
        MealSection("Almoço", "0 refeições | 0 kcal", lunchItems),
        MealSection("Lanche", "0 refeições | 0 kcal", snackItems)
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
