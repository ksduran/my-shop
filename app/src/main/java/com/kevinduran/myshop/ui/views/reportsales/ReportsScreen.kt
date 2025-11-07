package com.kevinduran.myshop.ui.views.reportsales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toHumanDate
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.ui.components.dialogs.EarningsDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.SaleInfoDialog
import com.kevinduran.myshop.ui.components.modals.SalesDateRangeModal
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.ListItemImage
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.ReportsScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReportsScreen(
    supplier: Supplier,
    navController: NavController,
    viewModel: ReportsScreenViewModel = hiltViewModel()
) {

    val sales = viewModel.sales.collectAsState().value
    val uiState by viewModel.uiState.collectAsState()
    val license = uiState.license
    val isDateRangeVisible = remember { mutableStateOf(false) }
    val selectedSale = remember { mutableStateOf<Sale?>(null) }
    val isEarningsDialogVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showBlur by remember {
        derivedStateOf {
            isDateRangeVisible.value || isEarningsDialogVisible.value || selectedSale.value != null
        }
    }
    val totalToPay by remember(sales) {
        derivedStateOf {
            sales.filter { it.paymentStatus.startsWith("Pago") }
                .sumOf { it.purchasePrice }
        }
    }
    val totalEarnings by remember(sales) {
        derivedStateOf {
            sales.filter { it.paymentStatus.startsWith("Pago") }
                .sumOf { it.salePrice - it.purchasePrice }
        }
    }

    LoadingDialog(visible = uiState.loading)

    ErrorDialog(
        visible = uiState.error.isNotEmpty(),
        content = uiState.error
    ) { viewModel.dismissError() }

    LaunchedEffect(supplier) {
        viewModel.load(supplier.name)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
            .graphicsLayer {
                renderEffect = if (showBlur) BlurEffect(15f, 15f) else null
            },
        topBar = {
            ReportsTopBar(
                supplier = supplier,
                navController = navController,
                isDateRangeVisible = isDateRangeVisible,
                scrollBehavior = topBarScrollBehavior
            )
        },
        bottomBar = { ReportsBottomBar(totalToPay, isEarningsDialogVisible) }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SalesDateRangeModal(
                visible = isDateRangeVisible.value,
                onDismiss = { isDateRangeVisible.value = false }
            ) {
                scope.launch {
                    isDateRangeVisible.value = false
                    viewModel.setDateRange(it)
                }
            }

            SaleInfoDialog(
                visible = selectedSale.value != null,
                sale = selectedSale.value,
                license = license,
                onDismiss = { selectedSale.value = null }
            )

            EarningsDialog(
                visible = isEarningsDialogVisible.value,
                earnings = totalEarnings,
                onDismiss = { isEarningsDialogVisible.value = false }
            )

            DataEmpty(
                visible = sales.isEmpty(),
                text = "Acá verá reflejados los reportes registrados para este proveedor",
                modifier = Modifier.align(Alignment.Center)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(sales) { sale ->
                    SaleGridItem(sale, license) {
                        selectedSale.value = sale
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportsBottomBar(totalToPay: Int, isEarningsDialogVisible: MutableState<Boolean>) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .navigationBarsPadding()
            .padding(vertical = 10.dp)
    ) {
        Column(
            Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp)
        ) {
            Text("Total a pagar")
            Text(
                totalToPay.toCurrency(),
                style = MaterialTheme.typography.headlineLarge,
                color = if (totalToPay > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                fontWeight = if (totalToPay > 0) FontWeight.Bold else FontWeight.Normal
            )
        }

        FilledIconButton(
            onClick = { isEarningsDialogVisible.value = true },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Rounded.Savings, contentDescription = "Earnings")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ReportsTopBar(
    supplier: Supplier,
    navController: NavController,
    isDateRangeVisible: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumFlexibleTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { TopBarTitle(supplier.name) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { isDateRangeVisible.value = true }) {
                Icon(Icons.Rounded.CalendarMonth, contentDescription = "Filter")
            }
        }
    )
}

@Composable
private fun SaleGridItem(sale: Sale, license: String, onClick: () -> Unit) {
    Row(
        Modifier
            .padding(2.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        ListItemImage(sale.image.generateProductImageFullPath(license)) { onClick() }
        val isPaid = sale.paymentStatus.startsWith("Pago")
        val titleColor = if (isPaid) Color.Green else MaterialTheme.colorScheme.onSurface
        val price = if (isPaid) sale.purchasePrice.toCurrency() else "$ 0,00"
        val priceColor =
            if (isPaid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline

        Column(
            Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = sale.paymentStatus,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = price,
                style = MaterialTheme.typography.bodySmall,
                color = priceColor,
                maxLines = 1,
                fontWeight = if (isPaid) FontWeight.ExtraBold else FontWeight.Normal,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sale.updatedAt.toHumanDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
