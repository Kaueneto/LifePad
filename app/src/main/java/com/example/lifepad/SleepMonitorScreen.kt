package com.example.lifepad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SleepMonitorScreen(navController: NavController) {
    val selectedDay = remember { mutableStateOf(3) } // Sex selecionado (index 3)
    val days = listOf("Ter" to "11", "Qua" to "12", "Qui" to "13", "Sex" to "14", "Sáb" to "15", "Dom" to "16")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B45))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
        ) {
            // AppBar customizada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 18.dp, end = 18.dp, bottom = 12.dp),
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
                    onClick = { /* menu mais */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Mais", tint = Color.White)
                }
            }

            // Card superior roxo: Horas ideais e ilustração
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF5F2181), Color(0xFF8A39D2))), RoundedCornerShape(38.dp))
                    .shadow(2.dp, RoundedCornerShape(38.dp))
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Horas Ideais de Sono", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(color = Color(0xFFD287FF), fontWeight = FontWeight.Bold, fontSize = 22.sp)) { append("8") }
                                append("hours e ")
                                withStyle(SpanStyle(color = Color(0xFFD287FF), fontWeight = FontWeight.Bold, fontSize = 22.sp)) { append("30") }
                                append("minutos")
                            },
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C2AED)),
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Text("Saiba Mais", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    // Troque por painterResource(R.drawable.ilustra_sono) se tiver
                    Icon(
                        painter = painterResource(id = R.drawable.ilustra_sleep), // coloque sua ilustração
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(
                "Sua programação",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
            )

            // Calendário horizontal
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 18.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                days.forEachIndexed { idx, (day, num) ->
                    val selected = idx == selectedDay.value
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                if (selected) Brush.verticalGradient(listOf(Color(0xFF2962FF), Color(0xFF406AFF))) else Brush.verticalGradient(listOf(Color(0xFF18162A), Color(0xFF232137))),
                                shape = RoundedCornerShape(13.dp)
                            )
                            .width(54.dp)
                            .clickable { selectedDay.value = idx }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day, color = if (selected) Color.White else Color(0xFFB2AEC6), fontWeight = FontWeight.Bold)
                        Text(num, color = if (selected) Color.White else Color(0xFFB2AEC6), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Cards de alarmes e horários
            SleepControlCard(
                title = "Hora de Dormir",
                time = "21:00h",
                emojiId = R.drawable.icon_bed, // Substitua pelo seu drawable
                highlight = "6horas e 22minutos",
                badgeColor = Color(0xFF8A39D2),
                checked = true
            )
            Spacer(Modifier.height(8.dp))
            SleepControlCard(
                title = "Alarme",
                time = "05:10h",
                emojiId = R.drawable.icon_alaarm, // Substitua pelo seu drawable
                highlight = "14horas e 30minutos",
                badgeColor = Color(0xFFDA56CD),
                checked = true
            )
            Spacer(Modifier.height(18.dp))

            // Card barra de progresso/resultado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Brush.linearGradient(listOf(Color(0xFF5F2181), Color(0xFF8A39D2))), RoundedCornerShape(28.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text("Você terá 8 horas e 10 minutos para esta noite", color = Color.White, fontSize = 17.sp)
                    Spacer(Modifier.height(10.dp))
                    // Barra de progresso customizada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(Color(0xFF281D37), RoundedCornerShape(14.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.96f)
                                .background(
                                    Brush.horizontalGradient(listOf(Color(0xFFD287FF), Color(0xFF7DEDFF))),
                                    RoundedCornerShape(14.dp)
                                )
                        )
                        Text(
                            "96%",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp)
                        )
                    }
                }
            }
        }

        // FAB com sombra
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                onClick = { /*add nova programação*/ },
                containerColor = Brush.radialGradient(
                    colors = listOf(Color(0xFF36EA82), Color(0xFF104F1B)),
                    center = Offset(0.5f, 0.5f),
                    radius = 130f
                ).toBrushColor(),
                modifier = Modifier
                    .padding(32.dp)
                    .size(64.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
    }
}

// Card de controle de sono/alarme
@Composable
fun SleepControlCard(
    title: String,
    time: String,
    emojiId: Int,
    highlight: String,
    badgeColor: Color,
    checked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                }, color = Color(0xFFB2AEC6), fontSize = 15.sp)
                Text(
                    buildAnnotatedString {
                        append("em ")
                        withStyle(SpanStyle(color = badgeColor, fontWeight = FontWeight.Bold)) { append(highlight) }
                    },
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF11EF7E))
            )
        }
    }
}

// Utilitário para Brush como Color (FloatingActionButton)
fun Brush.toBrushColor(): Color {
    // Workaround para usar radial gradient no FAB (não nativo)
    return Color(0xFF36EA82)
}
