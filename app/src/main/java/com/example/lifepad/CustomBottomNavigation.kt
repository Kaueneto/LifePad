package com.example.lifepad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CustomBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // observa a rota atual para destacar o ícone ativo
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF232024))
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = R.drawable.iconhome,
                isSelected = currentRoute == "home",
                onClick = {
                    if (currentRoute != "home") {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
            BottomNavItem(
                icon = R.drawable.iconfood,
                isSelected = currentRoute == "foodMain",
                onClick = {
                    if (currentRoute != "foodMain") {
                        navController.navigate("foodMain")
                    }
                }
            )
            BottomNavItem(
                icon = R.drawable.icongraficos,
                isSelected = currentRoute == "",
                onClick = {
                    if (currentRoute != "foodMain") {                 //ajustar aqui depois que tiver a tela pronta
                        navController.navigate("foodMain")    //aqui tambem
                    }
                }


            )
            BottomNavItem(
                icon = R.drawable.iconperfil,
                isSelected = currentRoute == "",
                onClick = {
                    if (currentRoute != "foodMain") {                 //ajustar aqui depois que tiver a tela pronta
                        navController.navigate("foodMain")    //aqui tambem
                    }
                }


            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = if (isSelected) Color.White else Color(0xFF7F7F7F), // Branco se selecionado, cinza se não
        modifier = Modifier
            .size(28.dp)
            .clickable(onClick = onClick)
    )
}
