package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.SupplierData

@Composable
fun SupplierDataDebtListItem(
    modifier: Modifier = Modifier,
    data: SupplierData,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {

    ListItem(
        headlineContent = { Text("Abono") },
        supportingContent = {
            Text(
                text = "Creado ${data.createdAt.toDateString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        },
        leadingContent = {
            Box(Modifier.size(60.dp)) {
                Icon(
                    Icons.Rounded.AttachMoney,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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