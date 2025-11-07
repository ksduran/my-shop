package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.ui.viewmodel.SuppliersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliersSelectorModal(
    visible: Boolean,
    suppliersViewModel: SuppliersViewModel,
    onDismiss: () -> Unit,
    onSupplierSelect: (Supplier) -> Unit,
    titlePadding: PaddingValues = PaddingValues(vertical = 10.dp)
) {
    if (visible) {
        val uiState by suppliersViewModel.uiState.collectAsState()

        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            LazyColumn(Modifier.padding(16.dp)) {
                item {
                    Text(
                        "Seleccionar proveedor",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(titlePadding)
                    )
                }
                items(uiState.suppliers.size) { index ->
                    val item = uiState.suppliers[index]
                    ListItem(
                        headlineContent = { Text(item.name) },
                        leadingContent = { Icon(Icons.Rounded.Person, contentDescription = "Supplier") },
                        trailingContent = { Icon(Icons.Rounded.ChevronRight, contentDescription = "Select") },

                        modifier = Modifier
                            .padding(1.5.dp)
                            .clip(RoundedCornerShape(28))
                            .clickable { onSupplierSelect(item) }
                    )
                }
            }
        }
    }
}