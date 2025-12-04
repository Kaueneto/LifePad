package com.example.lifepad

import RegisterScreen
import SplashScreen
import android.graphics.Color.alpha
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.graphicsLayer
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
//tela de login - principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifePadTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                    composable("login") {
                        LoginScreen(
                            onLoginClicked = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClicked = {
                                navController.navigate("register")
                            }
                        )
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


                        MealDetailScreen(
                            navController = navController,
                            mealImage = R.drawable.img_apple_pie,
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
                    composable("register") {
                        RegisterScreen(
                            onBackToLogin = {
                                navController.popBackStack() // volta para a tela anterior (login)
                            }
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
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    var isAluno by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isAluno) Color.White else Color(0xFF2E2B45),
        animationSpec = tween(durationMillis = 500),
        label = "backgroundColor"
    )

    val textColor = if (isAluno) Color.Black else Color.White
    val containerColor = if (isAluno) Color(0xFFEDEDED) else Color(0xFF4A4A4A)
    val selectedColor = if (isAluno) Color(0xFFB429C3) else Color(0xFF4539DF)
    val buttonColor = if (isAluno) Color(0xFFB429C3) else Color(0xFF4539DF)
    val borderFieldColor = if (isAluno) Color(0xFF232024) else Color(0xFFD1CCE4)
    val cadastroColor = if (isAluno) Color(0xFFB429C3) else Color(0xFF8E79E5)
    val socialBorder = if (isAluno) Color(0xFFDEDEDE) else Color(0xFF3A3641)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(60.dp))
        Text("Olá,", style = MaterialTheme.typography.titleMedium, color = textColor)
        Text("Bem vindo de volta!", style = MaterialTheme.typography.titleLarge, color = textColor)
        Spacer(Modifier.height(35.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(containerColor)
        ) {
            val alignment by animateAlignmentAsState(if (isAluno) Alignment.CenterStart else Alignment.CenterEnd)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(alignment)
                    .clip(RoundedCornerShape(32.dp))
                    .background(selectedColor)
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Aluno",
                    color = if (isAluno) Color.White else Color(0xFFBEBEBE),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isAluno = true }
                        .padding(horizontal = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    "Profissional",
                    color = if (!isAluno) Color.White else Color(0xFFBEBEBE),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isAluno = false }
                        .padding(horizontal = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = textColor) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = textColor) },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = selectedColor,
                unfocusedBorderColor = borderFieldColor,
                cursorColor = selectedColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha", color = textColor) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = textColor) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = textColor
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = selectedColor,
                unfocusedBorderColor = borderFieldColor,
                cursorColor = selectedColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Esqueceu a senha?",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onLoginClicked() },
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Login", color = Color.White)
        }

        Spacer(Modifier.height(32.dp))

        Divider(color = Color(0xFFBBBBBB), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { /* Google login */ },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .border(1.dp, socialBorder, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    painterResource(id = R.drawable.icongoogle),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(24.dp))
            IconButton(
                onClick = { /* instagram login */ },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .border(1.dp, socialBorder, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    painterResource(id = R.drawable.iconinsta),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Ainda não tem uma conta? ", color = textColor)
            Text(
                "Cadastre-se",
                color = cadastroColor,
                modifier = Modifier.clickable { onRegisterClicked() }
            )
}
    }
}

@Composable
fun animateAlignmentAsState(targetAlignment: Alignment): State<Alignment> {
    var currentAlignment by remember { mutableStateOf(targetAlignment) }
    LaunchedEffect(targetAlignment) { currentAlignment = targetAlignment }
    return rememberUpdatedState(currentAlignment)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LifePadTheme {
        LoginScreen(
            onLoginClicked = {},
            onRegisterClicked = {}  // <-- adiciona isso
        )
    }
}
