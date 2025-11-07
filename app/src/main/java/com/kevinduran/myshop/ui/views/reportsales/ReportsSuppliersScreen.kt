package com.kevinduran.myshop.ui.views.reportsales

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.listitem.SupplierListItem
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.SuppliersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsSuppliersScreen(
    navController: NavController,
    viewModel: SuppliersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LoadingDialog(visible = uiState.isLoading)

    ErrorDialog(
        visible = uiState.error != null,
        content = uiState.error ?: ""
    ) { viewModel.dismissError() }

    Scaffold(
        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        topBar = {
            ReportsTopBar(navController = navController, scrollBehavior = topBarScrollBehavior)
        }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            LazyColumn(Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)) {
                items(uiState.suppliers.size) { index ->
                    val item = uiState.suppliers[index]
                    SupplierListItem(
                        supplier = item,
                        selected = false,
                        onClick = {
                            navController.navigate(Routes.ReportsScreen.createRoute(item))
                        },
                        onLongClick = { },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ReportsTopBar(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    MediumFlexibleTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { TopBarTitle("Reportes") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
            }
        }
    )
}