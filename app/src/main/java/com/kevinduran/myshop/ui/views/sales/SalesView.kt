package com.kevinduran.myshop.ui.views.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.components.dialogs.CalculatorDialog
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.UsersSelectorModal
import com.kevinduran.myshop.ui.components.modals.ChangeProductModal
import com.kevinduran.myshop.ui.components.modals.SalePaymentStatusModal
import com.kevinduran.myshop.ui.components.shared.buttons.DuranButton
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.SaleListItem
import com.kevinduran.myshop.ui.components.shared.loading.ScreenLoading
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.states.PreferencesState
import com.kevinduran.myshop.ui.states.SalesState
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import com.kevinduran.myshop.ui.viewmodel.PreferencesViewModel
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SalesView(
    navController: NavController,
    salesViewModel: SalesViewModel = hiltViewModel(),
    employeesViewModel: EmployeesViewModel = hiltViewModel(),
    productsViewModel: ProductsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {

    val preferencesUiState by preferencesViewModel.uiState.collectAsState()
    val uiState by salesViewModel.uiState.collectAsState()
    val sales by salesViewModel.allItems.collectAsState()
    val selectedSales = uiState.selectedItems
    val paymentStatus = uiState.paymentStatus
    val searchSales = remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    var isCalculatorDialogVisible by remember { mutableStateOf(false) }
    var isEmployeesSelectorModalVisible by remember { mutableStateOf(false) }
    var isChangeModalVisible by remember { mutableStateOf(false) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    var isSalePaymentStatusModalVisible by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LoadingDialog(visible = uiState.loading)

    LaunchedEffect(Unit) {
        salesViewModel.getAll()
    }

    UsersSelectorModal(
        visible = isEmployeesSelectorModalVisible,
        onDismiss = { isEmployeesSelectorModalVisible = false },
        employeesViewModel = employeesViewModel,
        onResetSelected = {
            scope.launch {
                isEmployeesSelectorModalVisible = false
                salesViewModel.assignUserToSales("@Admin", selectedSales)
            }
        },
        onEmployeeSelect = {
            scope.launch {
                isEmployeesSelectorModalVisible = false
                salesViewModel.assignUserToSales(it.user, selectedSales)
            }
        }
    )

    SalePaymentStatusModal(
        visible = isSalePaymentStatusModalVisible,
        salesViewModel = salesViewModel,
        onChange = { isChangeModalVisible = true }
    ) { isSalePaymentStatusModalVisible = false }

    CalculatorDialog(
        visible = isCalculatorDialogVisible,
        totalToRaised = selectedSales.sumOf { it.salePrice }
    ) { isCalculatorDialogVisible = false }

    ChangeProductModal(
        visible = isChangeModalVisible,
        sale = selectedSales.firstOrNull(),
        productsViewModel = productsViewModel,
        salesViewModel = salesViewModel,
        isLoading = isLoading
    ) { isChangeModalVisible = false }

    DeleteDialog(
        visible = isDeleteDialogVisible,
        content = "¿Realmente desea eliminar las ventas seleccionadas? Esta acción no se puede deshacer",
        onDismiss = { isDeleteDialogVisible = false }
    ) {
        scope.launch {
            isDeleteDialogVisible = false
            isLoading.value = true
            salesViewModel.deleteAll(selectedSales)
            isLoading.value = false
        }
    }

    ErrorDialog(
        visible = uiState.error.isNotEmpty(),
        content = uiState.error
    ) { salesViewModel.dismissError() }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                salesViewModel.getAll()
            }
        }
    ) {

        Box(Modifier.fillMaxSize()) {

            DataEmpty(
                visible = sales.isEmpty() && !uiState.loading,
                text = "Acá verá reflejada las ventas registradas",
                modifier = Modifier.align(Alignment.Center)
            )

            SalesList(
                selectedSales = selectedSales,
                paymentStatus = paymentStatus,
                viewModel = salesViewModel,
                sales = sales,
                searchSales = searchSales,
                navController = navController,
                uiState = uiState,
                preferencesUiState = preferencesUiState
            )

            ActionsMenu(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = selectedSales.isNotEmpty(),
                onDelete = { isDeleteDialogVisible = true },
                onAssign = { isEmployeesSelectorModalVisible = true },
                onCollect = { isSalePaymentStatusModalVisible = true },
                onCalculate = { isCalculatorDialogVisible = true },
                onSelectAll = { salesViewModel.selectAllSales() },
                preferencesUiState = preferencesUiState
            )

            ScreenLoading(visible = uiState.loading)
        }

    }
}

