package com.example.lifepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lifepad.ui.theme.LifePadTheme
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.lifepad.R.drawable.iconfacebook
import com.example.lifepad.NavigationBar
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifePadTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    if (isLoggedIn) {
                        HomeScreen() // Tela inicial após login
                    } else {
                        LoginScreen(
                            modifier = Modifier.padding(innerPadding),
                            onLoginClicked = { isLoggedIn = true } // navega pra HomeScreen
                        )
                    }
                }
            }
        }
    }
}

// Passando callback onLoginClicked
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
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text("Olá,", style = MaterialTheme.typography.titleMedium)
        Text("Bem vindo de volta!", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        // Alternar entre aluno/profissional
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { isAluno = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAluno) Color(0xFFB429C3) else Color(0xFF232024)
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Aluno") }

            Button(
                onClick = { isAluno = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isAluno) Color(0xFF4539DF) else Color.Transparent
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Profissional") }
        }

        Spacer(Modifier.height(24.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de Senha
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
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

        // Botão de login
        Button(
            onClick = { onLoginClicked() }, // chama o callback do MainActivity
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Login, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Login")
        }

        // Divider
        Divider(Modifier.padding(vertical = 16.dp))

        // Login com Google/Facebook
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {/* Login Google */}) {
                Icon(painterResource(id = R.drawable.icongoogle), contentDescription = null)
            }
            Spacer(Modifier.width(16.dp))
            IconButton(onClick = {/*Login Facebook*/}) {
                Icon(painterResource(id = iconfacebook), contentDescription = null)
            }
        }

        Spacer(Modifier.height(16.dp))
        Row {
            Text("Ainda não tem uma conta? ")
            Text(
                "Cadastre-se",
                color = Color(0xFFB429C3),
                modifier = Modifier.clickable { /* Navegar para tela de cadastro */ }
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
