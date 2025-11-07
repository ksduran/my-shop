package com.kevinduran.myshop.ui.views.suppliers

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.kevinduran.myshop.config.enums.SupplierDataType
import com.kevinduran.myshop.config.extensions.hasStockControl
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.ui.components.dialogs.DatePickerField
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.products.MyModalProductColorSelector
import com.kevinduran.myshop.ui.components.dialogs.products.MyProductModalSelector
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SupplierDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun CreateSuppliersDataIncomeScreen(
    supplier: Supplier,
    navController: NavController,
    type: String = "income",
    productsViewModel: ProductsViewModel = hiltViewModel(),
    supplierDataViewModel: SupplierDataViewModel = hiltViewModel()
) {

    val products by productsViewModel.allProducts.collectAsState()
    val selectedProduct = remember { mutableStateOf<Product?>(null) }
    val isProductsSelectorVisible = remember { mutableStateOf(false) }
    val isModalProductColorSelectorVisible = remember { mutableStateOf(false) }
    val isDatePickerVisible = remember { mutableStateOf(false) }
    val selectedDateMillis = remember { mutableLongStateOf(Instant.now().toEpochMilli()) }
    val selectedDate =
        remember { mutableStateOf(selectedDateMillis.longValue.toDateString("EEE, dd MMM ' | ' HH:mm")) }
    val selectedColor = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val purchasePrice = remember { mutableStateOf("0") }
    val quantity = remember { mutableStateOf("0") }
    val trackInventory = remember(selectedProduct.value) {
        selectedProduct.value?.hasStockControl() ?: false
    }

    val supplierDataUiState by supplierDataViewModel.uiState.collectAsState()
    val productsUiState by productsViewModel.uiState.collectAsState()

    LaunchedEffect(selectedProduct.value) {
        selectedProduct.value?.let { product ->
            purchasePrice.value = product.purchasePrice.toString()
        }
    }

    val showSizes = trackInventory || type != "income"

    LoadingDialog(visible = supplierDataUiState.isLoading || productsUiState.loading)
    ErrorDialog(
        visible = supplierDataUiState.error != null,
        content = supplierDataUiState.error ?: "",
        onDismiss = { supplierDataViewModel.errorShown() })
    ErrorDialog(
        visible = productsUiState.error.isNotEmpty(),
        content = productsUiState.error,
        onDismiss = { productsViewModel.dismissError() })

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                purchasePrice = purchasePrice.value.toIntOrNull() ?: 0,
                color = selectedColor.value,
                supplierDataViewModel = supplierDataViewModel,
                productsViewModel = productsViewModel,
                supplier = supplier,
                scope = coroutineScope,
                quantity = quantity.value.toIntOrNull() ?: 0,
                product = selectedProduct.value,
                type = type,
                trackInventory = trackInventory,
                showSizes = showSizes,
                selectedDateMillis = selectedDateMillis.longValue
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            DatePickerField(
                visible = isDatePickerVisible.value,
                onDismiss = { isDatePickerVisible.value = false },
                onConfirm = {
                    isDatePickerVisible.value = false
                    it?.let { millis ->
                        selectedDateMillis.longValue = millis
                        selectedDate.value = millis.toDateString("EEE, dd MMM ' | ' HH:mm")
                    }
                }
            )

            MyProductModalSelector(
                visible = isProductsSelectorVisible.value,
                products = products,
                onSelectProduct = { product ->
                    isProductsSelectorVisible.value = false
                    selectedProduct.value = product
                    productsViewModel.getVariants(context.applicationContext, product)
                    purchasePrice.value = product.purchasePrice.toString()

                    val firstColor = productsViewModel.getColors(product).firstOrNull() ?: ""
                    selectedColor.value = firstColor
                },
                onDismiss = { isProductsSelectorVisible.value = false }
            )

            MyModalProductColorSelector(
                isModalProductColorSelectorVisible.value,
                selectedProduct.value,
                onSelect = { color ->
                    selectedColor.value = color
                    isModalProductColorSelectorVisible.value = false
                },
                onDismiss = { isModalProductColorSelectorVisible.value = false }
            )

            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
            ) {
                Header(
                    product = selectedProduct.value,
                    color = selectedColor.value,
                    viewModel = productsViewModel,
                    onSelectProduct = { isProductsSelectorVisible.value = true },
                    onChangeColorSelect = { isModalProductColorSelectorVisible.value = true },
                    onDelete = { selectedProduct.value = null }
                )
                Spacer(Modifier.height(10.dp))
                PurchasePriceField(purchasePrice)
                QuantityField(quantity)
                DateField(isDatePickerVisible, selectedDate.value)
            }
        }
    }

}

