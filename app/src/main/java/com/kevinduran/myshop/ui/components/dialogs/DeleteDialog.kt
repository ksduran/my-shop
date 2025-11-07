package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDialog(visible: Boolean, content: String, onDismiss : () -> Unit, onDelete: () -> Unit) {

    if(visible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Borrar") },
            text = { Text(content) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "",
                    tint = Color(0xFFFF9900),
                    modifier = Modifier.size(80.dp)
                )
            },
            confirmButton = {
                TextButton(onDelete) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }

}