@Composable
private fun ActionsMenu(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDelete: () -> Unit,
    onAssign: () -> Unit,
    onCollect: () -> Unit,
    onSelectAll: () -> Unit,
    onCalculate: () -> Unit,
    preferencesUiState: PreferencesState
) {
    AnimatedVisibility(
        modifier = modifier.fillMaxWidth(),
        visible = visible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(30f))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onSelectAll) {
                    Icon(Icons.Rounded.SelectAll, "")
                }
                if (preferencesUiState.isAdmin) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Rounded.Delete, "")
                    }
                    IconButton(onClick = onAssign) {
                        Icon(Icons.Rounded.AssignmentTurnedIn, "")
                    }
                }
                IconButton(onClick = onCalculate) {
                    Icon(Icons.Rounded.Calculate, "")
                }
                TextButton(
                    onClick = onCollect,
                    border = BorderStroke(
                        .5.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                ) {
                    Text("Cobrar")
                }
            }
        }
    }
}

@Composable
private fun SalesList(
    modifier: Modifier = Modifier,
    selectedSales: List<Sale>,
    paymentStatus: String,
    viewModel: SalesViewModel,
    sales: List<Sale>,
    searchSales: MutableState<String>,
    navController: NavController,
    uiState: SalesState,
    preferencesUiState: PreferencesState
) {
    val scope = rememberCoroutineScope()
    LazyColumn(modifier.fillMaxSize()) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                SearchField(
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                    value = searchSales,
                    viewModel = viewModel
                )
                FilterButton(
                    viewModel = viewModel,
                    scope = scope
                )
            }
        }
        items(sales.size, key = { it }) { index ->
            val sale = sales[index]
            val isSelectedSale = selectedSales.contains(sale)

            SaleListItem(
                modifier = Modifier.animateItem(),
                sale = sale,
                selected = isSelectedSale,
                showState = paymentStatus == Payment.All.status,
                onClick = {
                    if (selectedSales.isEmpty()) {
                        if (!preferencesUiState.isAdmin) return@SaleListItem
                        navController.navigate(Routes.SalePreview.createRoute(sale))
                    } else {
                        viewModel.toggleSelectedItems(sale)
                    }
                },
                onLongClick = { viewModel.toggleSelectedItems(sale) },
                onImagePressed = {
                    val uri = sale.image.generateProductImageFullPath(uiState.license)
                    navController.navigate(Routes.ProductPhoto.createRoute(uri))
                },
                viewModel = viewModel
            )
        }
        if (sales.size > 1) {
            item {
                Text(
                    text = "${sales.size} ventas registradas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    viewModel: SalesViewModel
) {
    RoundedTextField(
        value = value.value,
        label = "Buscar...",
        modifier = modifier.padding(horizontal = 10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { viewModel.searchSales(value.value) }
        )
    ) { value.value = it }
}

@Composable
private fun FilterButton(
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel,
    scope: CoroutineScope
) {
    var text by remember { mutableStateOf(Payment.ByRaised.status) }
    var expanded by remember { mutableStateOf(false) }
    val with = LocalWindowInfo.current.containerSize.width

    DropdownMenu(
        modifier = modifier,
        offset = DpOffset(with.dp, 0.dp),
        expanded = expanded,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        onDismissRequest = { expanded = false }
    ) {
        Row(Modifier.padding(10.dp)) {
            Icon(Icons.Rounded.FilterList, "")
            Spacer(Modifier.width(5.dp))
            Text("Filtrar por:")
        }
        DropdownMenuItem(
            text = { Text("Todo") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Todo"
                scope.launch { viewModel.setPaymentStatus(Payment.All.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Por cobrar") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Por cobrar"
                scope.launch { viewModel.setPaymentStatus(Payment.ByRaised.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Pago por transferencia") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Transferencia"
                scope.launch { viewModel.setPaymentStatus(Payment.PaidByTransfer.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Pago en fectivo") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Efectivo"
                scope.launch { viewModel.setPaymentStatus(Payment.PaidByCash.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Pago en local") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Local"
                scope.launch { viewModel.setPaymentStatus(Payment.PaidInLocal.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Pendientes") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Pendiente"
                scope.launch { viewModel.setPaymentStatus(Payment.Pending.status) }
            }
        )
        DropdownMenuItem(
            text = { Text("Devoluciones") },
            leadingIcon = { FilterButtonIcon() },
            onClick = {
                expanded = false
                text = "Devolución"
                scope.launch { viewModel.setPaymentStatus(Payment.Return.status) }
            }
        )
    }

    DuranButton(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { expanded = true }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun FilterButtonIcon() {
    Box(
        Modifier
            .padding(5.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(10.dp)
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Rounded.FilterList,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

