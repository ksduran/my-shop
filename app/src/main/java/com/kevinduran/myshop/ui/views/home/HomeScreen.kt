package com.kevinduran.myshop.ui.views.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.composables.icons.lucide.ChartPie
import com.composables.icons.lucide.FileSpreadsheet
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Percent
import com.composables.icons.lucide.PiggyBank
import com.composables.icons.lucide.Shirt
import com.composables.icons.lucide.UsersRound
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.TotalCapitalDialog
import com.kevinduran.myshop.ui.components.dialogs.UsersSelectorModal
import com.kevinduran.myshop.ui.components.modals.SalesDateRangeModal
import com.kevinduran.myshop.ui.components.shared.menu.DuranMenu
import com.kevinduran.myshop.ui.components.shared.menu.DuranMenuItem
import com.kevinduran.myshop.ui.states.PreferencesState
import com.kevinduran.myshop.ui.states.SalesState
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import com.kevinduran.myshop.ui.viewmodel.PreferencesViewModel
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import com.kevinduran.myshop.ui.views.products.ProductsView
import com.kevinduran.myshop.ui.views.sales.SalesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    //ViewModels
    val salesViewModel: SalesViewModel = hiltViewModel()
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val employeesViewModel: EmployeesViewModel = hiltViewModel()
    val preferencesViewModel: PreferencesViewModel = hiltViewModel()

    //States
    val salesState by salesViewModel.uiState.collectAsState()
    val preferencesUiState by preferencesViewModel.uiState.collectAsState()

    //Other
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val productsStateUI by productsViewModel.uiState.collectAsState()

    val selectedProducts = productsStateUI.selectedItems
    val totalCapital = remember { mutableIntStateOf(0) }
    val totalCapitalWithSales = remember { mutableIntStateOf(0) }
    val totalSavings = remember { mutableIntStateOf(0) }
    val isLoading = remember { mutableStateOf(false) }
    val isSalesDateRangeModalVisible = remember { mutableStateOf(false) }
    val isDeleteProductsDialogVisible = remember { mutableStateOf(false) }
    val isTotalCapitalDialogVisible = remember { mutableStateOf(false) }
    val isEmployeesFilterVisible = remember { mutableStateOf(false) }
    val isBlurActive = remember { derivedStateOf { isTotalCapitalDialogVisible.value } }
    val startDateRange = remember { mutableLongStateOf(Instant.now().toEpochMilli()) }
    val endDateRange = remember { mutableLongStateOf(Instant.now().toEpochMilli()) }

    LoadingDialog(isLoading.value) { }

    UsersSelectorModal(
        visible = isEmployeesFilterVisible.value,
        onDismiss = { isEmployeesFilterVisible.value = false },
        employeesViewModel = employeesViewModel,
        onResetSelected = {
            coroutineScope.launch {
                isEmployeesFilterVisible.value = false
                salesViewModel.setFilterByUser("%")
            }
        },
        onEmployeeSelect = {
            coroutineScope.launch {
                isEmployeesFilterVisible.value = false
                salesViewModel.setFilterByUser(it.user)
            }
        }
    )

    DeleteDialog(
        visible = isDeleteProductsDialogVisible.value,
        content = "¿Realmente desea borrar los productos seleccionados? Esta acción no se puede deshacer",
        onDismiss = { isDeleteProductsDialogVisible.value = false }
    ) {
        coroutineScope.launch {
            isDeleteProductsDialogVisible.value = false
            isLoading.value = true
            productsViewModel.deleteAll(selectedProducts)
            isLoading.value = false
        }
    }

    SalesDateRangeModal(
        visible = isSalesDateRangeModalVisible.value,
        onDismiss = { isSalesDateRangeModalVisible.value = false }
    ) {
        isSalesDateRangeModalVisible.value = false
        salesViewModel.setDateRange(start = it.first, end = it.second)
        startDateRange.longValue = it.first
        endDateRange.longValue = it.second
    }

    ErrorDialog(
        visible = productsStateUI.error.isNotEmpty(),
        content = productsStateUI.error
    ) { productsViewModel.dismissError() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { HomeDrawerContent(navController, drawerState, coroutineScope) },
        gesturesEnabled = preferencesUiState.isAdmin,
        content = {

            Scaffold(
                modifier = Modifier
                    .graphicsLayer {
                        renderEffect = if (isBlurActive.value) BlurEffect(50f, 50f) else null
                    },
                topBar = {
                    TopBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        salesState = salesState,
                        productsViewModel = productsViewModel,
                        index = selectedIndex,
                        isDeleteProductsDialogVisible = isDeleteProductsDialogVisible,
                        isSalesDateRangeModelVisible = isSalesDateRangeModalVisible,
                        isEmployeesFilterVisible = isEmployeesFilterVisible,
                        startDateRange = startDateRange,
                        endDateRange = endDateRange,
                        preferencesUiState = preferencesUiState
                    )
                },
                bottomBar = {
                    if (preferencesUiState.isAdmin) {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            BottomBarItem(
                                modifier = Modifier.weight(1f),
                                selected = selectedIndex == 0,
                                imageVector = Lucide.House,
                                onClick = { selectedIndex = 0 }
                            )
                            MyFloatingActionButton {
                                when (selectedIndex) {
                                    0 -> navController.navigate(Routes.CreateSale.route)
                                    1 -> navController.navigate(Routes.CreateProduct.route)
                                }
                            }
                            BottomBarItem(
                                modifier = Modifier.weight(1f),
                                selected = selectedIndex == 1,
                                imageVector = Lucide.Shirt,
                                onClick = { selectedIndex = 1 }
                            )
                        }
                    }
                }
            ) { innerPadding ->

                TotalCapitalDialog(
                    visible = isTotalCapitalDialogVisible.value,
                    totalCapital = totalCapital.intValue.toCurrency(),
                    totalCapitalWithSales = totalCapitalWithSales.intValue.toCurrency(),
                    totalSavings = totalSavings.intValue.toCurrency()
                ) { isTotalCapitalDialogVisible.value = false }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    when (selectedIndex) {
                        0 -> SalesView(navController = navController)
                        1 -> ProductsView(navController = navController)
                    }

                }
            }

        }
    )
}

