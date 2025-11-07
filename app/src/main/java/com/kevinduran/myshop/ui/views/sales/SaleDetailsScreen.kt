package com.kevinduran.myshop.ui.views.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.PersonPinCircle
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.shared.list.RoundedListContainer
import com.kevinduran.myshop.ui.components.shared.listitem.ListItemImage
import com.kevinduran.myshop.ui.components.shared.listitem.RoundedListContainerItem
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreen(
    sale: Sale,
    navController: NavController,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    salesViewModel: SalesViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val changedProductState = remember { mutableStateOf<Product?>(null) }
    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(sale.changedProductId) {
        if (sale.changedProductId.isNotBlank()) {
            changedProductState.value = productsViewModel.getProductByUuid(sale.changedProductId)
        }
    }

    Scaffold(
        topBar = {
            SalePreviewTopBar(navController, sale, topBarScrollBehavior) {
                isDeleteDialogVisible.value = true
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DeleteDialog(
                visible = isDeleteDialogVisible.value,
                content = "¿Realmente desea borrar la venta? esta acción no se puede deshacer",
                onDismiss = { isDeleteDialogVisible.value = false }
            ) {
                scope.launch {
                    salesViewModel.deleteAll(listOf(sale))
                    isDeleteDialogVisible.value = false
                    navController.popBackStack()
                }
            }

            Column(
                Modifier
                    .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
                    .verticalScroll(scrollState)
            ) {
                ItemHeader(sale, changedProductState.value, productsViewModel)
                AnimatedVisibility(sale.sizeR != sale.size) {
                    ListItem(
                        headlineContent = { Text("Esta venta es un R") },
                        supportingContent = { Text("Talla real: ${sale.sizeR}") },
                        leadingContent = { Icon(Icons.Rounded.Error, "", tint = Color.Red) },
                        modifier = Modifier
                            .padding(16.dp)
                            .height(80.dp)
                            .clip(RoundedCornerShape(28.dp)),
                        colors = ListItemDefaults.colors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )
                }
                SaleInfo(sale)
                ProductInfo(sale)
                Spacer(Modifier.height(50.dp))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalePreviewTopBar(
    navController: NavController,
    sale: Sale,
    topBarScrollBehavior: TopAppBarScrollBehavior,
    onDelete: () -> Unit
) {
    MediumTopAppBar(
        scrollBehavior = topBarScrollBehavior,
        title = { Text("Detalles de venta", fontWeight = FontWeight.ExtraBold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.popBackStack()
                navController.navigate(Routes.UpdateSale.createRoute(sale))
            }) {
                Icon(Icons.Rounded.Edit, "Edit sale")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Rounded.Delete, "Delete sale")
            }
        }
    )
}

@Composable
fun SaleInfo(sale: Sale) {

    Column {
        Title("Detalles de la venta")

        RoundedListContainer(
            items = listOf(
                {
                    RoundedListContainerItem(
                        title = "Estado de venta",
                        subtitle = sale.paymentStatus,
                        icon = Icons.Rounded.QueryStats
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Precio de venta",
                        subtitle = sale.salePrice.toCurrency(),
                        icon = Icons.Rounded.Savings
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Ganancias",
                        subtitle = (sale.salePrice - sale.purchasePrice).toCurrency(),
                        icon = Icons.Rounded.Savings
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Asignado a:",
                        subtitle = sale.raisedBy,
                        icon = Icons.Rounded.PersonPinCircle
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Modificado por:",
                        subtitle = sale.updatedBy,
                        icon = Icons.Rounded.PersonPinCircle
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Modificado el:",
                        subtitle = sale.updatedAt.toDateString("EEE, d MMM HH:mm"),
                        icon = Icons.Rounded.EditCalendar
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Fecha de registro",
                        subtitle = sale.createdAt.toDateString("EEE, d MMM HH:mm"),
                        icon = Icons.Rounded.EditCalendar
                    )
                },
            )
        )
    }

}

@Composable
fun ProductInfo(sale: Sale) {

    Column {
        Title("Detalles del producto")
        RoundedListContainer(
            items = listOf(
                {
                    RoundedListContainerItem(
                        title = "talla",
                        subtitle = sale.size,
                        icon = Icons.Rounded.Numbers
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Color",
                        subtitle = sale.color,
                        icon = Icons.Rounded.AttachMoney
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Precio de compra",
                        subtitle = sale.purchasePrice.toCurrency(),
                        icon = Icons.Rounded.AttachMoney
                    )
                }
            )
        )
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
    )
}

@Composable
private fun ItemHeader(
    sale: Sale,
    changedProduct: Product?,
    productsViewModel: ProductsViewModel
) {

    val license = productsViewModel.uiState.collectAsState().value.license

    Column {
        ListItem(
            headlineContent = { Text(sale.companyName) },
            supportingContent = { Text(sale.paymentStatus) },
            leadingContent = {
                ListItemImage(sale.image.generateProductImageFullPath(license)) { }
            },
            trailingContent = { Text(sale.createdAt.toDateString("dd MMM")) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(28)),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )

        AnimatedVisibility(sale.changedProductId.isNotEmpty()) {
            Text(
                text = "↕ Cambio ↕",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedVisibility(changedProduct != null) {
            val image = productsViewModel.getImageByColor(
                license,
                changedProduct!!,
                sale.changedProductColor
            ) ?: productsViewModel.getFirstImage(entity = changedProduct)
            ListItem(
                headlineContent = { Text("Producto entregado") },
                supportingContent = { Text("${sale.changedSize} | ${sale.changedProductColor}") },
                leadingContent = { ListItemImage(image) { } },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(28)),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    }
}
