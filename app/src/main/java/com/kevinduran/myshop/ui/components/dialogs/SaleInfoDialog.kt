package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.components.shared.list.RoundedListContainer
import com.kevinduran.myshop.ui.components.shared.listitem.RoundedListContainerItem

@Composable
fun SaleInfoDialog(
    visible: Boolean,
    sale: Sale?,
    license: String,
    onDismiss: () -> Unit
) {
    if (visible && sale != null) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(8.dp)
            ) {
                Column {
                    Box(
                        Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .align(Alignment.CenterHorizontally)
                    ) {
                        val context = LocalContext.current
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(sale.image.generateProductImageFullPath(license))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Sale photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.failed_download),
                            placeholder = painterResource(R.drawable.downloading)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    RoundedListContainer(
                        items = listOf(
                            { RoundedListContainerItem("Local", sale.companyName) },
                            {
                                RoundedListContainerItem(
                                    "Modificado",
                                    sale.updatedAt.toDateString()
                                )
                            },
                            { RoundedListContainerItem("Modificado por", sale.updatedBy) },
                            {
                                RoundedListContainerItem(
                                    "Precio de venta",
                                    sale.salePrice.toCurrency()
                                )
                            }
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

