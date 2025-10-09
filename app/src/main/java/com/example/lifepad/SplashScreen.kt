import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val backgroundColor = remember { Animatable(Color.Black) }
    //var pro fade
    var fadeOut by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (fadeOut) 0f else 1f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        //usando o alpha pra fazer um fade
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value)
            .graphicsLayer { this.alpha = alpha },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "LifePad",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )

    }

    LaunchedEffect(Unit) {

        kotlinx.coroutines.delay(900)


        backgroundColor.animateTo(
            targetValue = Color(0xFF020A15),
            animationSpec = tween(durationMillis = 500)
        )
        backgroundColor.animateTo(
            targetValue = Color(0xFF010423),
            animationSpec = tween(durationMillis = 450)
        )
        backgroundColor.animateTo(
            targetValue = Color(0xFF0A1148),
            animationSpec = tween(durationMillis = 1500)
        )

        //espera antes do fade
        kotlinx.coroutines.delay(100)


        fadeOut = true


        kotlinx.coroutines.delay(500)
        onSplashComplete()
    }
}
