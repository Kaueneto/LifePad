package com.example.lifepad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LifePadTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}