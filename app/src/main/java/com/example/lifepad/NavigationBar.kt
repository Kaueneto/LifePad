
package com.example.lifepad

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lifepad.R


@Composable
fun NavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(
        R.drawable.iconhome,
        R.drawable.iconfood,
        R.drawable.icongraficos,
        R.drawable.iconperfil
    )
    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        items.forEachIndexed { index, iconRes ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = if (selectedIndex == index) Color.White else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                },

                )
        }
    }
}