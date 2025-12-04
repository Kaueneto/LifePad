import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast




@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit
) {


    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // pode depois sincronizar essas cores com as da LoginScreen
    val backgroundColor = Color(0xFF2E2B45)
    val textColor = Color.White
    val fieldBorderColor = Color(0xFF4A4A4A)
    val buttonColor = Color(0xFFB429C3)
    val linkColor = Color(0xFFB429C3)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(60.dp))
        Text("Crie sua conta!", color = textColor, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        // Nome
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome", color = textColor) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = buttonColor,
                unfocusedBorderColor = fieldBorderColor,
                cursorColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Telefone
        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone", color = textColor) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = buttonColor,
                unfocusedBorderColor = fieldBorderColor,
                cursorColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = textColor) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = textColor) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = buttonColor,
                unfocusedBorderColor = fieldBorderColor,
                cursorColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Senha
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = buttonColor,
                unfocusedBorderColor = fieldBorderColor,
                cursorColor = buttonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Botão Confirmar
        Button(
            onClick = {
                // validação simples
                if (nome.isBlank() || telefone.isBlank() || email.isBlank() || senha.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // 1) criar usuário no Auth
                auth.createUserWithEmailAndPassword(email.trim(), senha.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uid = user?.uid

                            if (uid != null) {
                                val userData = hashMapOf(
                                    "nome" to nome,
                                    "telefone" to telefone,
                                    "email" to email
                                )

                                db.collection("users")
                                    .document(uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                                        onBackToLogin()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            context,
                                            "Erro ao salvar dados: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(context, "Erro ao obter usuário.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val msg = task.exception?.message ?: "Falha ao cadastrar."
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("Confirmar", color = Color.White)
        }


        Spacer(Modifier.weight(1f))

        // Texto "Já possui conta? Volte para o login."
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Já possui conta? ", color = textColor)
            Text(
                "Volte para o login.",
                color = linkColor,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }
}

