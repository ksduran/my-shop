package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie

@Composable
fun ErrorDialog(visible: Boolean, content: String, onDismiss: () -> Unit) {
    if (visible) {

        val primaryColor = Color.Red

        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                AnimatedLottie(
                    Modifier.size(200.dp),
                    asset = "404_error.json"
                )
            },
            title = {
                Text(
                    text = "Error",
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            },
            text = { Text(content) },
            confirmButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } }
        )
    }
}