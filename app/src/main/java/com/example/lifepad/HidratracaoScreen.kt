package com.example.lifepad

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

import kotlin.math.roundToInt

// necessários para usar "by" com State / mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// *****************************************************************************************
// ***  COMPOSABLE PRINCIPAL                                                            ***
// *****************************************************************************************

@Composable
fun HidratacaoScreen(
    navController: NavController,
    viewModel: HidratacaoViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current

    val animatedLevel by animateFloatAsState(
        targetValue = (uiState.percentualMeta / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.height(8.dp))

            // Card: Data atual e total ingerido (em litros)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00121A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data: ${uiState.dataHoje}", color = Color.White, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Ingerido hoje: ${uiState.totalLitros} L",
                        color = Color(0xFF4FD3E6),
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Área visual da água subindo
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF111111))
                    .padding(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xFF0A0A0A))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(animatedLevel)
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFF4FD3E6))
                )
            }

            Spacer(Modifier.height(20.dp))

            // Círculo da meta
            Card(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00121A))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sua meta é", color = Color.White, fontSize = 14.sp)
                        Text(
                            "${uiState.metaLitros} L",
                            color = Color(0xFF4FD3E6),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = uiState.textoAviso,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.adicionarCopo(context) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FD3E6)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text("+ 200 ml", color = Color.Black, fontSize = 16.sp)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.abrirDialogMeta() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
            ) {
                Text("Definir nova meta", color = Color.Black, fontSize = 14.sp)
            }
        }

        if (uiState.dialogMetaAberto) {
            DefinirMetaDialog(
                metaAtual = uiState.metaLitros.toString(),
                onConfirm = { nova -> viewModel.salvarMeta(nova, context) },
                onDismiss = { viewModel.fecharDialogMeta() }
            )
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }
    }
}

// *****************************************************************************************
// ***  VIEWMODEL                                                                       ***
// *****************************************************************************************

class HidratacaoViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val uid: String = auth.currentUser?.uid ?: ""

    private val _uiState = mutableStateOf(HidratacaoState())
    val uiState: State<HidratacaoState> = _uiState

    init {
        carregarDados()
    }

    private fun setState(update: HidratacaoState.() -> HidratacaoState) {
        _uiState.value = _uiState.value.update()
    }


    fun carregarDados() {
        if (uid.isBlank()) return

        val hojeId = LocalDate.now().toString()

        firestore.collection("users")
            .document(uid)
            .collection("consumo")
            .document(hojeId)
            .get()
            .addOnSuccessListener { doc ->
                val totalMl = doc.getLong("totalMl") ?: 0
                val meta = doc.getDouble("metaLitros") ?: 2.0

                val totalLitros = totalMl / 1000.0
                val percentual = if (meta > 0) ((totalLitros / meta) * 100).roundToInt() else 0

                val aviso =
                    if (totalMl == 0L) "Vamos lá, beba seu primeiro copo de água do dia"
                    else "Você já ingeriu $percentual% da sua meta"

                setState {
                    copy(
                        dataHoje = LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        totalLitros = totalLitros,
                        metaLitros = meta,
                        percentualMeta = percentual,
                        textoAviso = aviso
                    )
                }
            }
    }

    fun adicionarCopo(context: Context) {
        if (uid.isBlank()) return
        val hojeId = LocalDate.now().toString()

        val novoTotal = ((_uiState.value.totalLitros * 1000).toInt() + 200)
        val totalLitros = novoTotal / 1000.0
        val percentual = ((totalLitros / _uiState.value.metaLitros) * 100).roundToInt()

        firestore.collection("users")
            .document(uid)
            .collection("consumo")
            .document(hojeId)
            .set(
                mapOf(
                    "totalMl" to novoTotal,
                    "metaLitros" to _uiState.value.metaLitros
                )
            )

        setState {
            copy(
                totalLitros = totalLitros,
                percentualMeta = percentual,
                textoAviso = "Você já ingeriu $percentual% da sua meta"
            )
        }

        Toast.makeText(context, "Adicionado +200ml", Toast.LENGTH_SHORT).show()
    }

    fun abrirDialogMeta() = setState {
        copy(dialogMetaAberto = true)
    }

    fun fecharDialogMeta() = setState {
        copy(dialogMetaAberto = false)
    }

    fun salvarMeta(novaMeta: String, context: Context) {
        val valor = novaMeta.toDoubleOrNull()
        if (valor == null || valor <= 0) {
            Toast.makeText(context, "Meta inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val hojeId = LocalDate.now().toString()

        firestore.collection("users")
            .document(uid)
            .collection("consumo")
            .document(hojeId)
            .set(
                mapOf(
                    "totalMl" to (_uiState.value.totalLitros * 1000).toInt(),
                    "metaLitros" to valor
                )
            )

        fecharDialogMeta()
        carregarDados()

        Toast.makeText(context, "Meta atualizada!", Toast.LENGTH_SHORT).show()
    }
}

// *****************************************************************************************
// ***  STATE                                                                           ***
// *****************************************************************************************

data class HidratacaoState(
    val dataHoje: String = "",
    val totalLitros: Double = 0.0,
    val metaLitros: Double = 2.0,
    val percentualMeta: Int = 0,
    val textoAviso: String = "",
    val dialogMetaAberto: Boolean = false
)

// *****************************************************************************************
// ***  DIALOG DEFINIR META                                                             ***
// *****************************************************************************************

@Composable
fun DefinirMetaDialog(
    metaAtual: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var meta by remember { mutableStateOf(metaAtual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Definir nova meta") },
        text = {
            OutlinedTextField(
                value = meta,
                onValueChange = { meta = it },
                label = { Text("Meta em litros") }
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(meta) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
