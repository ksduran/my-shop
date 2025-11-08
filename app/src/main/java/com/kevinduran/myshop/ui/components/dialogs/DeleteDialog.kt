package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun DeletePreview() {
    DeleteDialog(
        visible = true,
        content = "Realmente desea eliminar los productos? Esta accion no se puede deshacer.",
        onDismiss = {},
        onDelete = {}
    )
}

@Composable
fun DeleteDialog(visible: Boolean, content: String, onDismiss : () -> Unit, onDelete: () -> Unit) {

    if(visible) {
        Box(Modifier.fillMaxSize()) {
            Dialog(onDismissRequest = onDismiss) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(Modifier.align(Alignment.Center)) {
                        Column(
                            Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "¿Eliminar?",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = content,
                                textAlign = TextAlign.Center
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .1f))
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Eliminar",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.clickable { onDelete() }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .1f))
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Cancelar",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.clickable { onDismiss() }
                        )
                    }
                }
            }
        }
    }

}