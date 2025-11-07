package com.kevinduran.myshop.ui.components.dialogs.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalProductColorSelector(
    visible: Boolean,
    entity: Product?,
    viewModel: ProductsViewModel = hiltViewModel(),
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if(visible) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            if(entity == null) return@ModalBottomSheet
            val colors = viewModel.getColors(entity)
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Seleccionar color",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Box(Modifier
                    .background(color= MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(28.dp))
                    .padding(16.dp)
                ) {
                    LazyColumn {
                        items(colors.size) { index ->
                            val color = colors[index]
                            ListItem(
                                headlineContent = { Text(color) },
                                leadingContent = { Icon(Icons.Rounded.FormatPaint, contentDescription = "Color") },
                                trailingContent = { Icon(Icons.Rounded.ChevronRight, contentDescription = "Select") },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(28))
                                    .padding(vertical = 1.5.dp)
                                    .clickable { onSelect(color) }
                            )
                        }
                    }
                }
            }
        }
    }
}