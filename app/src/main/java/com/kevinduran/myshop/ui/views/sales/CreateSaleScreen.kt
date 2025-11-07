package com.kevinduran.myshop.ui.views.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.products.MyModalProductColorSelector
import com.kevinduran.myshop.ui.components.dialogs.products.MyProductModalSelector
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSaleScreen(
    navController: NavController,
    item: Sale? = null,
    productViewModel: ProductsViewModel = hiltViewModel(),
    salesViewModel: SalesViewModel = hiltViewModel()
) {

    val salesUiState by salesViewModel.uiState.collectAsState()
    val productsUiState by productViewModel.uiState.collectAsState()
    var isModalProductSelectorVisible by remember { mutableStateOf(false) }
    var isModalProductColorSelectorVisible by remember { mutableStateOf(false) }
    var useReplacement by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedProductColor by remember { mutableStateOf("") }
    var companyNameController by remember { mutableStateOf("") }
    var sizeController by remember { mutableStateOf("") }
    var salePriceController by remember { mutableStateOf("0") }
    var quantityController by remember { mutableStateOf("1") }
    var sizeRController by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val products = productViewModel.allProducts.collectAsState()
    val verticalScroll = rememberScrollState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isErrorDialogVisible = remember { mutableStateOf(false) }
    var errorString = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (item == null) return@LaunchedEffect
        companyNameController = item.companyName
        sizeController = item.size
        salePriceController = item.salePrice.toString()
        val product = productViewModel.getProductByUuid(item.productId)
        selectedProduct = product
        selectedProductColor = item.color
        if (item.sizeR != item.size) {
            useReplacement = true
            sizeRController = item.sizeR
        }
    }

    LoadingDialog(visible = salesUiState.loading || productsUiState.loading)

    ErrorDialog(
        visible = productsUiState.error.isNotEmpty(),
        content = productsUiState.error
    ) { productViewModel.dismissError() }

    ErrorDialog(
        visible = salesUiState.error.isNotEmpty(),
        content = salesUiState.error
    ) { salesViewModel.dismissError() }

    Scaffold(
        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        topBar = {
            MyTopAppBar(
                isLoading = productsUiState.loading || salesUiState.loading,
                item = item,
                navController = navController,
                salePriceController = salePriceController,
                salesViewModel = salesViewModel,
                companyNameController = companyNameController,
                productViewModel = productViewModel,
                isErrorDialogVisible = isErrorDialogVisible,
                selectedProductColor = selectedProductColor,
                quantityController = quantityController,
                coroutineScope = coroutineScope,
                sizeController = sizeController,
                errorString = errorString,
                selectedProduct = selectedProduct,
                useReplacement = useReplacement,
                sizeR = sizeRController,
                scrollBehavior = topBarScrollBehavior
            )
        },
    ) { innerPadding ->

        MyProductModalSelector(
            isModalProductSelectorVisible,
            products = products.value,
            onSelectProduct = {
                selectedProduct = it
                salePriceController = it.salePrice.toString()
                sizeController = ""
                sizeRController = ""
                useReplacement = false
                isModalProductSelectorVisible = false
            },
            onDismiss = { isModalProductSelectorVisible = false }
        )
        MyModalProductColorSelector(
            isModalProductColorSelectorVisible,
            selectedProduct,
            onSelect = {
                selectedProductColor = it
                sizeController = ""
                sizeRController = ""
                useReplacement = false
                isModalProductColorSelectorVisible = false
            },
            onDismiss = { isModalProductColorSelectorVisible = false }
        )
        ErrorDialog(isErrorDialogVisible.value, errorString.value) {
            isErrorDialogVisible.value = false
        }


        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .imePadding()
                    .verticalScroll(verticalScroll)
            ) {
                Header(
                    visible = true,
                    product = selectedProduct,
                    color = selectedProductColor,
                    size = sizeController,
                    viewModel = productViewModel,
                    onDelete = if (item == null) {
                        { selectedProduct = null }
                    } else null,
                    onSelectProduct = {
                        if (item == null) {
                            isModalProductSelectorVisible = true
                        }
                    },
                    onChangeColorSelect = { isModalProductColorSelectorVisible = true }
                )
                MyCompanyField(companyNameController) { companyNameController = it }
                Spacer(Modifier.width(10.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    SizeField(
                        modifier = Modifier.weight(1f),
                        value = sizeController
                    ) { sizeController = it }
                    Spacer(Modifier.width(10.dp))
                    MyQuantityField(
                        readOnly = item != null,
                        modifier = Modifier.weight(1f),
                        quantityController
                    ) {
                        val digits = it.replace(Regex("[^0-9]"), "")
                        quantityController = digits
                    }
                }
                Spacer(Modifier.width(10.dp))
                MySalePriceField(
                    modifier = Modifier.fillMaxWidth(),
                    value = salePriceController
                ) {
                    val input = it.replace(Regex("[^0-9]"), "")
                    salePriceController = input.ifBlank { "0" }
                }
                Spacer(Modifier.width(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text("¿Es un R?")
                    Spacer(Modifier.width(10.dp))
                    Switch(checked = useReplacement, onCheckedChange = { useReplacement = it })
                }
                AnimatedVisibility(useReplacement) {
                    ReplacementSizeField(value = sizeRController) { sizeRController = it }
                }
            }
        }

    }
}

