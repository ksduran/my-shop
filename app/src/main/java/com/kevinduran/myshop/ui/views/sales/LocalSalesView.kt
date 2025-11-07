package com.kevinduran.myshop.ui.views.sales

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.config.extensions.toInternalImageUri
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.SaleListItem
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel

@Composable
fun LocalSalesView(navController: NavController, viewModel: SalesViewModel = hiltViewModel()) {
    DisposableEffect(Unit) {
        val previous = viewModel.uiState.value.paymentStatus
        viewModel.setPaymentStatus(Payment.PaidInLocal.status)
        onDispose { viewModel.setPaymentStatus(previous) }
    }

    val sales = viewModel.allItems.collectAsState().value
    val selectedSales = viewModel.uiState.collectAsState().value.selectedItems

    Box(Modifier.fillMaxSize()) {
        DataEmpty(
            visible = sales.isEmpty(),
            text = "Acá verá reflejada las ventas registradas",
            modifier = Modifier.align(Alignment.Center)
        )

        LazyColumn {
            items(sales.size, key = { it }) { index ->
                val sale = sales[index]
                val isSelected = selectedSales.contains(sale)
                val context = LocalContext.current
                SaleListItem(
                    modifier = Modifier.animateItem(),
                    sale = sale,
                    selected = isSelected,
                    showState = false,
                    onClick = {
                        if (selectedSales.isEmpty()) {
                            navController.navigate(Routes.SalePreview.createRoute(sale))
                        } else {
                            viewModel.toggleSelectedItems(sale)
                        }
                    },
                    onLongClick = { viewModel.toggleSelectedItems(sale) },
                    onImagePressed = {
                        val uri = sale.image.toInternalImageUri(context).toString()
                        navController.navigate(Routes.ProductPhoto.createRoute(uri))
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

