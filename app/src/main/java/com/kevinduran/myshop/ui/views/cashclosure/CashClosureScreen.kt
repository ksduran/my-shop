package com.kevinduran.myshop.ui.views.cashclosure

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.ui.components.dialogs.CalculatorDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CashClosureScreen(
    navController: NavController,
    employeesViewModel: EmployeesViewModel = hiltViewModel(),
    salesViewModel: SalesViewModel = hiltViewModel()
) {
    val employeesUiState by employeesViewModel.uiState.collectAsState()
    val salesUiState by salesViewModel.uiState.collectAsState()
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val employees = employeesUiState.allItems
    val coroutineScope = rememberCoroutineScope()
    val isCalculatorVisible = remember { mutableStateOf(false) }
    val totalCash = remember { mutableIntStateOf(0) }

    LoadingDialog(visible = employeesUiState.loading || salesUiState.loading)

    ErrorDialog(
        visible = employeesUiState.error.isNotEmpty(),
        content = employeesUiState.error
    ) {
        employeesViewModel.dismissError()
        isCalculatorVisible.value = false
    }

    ErrorDialog(
        visible = salesUiState.error.isNotEmpty(),
        content = salesUiState.error
    ) {
        salesViewModel.dismissError()
        isCalculatorVisible.value = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { TopBarTitle("Cierre de caja") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(employees.size, key = { it }) { index ->
                    val employee = employees[index]
                    ListItem(
                        headlineContent = { androidx.compose.material3.Text(employee.name) },
                        supportingContent = { androidx.compose.material3.Text(employee.user) },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.Person, contentDescription = null)
                            }
                        },
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                totalCash.intValue =
                                    salesViewModel.getTotalCashByUser(employee.user)
                                isCalculatorVisible.value = true
                            }
                        }
                    )
                }
            }
            CalculatorDialog(
                visible = isCalculatorVisible.value && employeesUiState.error.isEmpty() && salesUiState.error.isEmpty(),
                totalToRaised = totalCash.intValue
            ) { isCalculatorVisible.value = false }
        }
    }
}