@Composable
private fun MySalePriceField(
    modifier: Modifier = Modifier,
    value: String,
    onChangeValue: (String) -> Unit
) {
    RoundedTextField(
        value = value,
        modifier = modifier,
        label = "Precio de venta",
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        )
    ) { onChangeValue(it) }
}

@Composable
private fun MyQuantityField(
    readOnly: Boolean,
    modifier: Modifier,
    value: String,
    onChangeValue: (String) -> Unit
) {
    RoundedTextField(
        readOnly = readOnly,
        value = value,
        modifier = modifier,
        label = "Cantidad",
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        )
    ) { onChangeValue(it) }
}

@Composable
private fun SizeField(
    modifier: Modifier = Modifier,
    value: String,
    onChangeValue: (String) -> Unit
) {
    RoundedTextField(
        value = value,
        modifier = modifier,
        label = "Talla",
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Characters
        )
    ) { onChangeValue(it) }
}

@Composable
private fun ReplacementSizeField(value: String, onChangeValue: (String) -> Unit) {
    RoundedTextField(
        value = value,
        label = "Talla real",
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Characters
        )
    ) { onChangeValue(it) }
}

@Composable
private fun MyCompanyField(value: String, onChangeValue: (String) -> Unit) {
    RoundedTextField(
        value = value,
        label = "Nombre del local",
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Words
        )
    ) { onChangeValue(it) }
}

@Composable
private fun Header(
    visible: Boolean,
    product: Product?,
    size: String,
    color: String,
    viewModel: ProductsViewModel,
    onSelectProduct: () -> Unit,
    onChangeColorSelect: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    if (visible) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            if (product == null) MyProductSelector { onSelectProduct() } else MyProductItem(
                product = product,
                size = size,
                color = color,
                viewModel = viewModel,
                onDelete = onDelete,
                onChangeColorSelect = onChangeColorSelect
            )
        }
    }
}

