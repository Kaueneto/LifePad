package com.example.lifepad

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifepad.NavigationBar

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B45)) // Fundo principal escuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 64.dp) // Espaço para a barra inferior (bottom nav)
        ) {
            // Cabeçalho com avatar e ícones de notificação e menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 44.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconavatar), // Avatar do usuário
                    contentDescription = "Profile Pic",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Bem Vindo!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Nicolas Magalhães", color = Color.White, fontSize = 14.sp)
                }
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color.White)
            }

            // card imc
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFF8620AC))
                        .padding(16.dp)
                ) {
                    Text("IMC (Índice de Massa Corporal)", color = Color.White)
                    Text("Você tem um peso normal.", color = Color.White)
                    Text("20,1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                    Button(
                        onClick = { /*TODO: ação ver mais*/ },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Ver Mais")
                    }
                }
            }

            // card monitor de atividades
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4539DF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Monitor de atividades", color = Color.White, modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /*TODO: ação check*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A39D2))
                    ) {
                        Text("Check")
                    }
                }
            }

            // status das atividades
            Text(
                "Status da Atividade",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // card freq. cardiaca
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF332C4B))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Frequência cardíaca", color = Color.White)
                    Text("78 BPM", color = Color(0xFFE9449B), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text("3min atrás", color = Color(0xFFA39EFF))
                    // Aqui você pode colocar um gráfico real usando Canvas ou Image
                    Spacer(Modifier.height(60.dp)) // placeholder grafico altura
                }
            }

            // cards lado a lado: Consumo de agua, Sono, Calorias
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // card Consumo de agua
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Consumo de Água", color = Color.White)
                        Text("4 Litros", color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Tempo Real", color = Color.Gray)
                        // Pode adicionar uma barra vertical aqui representando o consumo
                        Spacer(Modifier.height(60.dp))
                    }
                }

                // card Sono
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sono", color = Color.White)
                        Text("8h 20m", color = Color(0xFF57B197), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        // Aqui pode ter gráficos ou desenhos estilizados
                        Spacer(Modifier.height(60.dp))
                    }
                }

                // card Calorias
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A29))
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Calorias", color = Color.White)
                        Text("760 kCal", color = Color(0xFF8E70D2), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("230kCal left", color = Color(0xFF8E70D2))
                        Spacer(Modifier.height(60.dp))
                    }
                }
            }

            // Progresso no Treino com dropdown para filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Progresso no Treino", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                Text("Por dias da Semana", color = Color(0xFFDA56CD))




            }

            // grafico de progresso (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                TrainingBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

            }


            // utimos treinos
            Text(
                "Últimos treinos",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 200.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(3) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4539DF))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                           //depois vou colocar uma foto de exmeplo de treino aqui
                            Image(
                                painter = painterResource(id = R.drawable.iconavatar),
                                contentDescription = "Treino Ícone",
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    when (index) {
                                        0 -> "Treino de Corpo Inteiro"
                                        1 -> "Treino de Inferiores"
                                        else -> "Treino de Abdômen"
                                    },
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    when (index) {
                                        0 -> "180 Calorias queimadas | 20 minutos"
                                        1 -> "200 Calori    as queimadas | 30 minutos"
                                        else -> "180 Calorias queimadas | 20 minutos"
                                    },
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                            Icon(Icons.Default.ArrowForward, contentDescription = "Detalhes", tint = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp)) // Espaço para o bottom nav
        }

        // Barra inferior fixa de navegação
        NavigationBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun NavigationBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF232024)) // fundo do bottom nav
    ) {
        NavigationBar(modifier = Modifier.fillMaxSize()) {
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                label = { Text("Home", color = Color.White) }
            )

        }
    }
}
@Composable
fun TrainingBarChart(
    modifier: Modifier = Modifier,
    barValues: List<Int> = listOf(80, 40, 50, 80, 70, 0, 30), // barras referentes aos dias da semana
    barColors: List<Color> = listOf(
        Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8),
        Color(0xFF64B5F6), Color(0xFF81C784), Color(0xFFFFB74D), Color(0xFF90A4AE)
    ),
    //dia das semanas
    dayLabels: List<String> = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
) {
    val maxValue = (barValues.maxOrNull() ?: 1).toFloat()
    val barWidth = 24.dp

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Gráfico em barras
        Canvas(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            val space = 18.dp.toPx()
            val widthPerBar = barWidth.toPx() + space
            val startX = (size.width - (barValues.size * widthPerBar - space)) / 2f // centraliza

            barValues.forEachIndexed { idx, value ->
                val left = startX + idx * widthPerBar
                val barHeight = (value / maxValue) * size.height
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(barColors[idx % barColors.size], Color(0x80000000)) // preto opaco
                    ),
                    topLeft = androidx.compose.ui.geometry.Offset(left, size.height - barHeight),
                    size = Size(barWidth.toPx(), barHeight),
                    cornerRadius = CornerRadius(barWidth.toPx() / 2)
                )
            }
        }
        // Labels dos dias da semana, centralizados abaixo das barras
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayLabels.forEach { day ->
                Text(day, color = Color.White, fontSize = 12.sp, )
            }
        }
    }
}

