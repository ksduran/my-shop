package com.kevinduran.myshop.ui.components.shared.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SaveButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Text("Guardar")
    }
}