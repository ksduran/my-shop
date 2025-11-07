package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.SupplierData

@Composable
fun SupplierDataReturnListItem(
    modifier: Modifier = Modifier,
    data: SupplierData,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    license: String
) {

    ListItem(
        headlineContent = { Text("Devolución") },
        supportingContent = {
            Text(
                text = "Creado ${data.createdAt.toDateString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        },
        leadingContent = {
            ListItemImage(data.image.generateProductImageFullPath(license)) { }
        },
        trailingContent = {
            ListItemTrailing(
                selected = selected,
                syncStatus = data.syncStatus
            ) { }
        },
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    )

}