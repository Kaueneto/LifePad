package com.example.lifepad

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SleepTrackerScreen(navController: NavController) {
    val selectedDay = remember { mutableStateOf(3) } // Qui (index 3)
    val days = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
    val highlightColor = Color(0xFF2DFF9B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B45))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            // AppBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 18.dp, end = 18.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "Monitor de sono",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Mais", tint = Color.White)
                }
            }

            // GRÁFICO
            SleepLineChart(
                progress = listOf(4f, 5.8f, 4f, 7f, 6f, 8f, 8f),
                days = days,
                highlightIndex = 3,
                comment = "Aumento de 43%",
                highlightColor = highlightColor
            )

            // Card verde resumo -- AGORA CLICÁVEL
            Box(
                modifier = Modifier
                    .padding(horizontal = 22.dp, vertical = 14.dp)
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF08D97C), Color(0xFF18BBA3))),
                        RoundedCornerShape(36.dp)
                    )
                    .clickable { navController.navigate("sleepMonitor") } // Torna clicável!
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text("Sono da Noite Passada", color = Color.White, fontSize = 19.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("8h 20m", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    // Onda desenhada
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .offset(y = 2.dp)) {
                        val path = android.graphics.Path()
                        val control = size.width / 4f
                        val segWidth = size.width / 3f
                        for(j in 0..2) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.27f),
                                start = Offset(j * segWidth, size.height/2),
                                end = Offset((j+1) * segWidth, size.height/2)
                            )
                        }
                    }
                }
            }

            // Card roxo + botão
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 8.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF6527D1), Color(0xFFB86EFF))),
                        RoundedCornerShape(22.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Programação de Sono Diária", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /* agendar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D2892)),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text("Marcar", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text(
                "Programação para Hoje",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 6.dp)
            )

            // CARD HORÁRIO DE DORMIR
            SleepControlCard(
                title = "Hora de Dormir",
                time = "21:00h",
                emojiId = R.drawable.ic_bed, // Substitua pelo seu drawable
                highlight = "6horas e 22minutos",
                checked = true
            )
            Spacer(Modifier.height(10.dp))
            // CARD ALARME
            SleepControlCard(
                title = "Alarme",
                time = "05:10h",
                emojiId = R.drawable.ic_alarmclock, // Substitua pelo seu drawable
                highlight = "14horas e 30minutos",
                checked = true
            )
        }
    }
}

@Composable
fun SleepLineChart(
    progress: List<Float>,
    days: List<String>,
    highlightIndex: Int,
    comment: String,
    highlightColor: Color
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(230.dp)
        .padding(horizontal = 12.dp)
    ) {
        // Gráfico
        Canvas(modifier = Modifier.fillMaxSize()) {
            val hScale = size.width / (progress.size - 1)
            val vScale = size.height / 10f
            // Linhas horizontais
            for (i in 0..5) {
                drawLine(
                    color = Color.White.copy(0.15f),
                    start = Offset(0f, vScale * i * 2),
                    end = Offset(size.width, vScale * i * 2),
                    strokeWidth = 1.2f
                )
            }
            // Curva
            val points = progress.mapIndexed { i, f -> Offset(i * hScale, size.height - f / 10f * size.height) }
            for(i in 0 until points.size-1) {
                drawLine(
                    color = highlightColor,
                    start = points[i],
                    end = points[i+1],
                    strokeWidth = 5f
                )
            }
            // Pontos de destaque e linha vertical
            val hiPt = points[highlightIndex]
            drawCircle(color = highlightColor, radius = 10f, center = hiPt)
            drawLine(color = Color.White.copy(.3f), start = Offset(hiPt.x, 0f), end = Offset(hiPt.x, size.height), strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
        }
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF222C2D), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 3.dp)
                ) {
                    Text(comment, color = highlightColor, fontSize = 16.sp)
                }
            }
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                days.forEachIndexed { i, dia ->
                    Text(
                        text = if (i == highlightIndex) dia else dia,
                        color = if (i == highlightIndex) highlightColor else Color.White,
                        fontWeight = if (i == highlightIndex) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 17.sp
                    )
                }
            }
        }
        // Labels verticais
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp)
        ) {
            listOf("10h", "8h", "6h", "4h", "2h", "0h").forEachIndexed { i, label ->
                Text(label, color = Color.White.copy(0.60f), fontSize = 13.sp, modifier = Modifier.weight(1f))
            }
        }
        // Label valor destacado
        Text(
            "8h",
            color = highlightColor,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 30.dp, end = 8.dp)
        )
        // Dia destacado embaixo
        Text(
            days[highlightIndex],
            color = highlightColor,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 0.dp, y = 15.dp)
        )
    }
}

@Composable
fun SleepControlCard(
    title: String,
    time: String,
    emojiId: Int,
    highlight: String,
    checked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF282249)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = emojiId),
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(buildAnnotatedString {
                    append(title)
                    append(", ")
                    withStyle(SpanStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) {
                        append(time)
                    }
                }, color = Color.White, fontSize = 15.sp)
                Text(
                    buildAnnotatedString {
                        append("em ")
                        withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) { append(highlight) }
                    },
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF11EF7E))
            )
        }
    }
}
