package com.example.lifepad

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B45))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = com.example.lifepad.R.drawable.iconavatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Bem Vindo!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Nicolas Magalhães",
                        color = Color(0xFFD1CCE4),
                        fontSize = 14.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificações",
                    tint = Color(0xFFD1CCE4)
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Mais",
                    tint = Color(0xFFD1CCE4)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFA237E4), Color(0xFFA237E4))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                        .height(120.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "IMC (Índice de Massa Corporal)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                "Você tem um peso normal.",
                                color = Color(0xFFD1CCE4),
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { /*TODO ver mais*/ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFFA237E4
                                )
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    "Ver mais.",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Spacer(Modifier.width(24.dp))
                        Box {
                            Canvas(modifier = Modifier.size(80.dp)) {
                                drawArc(
                                    color = Color(0xFF390052),
                                    startAngle = 120f,
                                    sweepAngle = 300f,
                                    useCenter = true
                                )
                                drawArc(
                                    color = Color(0x27B0F5),
                                    startAngle = 120f,
                                    sweepAngle = 120f,
                                    useCenter = true
                                )
                            }
                            Text(
                                "20,1",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4539DF))
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Monitor de atividades",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { /*TODO check*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA16CED)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Check", color = Color.White)
                    }
                }
            }

            Text(
                "Status da Atividade",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF332C4B))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Frequência cardíaca", color = Color(0xFFD1CCE4))
                            Text(
                                "78 BPM",
                                color = Color(0xFFE9449B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF7635B4), Color(0xFFBE4BBB))
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("3min atrás", color = Color.White, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)) {
                        val path = androidx.compose.ui.graphics.Path()
                        val points = listOf(20f, 50f, 30f, 30f, 50f, 70f, 70f, 40f, 90f, 60f, 110f, 55f, 130f, 30f)
                        path.moveTo(0f, size.height)
                        for (i in points.indices step 2) {
                            path.lineTo(points[i], points[i + 1])
                        }
                        path.lineTo(size.width, size.height)
                        path.close()
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFE9449B), Color.Transparent)
                            ),
                            alpha = 0.7f
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Consumo de Água", color = Color.White)
                        Text("4 Litros", color = Color(0xFF6299FF), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Tempo Real", color = Color(0xFF7F7F7F), fontSize = 12.sp)
                        Spacer(Modifier.height(12.dp))
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sono", color = Color.White)
                        Text("8h 20m", color = Color(0xFF57B197), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(Modifier.height(12.dp))
                        Canvas(modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()) {
                            drawPath(
                                path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(0f, size.height / 2)
                                    cubicTo(size.width / 4, size.height, size.width / 2, 0f, size.width, size.height / 2)
                                },
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFF57B197), Color(0xFF3E826D))
                                ),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Calorias", color = Color.White)
                        Text("760 kCal", color = Color(0xFF8E70D2), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("230 kCal left", color = Color(0xFF8E70D2), fontSize = 12.sp)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Progresso no Treino",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "Por dias da Semana",
                    color = Color(0xFFDA56CD),
                    fontSize = 14.sp
                )
            }

            TrainingBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 20.dp)
            )

            Text(
                "Últimos treinos",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            //scrool pra mostrar os treinos
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF332C4B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(3) { index ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF4539DF))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.lifepad.R.drawable.iconavatar),
                                    contentDescription = "Treino Ícone",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        when (index) {
                                            0 -> "Treino de Corpo Inteiro"
                                            1 -> "Treino de Inferiores"
                                            else -> "Treino de Abdômen"
                                        },
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        when (index) {
                                            0 -> "180 Calorias queimadas | 20 minutos"
                                            1 -> "200 Calorias queimadas | 30 minutos"
                                            else -> "180 Calorias queimadas | 20 minutos"
                                        },
                                        color = Color(0xFFD1CCE4),
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "Detalhes",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }

        CustomBottomNavigation(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TrainingBarChart(
    modifier: Modifier = Modifier,
    barValues: List<Int> = listOf(80, 40, 50, 80, 70, 0, 30),
    barColors: List<Color> = listOf(
        Color(0xFFE57373),
        Color(0xFFF06292),
        Color(0xFFBA68C8),
        Color(0xFF64B5F6),
        Color(0xFF81C784),
        Color(0xFFFFB74D),
        Color(0xFF90A4AE)
    ),
    dayLabels: List<String> = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
) {
    val maxValue = (barValues.maxOrNull() ?: 1).toFloat()
    val barWidth = 22.dp
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            val space = 14.dp.toPx()
            val widthPerBar = barWidth.toPx() + space
            val startX = (size.width - (barValues.size * widthPerBar - space)) / 2f
            barValues.forEachIndexed { idx, value ->
                val left = startX + idx * widthPerBar
                val barHeight = (value / maxValue) * size.height
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(barColors[idx % barColors.size], Color(0x80000000))
                    ),
                    topLeft = androidx.compose.ui.geometry.Offset(left, size.height - barHeight),
                    size = Size(barWidth.toPx(), barHeight),
                    cornerRadius = CornerRadius(barWidth.toPx() / 2)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayLabels.forEach { day ->
                Text(day, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
