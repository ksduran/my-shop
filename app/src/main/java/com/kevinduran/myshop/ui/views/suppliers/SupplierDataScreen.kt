package com.kevinduran.myshop.ui.views.suppliers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardReturn
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.enums.SupplierDataType
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierDataCapitalListItem
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierDataDebtListItem
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierDataIncomeListItem
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierDataReturnListItem
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierDataWarrantyListItem
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SupplierDataViewModel
import kotlinx.coroutines.launch

@Composable
fun SupplierDataScreen(
    supplier: Supplier,
    navController: NavController,
    viewModel: SupplierDataViewModel = hiltViewModel(),
    productsViewModel: ProductsViewModel = hiltViewModel()
) {

    LaunchedEffect(supplier.uuid) {
        viewModel.loadSupplierData(supplier.uuid)
    }

    val uiState by viewModel.uiState.collectAsState()
    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val productsState by productsViewModel.uiState.collectAsState()

    LoadingDialog(visible = uiState.isLoading)
    ErrorDialog(
        visible = uiState.error != null,
        content = uiState.error ?: "",
        onDismiss = { viewModel.errorShown() })

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadSupplierData(supplier.uuid) }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    supplier = supplier,
                    navController = navController,
                    selectedItems = uiState.selectedItems,
                    isDeleteDialogVisible = isDeleteDialogVisible
                )
            },
            floatingActionButton = {
                FabMenu(
                    onIncome = {
                        navController.navigate(
                            Routes.CreateSupplierData.createRoute(
                                supplier,
                                "income"
                            )
                        )
                    },
                    onReturn = {
                        navController.navigate(
                            Routes.CreateSupplierData.createRoute(
                                supplier,
                                "return"
                            )
                        )
                    },
                    onWarranty = {
                        navController.navigate(
                            Routes.CreateSupplierData.createRoute(
                                supplier,
                                "warranty"
                            )
                        )
                    },
                    onPayment = {
                        navController.navigate(
                            Routes.CreateSupplierDebtPayment.createRoute(
                                supplier
                            )
                        )
                    }
                )
            },
            bottomBar = {
                if (supplier.debtControl == 1) {
                    SupplierDebtBottomBar(uiState.currentDebt) {
                        navController.navigate(Routes.CreateSupplierDebtPayment.createRoute(supplier))
                    }
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
                    content = "¿Realmente desea eliminar los items seleccionados? Esta acción no se puede deshacer",
                    onDismiss = { isDeleteDialogVisible.value = false },
                    onDelete = {
                        coroutineScope.launch {
                            isDeleteDialogVisible.value = false
                            viewModel.deleteAll(uiState.selectedItems, supplier.uuid)
                        }
                    }
                )

                DataEmpty(
                    visible = uiState.items.isEmpty(),
                    text = "Acá verá reflejado las compras y pagos realizados al proveedor",
                    modifier = Modifier.align(Alignment.Center)
                )

                DataList(
                    data = uiState.items,
                    selectedItems = uiState.selectedItems,
                    navController = navController,
                    viewModel = viewModel,
                    license = productsState.license
                )

            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    supplier: Supplier,
    navController: NavController,
    selectedItems: List<SupplierData>,
    isDeleteDialogVisible: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text(supplier.name) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        },
        actions = {
            if (selectedItems.isNotEmpty()) {
                IconButton(onClick = { isDeleteDialogVisible.value = true }) {
                    Icon(Icons.Rounded.Delete, "Delete")
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DataList(
    data: List<SupplierData>,
    selectedItems: List<SupplierData>,
    navController: NavController,
    viewModel: SupplierDataViewModel,
    license: String
) {
    LazyColumn(Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp)) {
        items(data.size, key = { data[it].uuid }) { index ->
            val item = data[index]
            val onClick = {
                if (selectedItems.isNotEmpty()) {
                    viewModel.toggleSelectedItems(item)
                } else {
                    navController.navigate(Routes.SupplierDataPreview.createRoute(item))
                }
            }
            val onLongClick = { viewModel.toggleSelectedItems(item) }

            when {
                item.type == SupplierDataType.INCOME.name -> {
                    SupplierDataIncomeListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick,
                        license = license
                    )
                }

                item.type == SupplierDataType.RETURN.name && item.title == "Garantía" -> {
                    SupplierDataWarrantyListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick,
                        license = license
                    )
                }

                item.type == SupplierDataType.RETURN.name -> {
                    SupplierDataReturnListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick,
                        license = license
                    )
                }

                item.type == SupplierDataType.DEBT_PAYMENT.name -> {
                    SupplierDataDebtListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }

                item.type == SupplierDataType.CAPITAL.name -> {
                    SupplierDataCapitalListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }

                else -> {
                    SupplierDataCapitalListItem(
                        modifier = Modifier.animateItem(),
                        data = item,
                        selected = selectedItems.contains(item),
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FabMenu(
    onIncome: () -> Unit,
    onReturn: () -> Unit,
    onWarranty: () -> Unit,
    onPayment: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    BackHandler(expanded) { if (expanded) expanded = false }
    Box {
        FloatingActionButtonMenu(
            expanded = expanded,
            button = {
                ToggleFloatingActionButton(
                    checked = expanded,
                    onCheckedChange = { expanded = it }
                ) {
                    val imageVector by remember {
                        derivedStateOf {
                            if (checkedProgress == 0.5f) Icons.Filled.Close else Icons.Filled.Add
                        }
                    }
                    Icon(
                        painter = rememberVectorPainter(imageVector),
                        contentDescription = null,
                        modifier = Modifier.animateIcon({ checkedProgress }),
                    )
                }
            }
        ) {

            FloatingActionButtonMenuItem(
                onClick = { expanded = false; onReturn() },
                text = { Text("Devolución") },
                icon = { Icon(Icons.AutoMirrored.Rounded.KeyboardReturn, "Return") }
            )

            FloatingActionButtonMenuItem(
                onClick = { expanded = false; onWarranty() },
                text = { Text("Garantía") },
                icon = { Icon(Icons.Rounded.Shield, "Warranty") }
            )

            FloatingActionButtonMenuItem(
                onClick = { expanded = false; onIncome() },
                text = { Text("Ingreso") },
                icon = { Icon(Icons.Rounded.Add, "Income") }
            )

            FloatingActionButtonMenuItem(
                onClick = { expanded = false; onPayment() },
                text = { Text("Abono") },
                icon = { Icon(Icons.Rounded.AttachMoney, "Payment debt") }
            )

        }
    }
}

@Composable
private fun SupplierDebtBottomBar(debt: Int, onPayment: () -> Unit) {
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
            Text("Deuda actual")
            Text(
                debt.toCurrency(),
                style = MaterialTheme.typography.headlineLarge,
                color = if (debt > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                fontWeight = if (debt > 0) FontWeight.Bold else FontWeight.Normal
            )
        }

        FilledIconButton(
            onClick = onPayment,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Rounded.AttachMoney, contentDescription = "New Payment")
        }
    }
}