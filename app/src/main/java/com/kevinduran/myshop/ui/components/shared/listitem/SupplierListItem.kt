package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.FolderInput
import com.composables.icons.lucide.Lucide
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.Supplier

@Composable
fun SupplierListItem(
    modifier: Modifier = Modifier,
    supplier: Supplier,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(supplier.name) },
        supportingContent = {
            Text(
                text = "Creado el ${supplier.updatedAt.toDateString("d MMMM yyyy")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        },
        leadingContent = {
            Box(Modifier.size(50.dp)) {
                Icon(
                    imageVector = Lucide.FolderInput,
                    contentDescription = "Folder shared",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingContent = {
            ListItemTrailing(
                selected = selected,
                syncStatus = supplier.syncStatus,
            ) { }
        },
        modifier = modifier
            .padding(vertical = 1.5.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
    )
}

