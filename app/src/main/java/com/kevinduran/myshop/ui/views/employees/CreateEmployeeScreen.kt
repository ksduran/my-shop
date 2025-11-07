package com.kevinduran.myshop.ui.views.employees

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.EmployeePermissions
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.states.EmployeesState
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale.getDefault

@Composable
fun CreateEmployeeScreen(
    navController: NavController,
    employee: Employee? = null,
    viewModel: EmployeesViewModel = hiltViewModel()
) {
    val stateUI by viewModel.uiState.collectAsState()
    val name = remember { mutableStateOf("") }
    val user = remember { mutableStateOf("") }
    val confirmEnabled = remember {
        derivedStateOf { name.value.isNotBlank() && user.value.isNotBlank() }
    }
    val coroutineScope = rememberCoroutineScope()
    val permissions = remember { mutableStateOf(EmployeePermissions()) }

    LoadingDialog(
        visible = stateUI.loading
    ) { }

    ErrorDialog(
        visible = stateUI.error.isNotEmpty(),
        content = stateUI.error
    ) { viewModel.dismissError() }

    LaunchedEffect(Unit) {
        employee?.let {
            name.value = it.name
            user.value = it.user
            permissions.value = EmployeePermissions(
                chargeControl = it.chargeControl,
                createSales = it.createSales,
                createProducts = it.createProducts,
                createEmployees = it.createEmployees,
                createSuppliers = it.createSuppliers,
                updateSales = it.updateSales,
                updateProducts = it.updateProducts,
                updateEmployees = it.updateEmployees,
                updateSuppliers = it.updateSuppliers,
                deleteSales = it.deleteSales,
                deleteProducts = it.deleteProducts,
                deleteEmployees = it.deleteEmployees,
                deleteSuppliers = it.deleteSuppliers,
                fullSalesControl = it.fullSalesControl,
                fullProductsControl = it.fullProductsControl,
                fullEmployeesControl = it.fullEmployeesControl,
                fullSuppliersControl = it.fullSuppliersControl,
                seeSalesDetails = it.seeSalesDetails,
                seeProductsDetails = it.seeProductsDetails,
                seeSalesReports = it.seeSalesReports,
                seeFinanceReport = it.seeFinanceReport,
            )
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                confirmEnabled = confirmEnabled.value,
                navController = navController,
                item = employee,
                viewModel = viewModel,
                scope = coroutineScope,
                name = name,
                user = user,
                permissions = permissions,
                stateUI = stateUI
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item { Header() }
                item { NameField(name) }
                item { UserField(user) }
            }

        }
    }

}

@Composable
private fun Header() {
    Box(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = "Person",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun NameField(name: MutableState<String>) {
    RoundedTextField(
        value = name.value,
        label = "Nombre del empleado",
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    ) { name.value = it }
}

@Composable
private fun UserField(user: MutableState<String>) {
    RoundedTextField(
        value = user.value,
        label = "Usuario",
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    ) { user.value = it.trim().lowercase(getDefault()) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    confirmEnabled: Boolean,
    navController: NavController,
    item: Employee?,
    viewModel: EmployeesViewModel,
    scope: CoroutineScope,
    name: MutableState<String>,
    user: MutableState<String>,
    permissions: MutableState<EmployeePermissions>,
    stateUI: EmployeesState
) {
    TopAppBar(
        title = { Text(if (item == null) "Nuevo empleado" else "Actualizar empleado") },
        navigationIcon = { BackButton(navController) },
        actions = {
            SaveButton(enabled = confirmEnabled) {
                scope.launch {
                    if (item == null) {
                        viewModel.addEmployee(name.value, user.value, permissions.value)
                    } else {
                        viewModel.update(name.value, user.value, permissions.value, item)
                    }
                    if (stateUI.error.isEmpty()) {
                        withContext(Dispatchers.Main) { navController.popBackStack() }
                    }
                }
            }
        }
    )
}