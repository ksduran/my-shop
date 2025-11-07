package com.kevinduran.myshop.ui.views.suppliers

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierListItem
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.SuppliersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliersScreen(navController: NavController, viewModel: SuppliersViewModel = hiltViewModel()) {

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LoadingDialog(visible = uiState.isLoading)
    ErrorDialog(
        visible = uiState.error != null,
        content = uiState.error ?: "",
        onDismiss = { viewModel.dismissError() })

    LaunchedEffect(Unit) {
        viewModel.loadSuppliers()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadSuppliers() }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            topBar = {
                TopBar(
                    navController = navController,
                    selectedItems = uiState.selectedSuppliers,
                    isDeleteDialogVisible = isDeleteDialogVisible,
                    scrollBehavior = topBarScrollBehavior
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("createSupplier") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add")
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
                    onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteAllSuppliers(uiState.selectedSuppliers)
                            isDeleteDialogVisible.value = false
                        }
                    },
                    onDismiss = { isDeleteDialogVisible.value = false }
                )

                DataEmpty(
                    visible = uiState.suppliers.isEmpty(),
                    text = "Acá verá reflejado los proveedores que registre",
                    modifier = Modifier.align(Alignment.Center)
                )
                SuppliersList(
                    uiState.suppliers,
                    viewModel,
                    uiState.selectedSuppliers,
                    navController
                )
            }
        }
    }
}

@Composable
private fun SuppliersList(
    suppliers: List<Supplier>,
    viewModel: SuppliersViewModel,
    selectedItems: List<Supplier>,
    navController: NavController
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(suppliers.size, key = { suppliers[it].uuid }) { index ->
            val item = suppliers[index]
            val selected = selectedItems.contains(item)
            SupplierListItem(
                modifier = Modifier.animateItem(),
                supplier = item,
                selected = selected,
                onClick = {
                    if (selectedItems.isEmpty()) {
                        navController.navigate(Routes.SupplierData.createRoute(item))
                    } else {
                        viewModel.toggleSelectedSuppliers(item)
                    }
                },
                onLongClick = { viewModel.toggleSelectedSuppliers(item) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(
    navController: NavController,
    selectedItems: List<Supplier>,
    isDeleteDialogVisible: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumFlexibleTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { TopBarTitle("Proveedores") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
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
