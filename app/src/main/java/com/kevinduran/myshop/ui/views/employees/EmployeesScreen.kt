package com.kevinduran.myshop.ui.views.employees

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.EmployeeListItem
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeesScreen(
    navController: NavController,
    viewModel: EmployeesViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    //State
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState by viewModel.uiState.collectAsState()
    val employees = uiState.allItems
    val selectedItems = uiState.selectedItems
    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    LoadingDialog(uiState.loading) { }

    ErrorDialog(
        visible = uiState.error.isNotEmpty(),
        content = uiState.error
    ) { viewModel.dismissError() }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getAll()
                isRefreshing = false
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            topBar = {
                TopBar(
                    navController = navController,
                    isDeleteDialogVisible = isDeleteDialogVisible,
                    selectedItems = selectedItems,
                    scrollBehavior = topBarScrollBehavior
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.CreateEmployee.route) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Rounded.Add, "Add")
                }
            }
        ) { innerPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                DataEmpty(
                    visible = employees.isEmpty(),
                    text = "Acá verá reflejado los empleados registrados",
                    modifier = Modifier.align(Alignment.Center)
                )

                LoadingDialog(isLoading.value) { }

                DeleteDialog(
                    visible = isDeleteDialogVisible.value,
                    content = "¿Realmente desea eliminar los empleados seleccionados? Esta acción no se puede deshacer",
                    onDismiss = { isDeleteDialogVisible.value = false }
                ) {
                    scope.launch {
                        isLoading.value = true
                        isDeleteDialogVisible.value = false
                        viewModel.deleteAll(selectedItems)
                        isLoading.value = false
                    }
                }

                EmployeesList(
                    employees = employees,
                    selectedItems = selectedItems,
                    navController = navController,
                    viewModel = viewModel
                )

            }
        }
    }
}

@Composable
fun EmployeesList(
    employees: List<Employee>,
    navController: NavController,
    selectedItems: List<Employee>,
    viewModel: EmployeesViewModel
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(employees.size, key = { it }) { index ->
            val item = employees[index]
            val selected = selectedItems.contains(item)
            EmployeeListItem(
                viewModel = viewModel,
                modifier = Modifier.animateItem(),
                employee = item,
                selected = selected,
                onClick = {
                    if (selectedItems.isNotEmpty()) {
                        viewModel.toggleSelectedItems(item)
                    } else {
                        navController.navigate(Routes.UpdateEmployee.createRoute(item))
                    }
                },
                onLongClick = { viewModel.toggleSelectedItems(item) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(
    navController: NavController,
    selectedItems: List<Employee>,
    isDeleteDialogVisible: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumFlexibleTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { TopBarTitle("Empleados") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        },
        actions = {
            AnimatedVisibility(
                visible = selectedItems.isNotEmpty(),
                enter = fadeIn(tween(300)) + scaleIn(tween(300)),
                exit = fadeOut(tween(300)) + scaleOut(tween(300))
            ) {
                IconButton(onClick = { isDeleteDialogVisible.value = true }) {
                    Icon(Icons.Rounded.Delete, "Delete")
                }
            }
        }
    )
}