@Composable
private fun MyProductItem(
    product: Product,
    size: String,
    color: String,
    viewModel: ProductsViewModel,
    onChangeColorSelect: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val license = viewModel.uiState.collectAsState().value.license
    val image = viewModel.getImageByColor(license, product, color)
    val tempImage = viewModel.getFirstImage(entity = product)
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
                supportingContent = { Text("Talla: ${size.ifBlank { "No registra" }}") },
                leadingContent = {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(image ?: tempImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Product image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.failed_download),
                            placeholder = painterResource(R.drawable.downloading)
                        )
                    }
                },
                trailingContent = {
                    onDelete?.let {
                        IconButton(onClick = { it() }) {
                            Icon(
                                Icons.Rounded.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            MyProductColorSelector { onChangeColorSelect() }
        }
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

@Composable
private fun MyProductColorSelector(onSelect: () -> Unit) {
    TextButton(onClick = { onSelect() }) {
        Text("Seleccionar color")
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MyTopAppBar(
    isLoading: Boolean,
    item: Sale?,
    navController: NavController,
    companyNameController: String,
    sizeController: String,
    isErrorDialogVisible: MutableState<Boolean>,
    errorString: MutableState<String>,
    selectedProduct: Product?,
    selectedProductColor: String,
    quantityController: String,
    coroutineScope: CoroutineScope,
    salesViewModel: SalesViewModel,
    productViewModel: ProductsViewModel,
    salePriceController: String,
    useReplacement: Boolean,
    sizeR: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumFlexibleTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = { BackButton(navController) },
        title = { TopBarTitle(if (item == null) "Nueva venta" else "Actualizar venta") },
        actions = {
            SaveButton(enabled = !isLoading) {
                if (companyNameController.isBlank() || sizeController.isBlank()) {
                    isErrorDialogVisible.value = true
                    errorString.value = "Complete los campos obligatorios para registrar esta venta"
                    return@SaveButton
                }
                if (item == null) {
                    if (selectedProduct == null) {
                        isErrorDialogVisible.value = true
                        errorString.value =
                            "El producto es obligatorio, seleccione uno e intente nuevamente"
                        return@SaveButton
                    }
                    if (selectedProductColor.isBlank()) {
                        isErrorDialogVisible.value = true
                        errorString.value =
                            "El color del producto es obligatorio, seleccione uno e intente nuevamente"
                        return@SaveButton
                    }
                    val quantity = quantityController.toIntOrNull() ?: 0
                    val salePrice = salePriceController.toIntOrNull() ?: 0
                    if (quantity <= 0) {
                        isErrorDialogVisible.value = true
                        errorString.value =
                            "La cantidad de productos a registrar debe ser mayor a 0"
                        return@SaveButton
                    }
                    coroutineScope.launch {
                        val realSize =
                            if (useReplacement) sizeR.ifBlank { sizeController } else sizeController
                        val productsToSell = List(quantity) { selectedProduct.copy() }
                        val imageFileName = productViewModel.getImageFileNameByColor(
                            selectedProduct,
                            selectedProductColor
                        )
                            ?: productViewModel.getFirstImageFileName(selectedProduct)
                        salesViewModel.addAllSales(
                            companyName = companyNameController.trim(),
                            color = selectedProductColor.trim(),
                            size = sizeController.trim(),
                            sizeR = realSize,
                            salePrice = salePrice,
                            image = imageFileName,
                            entities = productsToSell
                        )
                        withContext(Dispatchers.Main) { navController.popBackStack() }
                    }
                } else {
                    coroutineScope.launch {
                        val salePrice = salePriceController.toIntOrNull() ?: 0
                        var purchasePrice = item.purchasePrice
                        var sizeRValue = item.sizeR
                        var colorValue = item.color
                        var sizeValue = item.size

                        val realSize =
                            if (useReplacement) sizeR.ifBlank { sizeController } else sizeController

                        if (selectedProductColor != item.color || sizeController != item.size || realSize != item.sizeR) {
                            val updatedProduct = productViewModel.getProductByUuid(item.productId)
                                ?: selectedProduct!!
                            purchasePrice = updatedProduct.purchasePrice
                            sizeRValue = realSize
                            colorValue = selectedProductColor
                            sizeValue = sizeController
                        }

                        salesViewModel.updateSale(
                            companyName = companyNameController,
                            color = colorValue,
                            size = sizeValue,
                            sizeR = sizeRValue,
                            salePrice = salePrice,
                            purchasePrice = purchasePrice,
                            entity = item
                        )
                        withContext(Dispatchers.Main) { navController.popBackStack() }
                    }
                }
            }
        }
    )
}