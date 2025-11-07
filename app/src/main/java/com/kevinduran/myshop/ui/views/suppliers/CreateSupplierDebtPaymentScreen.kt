package com.kevinduran.myshop.ui.views.suppliers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSupplierDebtPaymentScreen(
    supplier: Supplier,
    navController: NavController,
    viewModel: SupplierDataViewModel = hiltViewModel()
) {
    val amount = rememberSaveable { mutableStateOf("0") }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LoadingDialog(visible = uiState.isLoading)
    ErrorDialog(visible = uiState.error != null, content = uiState.error ?: "", onDismiss = { viewModel.errorShown() })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo abono") },
                navigationIcon = { BackButton(navController) },
                actions = {
                    SaveButton {
                        scope.launch {
                            val value = amount.value.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
                            if(value > 0) {
                                viewModel.add(
                                    supplierId = supplier.uuid,
                                    productId = "",
                                    title = "Abono a deuda",
                                    quantity = 0,
                                    image = "",
                                    purchasePrice = value,
                                    type = SupplierDataType.DEBT_PAYMENT,
                                    createdAt = Instant.now().toEpochMilli()
                                )
                            }
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                RoundedTextField(
                    value = amount.value,
                    label = "Valor abonado",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                ) { amount.value = it.replace(Regex("[^0-9]"), "") }
            }
        }
    }
}