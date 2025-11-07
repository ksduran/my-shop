package com.kevinduran.myshop.ui.views.suppliers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.enums.SupplierDataType
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.SupplierDataViewModel
import com.kevinduran.myshop.ui.viewmodel.SuppliersViewModel
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun CreateSupplierScreen(
    navController: NavController,
    supplier: Supplier? = null,
    viewModel: SuppliersViewModel = hiltViewModel(),
    supplierDataViewModel: SupplierDataViewModel = hiltViewModel()
) {

    val name = rememberSaveable { mutableStateOf(supplier?.name ?: "") }
    val debt = rememberSaveable { mutableStateOf("0") }
    val debtControl = rememberSaveable { mutableStateOf(supplier?.debtControl == 1) }
    val uiState by viewModel.uiState.collectAsState()

    LoadingDialog(visible = uiState.isLoading)
    ErrorDialog(
        visible = uiState.error != null,
        content = uiState.error ?: "",
        onDismiss = { viewModel.dismissError() })

    Scaffold(
        topBar = {
            TopBar(
                supplier = supplier,
                navController = navController,
                name = name.value,
                debt = debt.value,
                debtControl = debtControl.value,
                viewModel = viewModel,
                supplierDataViewModel = supplierDataViewModel
            )
        }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                TopIcon(Modifier.align(Alignment.CenterHorizontally))
                NameField(name)
                if (supplier == null) {
                    DebtControlSwitch(debtControl)
                    if (debtControl.value) {
                        DebtField(debt)
                    }
                }
            }
        }
    }
}

@Composable
fun TopIcon(modifier: Modifier) {
    Icon(
        imageVector = Icons.Rounded.Person,
        contentDescription = "Person",
        modifier = modifier.size(100.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    supplier: Supplier?,
    navController: NavController,
    name: String,
    debt: String,
    debtControl: Boolean,
    viewModel: SuppliersViewModel,
    supplierDataViewModel: SupplierDataViewModel
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(if (supplier == null) "Nuevo proveedor" else "Actualizar proveedor") },
        navigationIcon = { BackButton(navController) },
        actions = {
            SaveButton {
                scope.launch {
                    if (supplier == null) {
                        val newSupplier = viewModel.addSupplier(name, if (debtControl) 1 else 0)
                        if (newSupplier != null) {
                            val amount = debt.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
                            if (debtControl && amount > 0) {
                                supplierDataViewModel.add(
                                    supplierId = newSupplier.uuid,
                                    productId = "",
                                    title = "Deuda inicial",
                                    quantity = 0,
                                    image = "",
                                    purchasePrice = amount,
                                    type = SupplierDataType.CAPITAL,
                                    createdAt = Instant.now().toEpochMilli()
                                )
                            }
                            navController.popBackStack()
                        }
                    } else {
                        viewModel.updateSupplier(name, if (debtControl) 1 else 0, supplier)
                        navController.popBackStack()
                    }
                }
            }
        }
    )
}

@Composable
private fun NameField(value: MutableState<String>) {
    RoundedTextField(
        value = value.value,
        label = "Nombre",
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    ) { value.value = it }
}

@Composable
private fun DebtField(value: MutableState<String>) {
    RoundedTextField(
        value = value.value,
        label = "Deuda inicial (Opcional)",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    ) { value.value = it.replace(Regex("[^0-9]"), "") }
}

@Composable
private fun DebtControlSwitch(value: MutableState<Boolean>) {
    ListItem(
        headlineContent = { Text("¿Controlar deuda?") },
        trailingContent = {
            Switch(checked = value.value, onCheckedChange = { value.value = it })
        },
        modifier = Modifier
            .clickable { value.value = !value.value }
            .padding(vertical = 10.dp)
    )
}