package com.example.lifepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lifepad.ui.theme.LifePadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifePadTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onLoginClicked = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("foodMain") {
                        MealScheduleScreen(navController = navController)
                    }
                    composable("breakfast") {
                        BreakfastScreen(navController = navController)
                    }
                    composable(
                        route = "mealDetail/{mealName}",
                        arguments = listOf(navArgument("mealName") {
                            type = NavType.StringType
                        })
                    ) { backStack ->
                        val mealName = backStack.arguments?.getString("mealName") ?: ""
                        // Exemplo fixo de dados; ajuste conforme necessário
                        MealDetailScreen(
                            navController = navController,
                            mealImage = R.drawable.panqueca,
                            mealTitle = mealName,
                            author = "Arash Ranjbaran Qadikolaei",
                            nutrition = listOf(
                                NutritionInfo(R.drawable.ic_fire, "180kCal"),
                                NutritionInfo(R.drawable.ic_nut, "30g gord."),
                                NutritionInfo(R.drawable.ic_protein, "20g proteínas"),
                                NutritionInfo(R.drawable.ic_carb, "50g carbo")
                            ),
                            ingredients = listOf(
                                Ingredient("Farinha de Trigo", "100g", R.drawable.ic_flour),
                                Ingredient("Açúcar", "3 colheres", R.drawable.ic_sugar),
                                Ingredient("Fermento", "2 colheres", R.drawable.ic_yeast),
                                Ingredient("Ovos", "2 itens", R.drawable.ic_eggs)
                            ),
                            steps = listOf(
                                Step(1, "Prepare ingredientes", "Prepare todos os ingredientes necessários"),
                                Step(2, "Misture secos", "Misture farinha, açúcar, sal e fermento"),
                                Step(3, "Misture líquidos", "Em recipiente separado, misture ovos e leite"),
                                Step(4, "Cozinhe", "Despeje a massa e cozinhe até dourar")
                            ),
                            onAddClick = { /* TODO: adicionar ação */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit
) {
    var isAluno by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B45))
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text("Olá,", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text("Bem vindo de volta!", style = MaterialTheme.typography.titleLarge, color = Color.White)

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF4A4A4A)),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { isAluno = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAluno) Color(0xFFB429C3) else Color.Transparent
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Aluno", color = Color.White) }

            Button(
                onClick = { isAluno = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isAluno) Color(0xFF4539DF) else Color.Transparent
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Profissional", color = Color.White) }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.White) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFB429C3),
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFB429C3),
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            "Esqueceu a senha?",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, bottom = 16.dp)
        )

        Button(
            onClick = { onLoginClicked() },
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB429C3)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Login", color = Color.White)
        }

        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { /* Google login */ }) {
                Icon(painterResource(id = R.drawable.icongoogle), contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            IconButton(onClick = { /* Facebook login */ }) {
                Icon(painterResource(id = R.drawable.iconfacebook), contentDescription = null, tint = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))
        Row {
            Text("Ainda não tem uma conta? ", color = Color.White)
            Text(
                "Cadastre-se",
                color = Color(0xFFB429C3),
                modifier = Modifier.clickable { /* Navegar para cadastro */ }
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LifePadTheme {
        LoginScreen(onLoginClicked = {})
    }
}
