package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TotalCapitalDialog(
    visible: Boolean,
    totalCapital: String,
    totalCapitalWithSales: String,
    totalSavings: String,
    onDismiss: () -> Unit
) {

    if(visible) {
        Box(Modifier.fillMaxSize()) {
            Dialog(onDismissRequest = onDismiss) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "Capital",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.dp)
                    )
                    Box(Modifier.fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = RoundedCornerShape(28.dp)
                        ).padding(16.dp).align(Alignment.Center)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TotalCapitalListItem("Capital Invertido", totalCapital)
                            TotalCapitalListItem("Recaudo estimado", totalCapitalWithSales)
                            TotalCapitalListItem("Utilidad estimada", totalSavings)
                            Spacer(Modifier.height(10.dp))
                            TextButton(
                                onClick = { onDismiss() },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Cerrar")
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun TotalCapitalListItem(title: String, value: String) {
    ListItem(
        modifier = Modifier.clip(MaterialTheme.shapes.large),
        headlineContent = { Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        supportingContent = { Text(value) },
        trailingContent = { Icon(Icons.Rounded.CheckCircle, null) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}