@Composable
fun DateField(isDatePickerVisible: MutableState<Boolean>, selectedDate: String) {
    RoundedTextField(
        value = selectedDate,
        readOnly = true,
        label = "Fecha de ingreso",
        trailingIcon = {
            IconButton(onClick = { isDatePickerVisible.value = true }) {
                Icon(Icons.Rounded.DateRange, "Selected date")
            }
        }
    ) { }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    product: Product?,
    quantity: Int,
    productsViewModel: ProductsViewModel,
    supplierDataViewModel: SupplierDataViewModel,
    color: String,
    purchasePrice: Int,
    scope: CoroutineScope,
    supplier: Supplier,
    type: String,
    trackInventory: Boolean,
    showSizes: Boolean,
    selectedDateMillis: Long
) {
    val license = productsViewModel.uiState.collectAsState().value.license

    TopAppBar(
        title = {
            val title = when (type) {
                "return" -> "Nueva devolución"
                "warranty" -> "Nueva garantía"
                else -> "Nuevo ingreso"
            }
            Text(title)
        },
        navigationIcon = { BackButton(navController) },
        actions = {
            SaveButton {
                if (color.isBlank()) {
                    return@SaveButton
                }
                if (quantity <= 0) {
                    return@SaveButton
                }
                scope.launch {
                    val title = when (type) {
                        "return" -> "Devolución de mercancía"
                        "warranty" -> "Garantía"
                        else -> "Ingreso de producto"
                    }
                    val dataType = when (type) {
                        "return", "warranty" -> SupplierDataType.RETURN
                        else -> SupplierDataType.INCOME
                    }
                    val success = supplierDataViewModel.add(
                        supplierId = supplier.uuid,
                        productId = product!!.uuid,
                        title = title,
                        quantity = quantity,
                        image = productsViewModel.getImageByColor(license, product, color)
                            .toString(),
                        purchasePrice = purchasePrice,
                        createdAt = selectedDateMillis,
                        type = dataType
                    )
                    if (success) {
                        navController.popBackStack()
                    }
                }
            }
        }
    )
}

@Composable
private fun QuantityField(quantity: MutableState<String>) {
    RoundedTextField(
        value = quantity.value,
        label = "Cantidad",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    ) { quantity.value = it.replace(Regex("[^0-9]"), "").ifBlank { "0" } }
}

@Composable
private fun PurchasePriceField(purchasePrice: MutableState<String>) {
    RoundedTextField(
        value = purchasePrice.value,
        label = "Precio de compra"
    ) { purchasePrice.value = it.replace(Regex("[^0-9]"), "").ifBlank { "0" } }
}

@Composable
private fun Header(
    product: Product?,
    color: String,
    viewModel: ProductsViewModel,
    onSelectProduct: () -> Unit,
    onChangeColorSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        if (product == null) MyProductSelector { onSelectProduct() } else MyProductItem(
            product = product,
            color = color,
            viewModel = viewModel,
            onDelete = onDelete,
            onChangeColorSelect = onChangeColorSelect
        )
    }
}

@Composable
private fun MyProductItem(
    product: Product,
    color: String,
    viewModel: ProductsViewModel,
    onChangeColorSelect: () -> Unit,
    onDelete: () -> Unit
) {
    val license = viewModel.uiState.collectAsState().value.license
    val image = viewModel.getImageByColor(license, product, color)
    val tempImage = viewModel.getFirstImage(product)
    Column {
        Box(
            Modifier.background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(28)
            )
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        "$color | ${product.name}",
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingContent = {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Log.d("TEST", image?.toString() ?: "Image is null")
                        Log.d("TEST", tempImage.toString())
                        val displayImage = image ?: tempImage
                        Image(
                            painter = rememberAsyncImagePainter(displayImage),
                            contentDescription = "Product image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                trailingContent = {
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        }
        MyProductColorSelector { onChangeColorSelect() }
    }
}

@Composable
private fun MyProductColorSelector(onSelect: () -> Unit) {
    TextButton(onClick = { onSelect() }) {
        Text("Seleccionar color")
    }
}

@Composable
private fun MyProductSelector(onSelectProduct: () -> Unit) {
    ListItem(
        headlineContent = { Text("Seleccionar producto") },
        trailingContent = { Icon(Icons.Rounded.ChevronRight, contentDescription = "Select") },
        leadingContent = {
            Box(
                Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add product",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10))
            .clickable { onSelectProduct() }
    )
}