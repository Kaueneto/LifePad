import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import org.threeten.bp.LocalTime

@Composable
fun HidratacaoScreen(
    navController: NavController,
    viewModel: HidratacaoViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current

    val animatedLevel by animateFloatAsState(
        targetValue = (uiState.percentualMeta / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "waterProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2846))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TopAppBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.14f), shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "Hidratação",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { /* menu */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.14f), shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                }
            }

            // Card com gradiente (texto + botão + ícone)
            GradientHighlight(
                totalLitros = uiState.totalLitros,
                onDefinirClick = { viewModel.abrirDialogMeta() },
                modifier = Modifier
            )

            Spacer(Modifier.height(18.dp))

            // Progresso + cards laterais
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WaterGoalIndicator(
                        progress = animatedLevel,
                        goalLabel = "${(uiState.metaLitros * 1000).toInt()} ml"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    // Última ingestão
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Última ingestão", color = Color(0xFF243047), fontSize = 13.sp)

                                val ultimaLinha =
                                    if (uiState.ultimaIngestaoMl > 0)
                                        "${uiState.ultimaIngestaoMl} ml • ${uiState.ultimaIngestaoHora}"
                                    else
                                        "Ainda não registrado hoje"

                                Text(
                                    ultimaLinha,
                                    color = Color(0xFF243047),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(90))
                                    .background(Color(0xFFE7F5FF)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxHeight()
                                        .width(((uiState.percentualMeta.coerceIn(0, 100)) * 0.44f).dp)
                                        .background(Color(0xFF48B3E8))
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Meta diária", color = Color(0xFF243047), fontSize = 13.sp)
                            Spacer(Modifier.weight(1f))
                            Text(
                                "${(uiState.metaLitros * 1000).toInt()} ml",
                                color = Color(0xFF243047),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                if (uiState.totalLitros == 0.0)
                    "Vamos lá, beba seu primeiro copo de hoje"
                else uiState.textoAviso,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(Modifier.height(12.dp))

            BluePrimaryButtonAlt(
                text = "+ 200 ml",
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = { viewModel.adicionarCopo(context) }
            )

            Spacer(Modifier.height(10.dp))

            BluePrimaryButtonAlt(
                text = "Ver desempenho",
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(Modifier.height(8.dp))

            BluePrimaryButtonAlt(
                text = "Lembretes",
                modifier = Modifier.fillMaxWidth(0.9f),
                icon = { Icon(Icons.Default.AccessAlarm, contentDescription = null, tint = Color.White) }
            )
        }

        // Dialog de meta
        if (uiState.dialogMetaAberto) {
            DefinirMetaDialog(
                metaAtual = uiState.metaLitros.toString(),
                onConfirm = { nova -> viewModel.salvarMeta(nova, context) },
                onDismiss = { viewModel.fecharDialogMeta() }
            )
        }
    }
}

@Composable
fun GradientHighlight(
    totalLitros: Double,
    onDefinirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFFA5D8FF), Color(0xFF48B3E8))),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${(totalLitros * 1000).toInt()} ml ingeridos",
                    color = Color(0xFF0F1B2B),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onDefinirClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 40.dp)
                ) {
                    Text("Definir objetivo", color = Color(0xFF48B3E8), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = null,
                tint = Color(0xFF0F3F5A),
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

@Composable
fun WaterGoalIndicator(progress: Float, goalLabel: String) {
    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            drawCircle(color = Color(0xFF062534).copy(alpha = 0.6f), radius = radius)
            drawArc(
                brush = Brush.sweepGradient(listOf(Color(0xFF62CCF9), Color(0xFF48B3E8))),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
            drawCircle(
                brush = Brush.verticalGradient(listOf(Color(0xFF0F3F5A), Color(0xFF062534))),
                radius = radius - 18.dp.toPx(),
                center = Offset(size.width / 2, size.height / 2)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Meta", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            Text(goalLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text("${(progress * 100).roundToInt()}%", color = Color(0xFF62CCF9), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun BluePrimaryButtonAlt(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 4.dp)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48B3E8))
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.width(10.dp))
        }
        Text(text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ViewModel e estado
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

                // Lê os campos da última ingestão
                val ultimaMl = doc.getLong("ultimaIngestaoMl")?.toInt() ?: 0
                val ultimaHora = doc.getString("ultimaIngestaoHora") ?: ""

                val totalLitros = totalMl / 1000.0
                val percentual = if (meta > 0) ((totalLitros / meta) * 100).roundToInt() else 0

                val aviso =
                    if (totalMl == 0L) "Vamos lá, beba seu primeiro copo de água do dia"
                    else "Você já ingeriu $percentual% da sua meta"

                setState {
                    copy(
                        dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        totalLitros = totalLitros,
                        metaLitros = meta,
                        percentualMeta = percentual,
                        textoAviso = aviso,
                        ultimaIngestaoMl = ultimaMl,
                        ultimaIngestaoHora = ultimaHora
                    )
                }
            }
            .addOnFailureListener {
                // Se não existir documento ainda, cria com valores padrão
                setState {
                    copy(
                        dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        totalLitros = 0.0,
                        metaLitros = 2.0,
                        percentualMeta = 0,
                        textoAviso = "Vamos lá, beba seu primeiro copo de água do dia",
                        ultimaIngestaoMl = 0,
                        ultimaIngestaoHora = ""
                    )
                }
            }
    }

    fun adicionarCopo(context: Context) {
        if (uid.isBlank()) {
            Toast.makeText(context, "UID vazio, não salvou", Toast.LENGTH_SHORT).show()
            return
        }

        val hojeId = LocalDate.now().toString()
        val horaAgora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

        val novoTotal = ((_uiState.value.totalLitros * 1000).toInt() + 200)
        val totalLitros = novoTotal / 1000.0
        val percentual = if (_uiState.value.metaLitros > 0) {
            ((totalLitros / _uiState.value.metaLitros) * 100).roundToInt()
        } else 0

        firestore.collection("users")
            .document(uid)
            .collection("consumo")
            .document(hojeId)
            .set(
                mapOf(
                    "totalMl" to novoTotal,
                    "metaLitros" to _uiState.value.metaLitros,
                    "ultimaIngestaoMl" to 200,
                    "ultimaIngestaoHora" to horaAgora
                )
            )
            .addOnSuccessListener {
                setState {
                    copy(
                        totalLitros = totalLitros,
                        percentualMeta = percentual,
                        textoAviso = "Você já ingeriu $percentual% da sua meta",
                        ultimaIngestaoMl = 200,
                        ultimaIngestaoHora = horaAgora
                    )
                }
                Toast.makeText(context, "Adicionado +200ml", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro ao salvar no banco", Toast.LENGTH_SHORT).show()
            }
    }

    fun abrirDialogMeta() = setState { copy(dialogMetaAberto = true) }
    fun fecharDialogMeta() = setState { copy(dialogMetaAberto = false) }

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
                    "metaLitros" to valor,
                    "ultimaIngestaoMl" to _uiState.value.ultimaIngestaoMl,
                    "ultimaIngestaoHora" to _uiState.value.ultimaIngestaoHora
                )
            )

        fecharDialogMeta()
        carregarDados()
        Toast.makeText(context, "Meta atualizada!", Toast.LENGTH_SHORT).show()
    }
}

data class HidratacaoState(
    val dataHoje: String = "",
    val totalLitros: Double = 0.0,
    val metaLitros: Double = 2.0,
    val percentualMeta: Int = 0,
    val textoAviso: String = "",
    val dialogMetaAberto: Boolean = false,
    val ultimaIngestaoMl: Int = 0,
    val ultimaIngestaoHora: String = ""
)

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
