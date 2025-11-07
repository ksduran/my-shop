package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toHumanDate
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel

@Composable
fun SaleListItem(
    modifier: Modifier = Modifier,
    sale: Sale,
    selected: Boolean,
    showState: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onImagePressed: () -> Unit,
    viewModel: SalesViewModel
) {

    val license = viewModel.uiState.collectAsState().value.license

    ListItem(
        headlineContent = { Text(sale.companyName) },
        supportingContent = { SaleListItemSupportingContent(sale, showState) },
        leadingContent = {
            ListItemImage(sale.image.generateProductImageFullPath(license)) { onImagePressed() }
        },
        trailingContent = {
            ListItemTrailing(
                selected = selected,
                syncStatus = sale.syncStatus
            ) { Text(sale.createdAt.toHumanDate()) }
        },
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    )

}

@Composable
private fun SaleListItemSupportingContent(sale: Sale, showState: Boolean) {
    Column(Modifier.fillMaxWidth()) {
        Text("${sale.size} | ${sale.salePrice.toCurrency()}")
        AnimatedVisibility(showState) {
            Text("Estado: ${sale.paymentStatus}")
        }
    }
}