@Composable
private fun BottomBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    imageVector: ImageVector,
    onClick: () -> Unit

) {
    val focusedColor = MaterialTheme.colorScheme.primary
    val unfocusedColor = MaterialTheme.colorScheme.outline
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable { onClick() }
                .align(Alignment.Center)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(25.dp),
                imageVector = imageVector,
                contentDescription = "icon",
                tint = if (selected) focusedColor else unfocusedColor
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    salesState: SalesState,
    productsViewModel: ProductsViewModel,
    index: Int,
    isDeleteProductsDialogVisible: MutableState<Boolean>,
    isSalesDateRangeModelVisible: MutableState<Boolean>,
    isEmployeesFilterVisible: MutableState<Boolean>,
    startDateRange: MutableState<Long>,
    endDateRange: MutableState<Long>,
    preferencesUiState: PreferencesState
) {
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_display_name),
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (preferencesUiState.isAdmin) {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Rounded.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        },
        actions = {
            MyTopAppBarActions(
                salesState = salesState,
                productsViewModel = productsViewModel,
                index = index,
                isDeleteProductsDialogVisible = isDeleteProductsDialogVisible,
                isSalesDateRangeModelVisible = isSalesDateRangeModelVisible,
                isEmployeesFilterVisible = isEmployeesFilterVisible,
                startDateRange = startDateRange,
                endDateRange = endDateRange,
                preferencesUiState = preferencesUiState
            )
        }
    )
}

@Composable
fun MyTopAppBarActions(
    salesState: SalesState,
    productsViewModel: ProductsViewModel,
    index: Int,
    isDeleteProductsDialogVisible: MutableState<Boolean>,
    isSalesDateRangeModelVisible: MutableState<Boolean>,
    isEmployeesFilterVisible: MutableState<Boolean>,
    startDateRange: MutableState<Long>,
    endDateRange: MutableState<Long>,
    preferencesUiState: PreferencesState,
) {
    when (index) {
        0 -> {
            AnimatedContent(
                transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() },
                contentAlignment = Alignment.Center,
                targetState = salesState.selectedItems.isEmpty()
            ) {
                if (it) {
                    if (preferencesUiState.isAdmin) {
                        Row {
                            TextButton(onClick = { isEmployeesFilterVisible.value = true }) {
                                Box(
                                    Modifier
                                        .clip(MaterialTheme.shapes.extraLarge)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                        .border(
                                            .5.dp,
                                            MaterialTheme.colorScheme.outline.copy(alpha = .5f),
                                            MaterialTheme.shapes.extraLarge
                                        )
                                        .padding(5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Rounded.AlternateEmail,
                                            "User"
                                        )
                                        Text(
                                            text = if (salesState.raisedBy == "%") "" else salesState.raisedBy.replace(
                                                "@",
                                                ""
                                            ),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                            TextButton(onClick = { isSalesDateRangeModelVisible.value = true }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Rounded.CalendarMonth,
                                        contentDescription = "Calendar"
                                    )
                                    Column {
                                        Text(
                                            text = startDateRange.value.toDateString("dd MMM"),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = endDateRange.value.toDateString("dd MMM"),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = "Seleccionado: ${salesState.selectedItems.size}"
                    )
                }
            }
        }

        1 -> {
            val selectedProducts = productsViewModel.uiState.collectAsState().value.selectedItems
            AnimatedVisibility(selectedProducts.isNotEmpty()) {
                Row {
                    IconButton(onClick = { isDeleteProductsDialogVisible.value = true }) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    IconButton(onClick = { productsViewModel.selectAll() }) {
                        Icon(
                            Icons.Rounded.SelectAll,
                            contentDescription = "Select all"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Box(
        Modifier
            .background(
                MaterialTheme.colorScheme.surfaceBright,
                RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)
            )
            .width(300.dp)
            .fillMaxHeight()
    ) {
        Column(
            Modifier
                .padding(vertical = 40.dp, horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.app_display_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Gestión",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            DuranMenu {
                DuranMenuItem(
                    title = "Empleados",
                    imageVector = Lucide.UsersRound
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Routes.Employees.route)
                    }
                }
                DuranMenuItem(
                    title = "Proveedores",
                    imageVector = Lucide.UsersRound
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Routes.Suppliers.route)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Finanzas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            DuranMenu {
                DuranMenuItem(
                    title = "Cierre de caja",
                    imageVector = Lucide.ChartPie
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Routes.CashClosure.route)
                    }
                }
                DuranMenuItem(
                    title = "Ganancias",
                    imageVector = Lucide.PiggyBank
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Routes.Analytics.route)
                    }
                }
                DuranMenuItem(
                    title = "Reportes",
                    imageVector = Lucide.FileSpreadsheet
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Routes.Reports.route)
                    }
                }
            }
        }
    }
}

@Composable
fun MyFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() })
    {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
    }
}
