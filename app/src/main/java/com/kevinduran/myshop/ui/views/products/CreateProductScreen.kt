package com.kevinduran.myshop.ui.views.products

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.model.ProductImage
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.SuppliersSelectorModal
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.states.ProductsState
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SuppliersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CreateProductScreen(
    navController: NavController,
    item: Product? = null
) {

    //ViewModels.
    val viewModel: ProductsViewModel = hiltViewModel()
    val suppliersViewModel: SuppliersViewModel = hiltViewModel()

    //Visibility
    val isErrorDialogVisible = remember { mutableStateOf(false) }
    var isSupplierModalVisible by remember { mutableStateOf(false) }
    val stateUI by viewModel.uiState.collectAsState()
    val showBlur by remember {
        derivedStateOf {
            stateUI.loading || stateUI.error.isNotEmpty() || isSupplierModalVisible
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var nameController by remember { mutableStateOf("") }
    var salePriceController by remember { mutableStateOf("") }
    var purchasePriceController by remember { mutableStateOf("") }
    var supplierNameController by remember { mutableStateOf("") }
    var refController by remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedImages = viewModel.selectedImages.toMutableList()
    val pickImagesLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            uris.forEach { viewModel.addImage(it, context) }
        }

    LaunchedEffect(Unit) {
        viewModel.selectedImagesClear()
        if (item != null) {
            nameController = item.name
            salePriceController = item.salePrice.toString()
            purchasePriceController = item.purchasePrice.toString()
            supplierNameController = item.supplierName
            refController = item.ref
            viewModel.getVariants(context, item)
        }
    }

    Scaffold(
        modifier = Modifier.graphicsLayer {
            renderEffect = if (showBlur) BlurEffect(15f, 15f) else null
        },
        topBar = {
            MyTopAppBar(
                item = item,
                navController = navController,
                context = context,
                coroutineScope = coroutineScope,
                salePriceController = salePriceController,
                viewModel = viewModel,
                nameController = nameController,
                refController = refController,
                supplierNameController = supplierNameController,
                purchasePriceController = purchasePriceController,
                isErrorDialogVisible = isErrorDialogVisible,
                stateUI = stateUI
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            SuppliersSelectorModal(
                visible = isSupplierModalVisible,
                suppliersViewModel = suppliersViewModel,
                onDismiss = { isSupplierModalVisible = false },
                onSupplierSelect = {
                    supplierNameController = it.name
                    isSupplierModalVisible = false
                }
            )
            LoadingDialog(stateUI.loading) { }
            ErrorDialog(stateUI.error.isNotEmpty(), stateUI.error) {
                viewModel.dismissError()
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                Column(Modifier.padding(bottom = 80.dp)) {
                    Header(
                        item,
                        viewModel,
                        selectedImages,
                        stateUI.license
                    ) { pickImagesLauncher.launch(PickVisualMediaRequest()) }
                    Spacer(Modifier.height(30.dp))
                    Form(
                        name = nameController,
                        salePrice = salePriceController,
                        purchasePrice = purchasePriceController,
                        supplierName = supplierNameController,
                        ref = refController,
                        onNameChange = { nameController = it },
                        onPurchasePriceChange = {
                            purchasePriceController = it.replace(Regex("[^0-9]"), "")
                        },
                        onRefChange = { refController = it },
                        onSalePriceChange = {
                            salePriceController = it.replace(Regex("[^0-9]"), "")
                        },
                        onSupplierNameChange = { supplierNameController = it },
                        onSupplierPressed = { isSupplierModalVisible = true }
                    )
                }
            }
        }
    }

}

@Composable
private fun Form(
    name: String,
    salePrice: String,
    purchasePrice: String,
    supplierName: String,
    ref: String,
    onNameChange: (String) -> Unit,
    onSalePriceChange: (String) -> Unit,
    onPurchasePriceChange: (String) -> Unit,
    onSupplierNameChange: (String) -> Unit,
    onRefChange: (String) -> Unit,
    onSupplierPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp)
    ) {
        RoundedTextField(
            value = name,
            label = "Nombre",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        ) { onNameChange(it) }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RoundedTextField(
                modifier = Modifier.weight(1f),
                value = purchasePrice,
                label = "P. Compra",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            ) { onPurchasePriceChange(it.replace(Regex("[^0-9]"), "")) }
            RoundedTextField(
                modifier = Modifier.weight(1f),
                value = salePrice,
                label = "P. Venta",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            ) { onSalePriceChange(it.replace(Regex("[^0-9]"), "")) }
        }
        Text(
            "Campos opcionales",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outline,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(vertical = 20.dp)
        )
        RoundedTextField(
            value = ref,
            label = "Referencia (Opcional)",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        ) { onRefChange(it) }
        ListItem(
            headlineContent = { Text(supplierName.ifBlank { "Seleccione un proveedor" }) },
            leadingContent = { Icon(Icons.Rounded.Person, contentDescription = "Supplier") },
            trailingContent = { Icon(Icons.Rounded.ChevronRight, contentDescription = "Select") },
            modifier = Modifier
                .padding(vertical = 1.5.dp)
                .clickable { onSupplierPressed() }
        )
    }
}

@Composable
private fun Header(
    product: Product?,
    viewModel: ProductsViewModel,
    selectedImages: List<ProductImage>,
    license: String,
    onAdd: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
    ) {

        val context = LocalContext.current

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(250.dp)
                .align(Alignment.Center)
        ) {
            items(selectedImages.size, key = { it }) { index ->
                val item = selectedImages[index]
                val fileName = item.image.path?.split("/")
                    ?.last()?.generateProductImageFullPath(license)
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Box {
                        if (item.image.path?.contains("cache") ?: false) {
                            Image(
                                painter = rememberAsyncImagePainter(item.image),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(shape = RoundedCornerShape(28.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(fileName)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(shape = RoundedCornerShape(28.dp)),
                                error = painterResource(R.drawable.failed_download),
                                placeholder = painterResource(R.drawable.downloading),
                            )
                        }
                        FilledIconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp),
                            onClick = { viewModel.removeImage(item.image) },
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = Color.Black.copy(alpha = .3f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    RoundedTextField(
                        modifier = Modifier.width(160.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        value = item.color,
                        label = "Color"
                    ) { viewModel.updateColor(item.image, it) }
                }
            }
            item {
                Box(
                    Modifier
                        .size(120.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clickable { onAdd() }
                        .padding(horizontal = 16.dp)

                ) {
                    Icon(
                        Icons.Rounded.AddAPhoto,
                        contentDescription = "Add new foto",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MyTopAppBar(
    item: Product?,
    navController: NavController,
    coroutineScope: CoroutineScope,
    isErrorDialogVisible: MutableState<Boolean>,
    viewModel: ProductsViewModel,
    nameController: String,
    refController: String,
    purchasePriceController: String,
    salePriceController: String,
    supplierNameController: String,
    context: Context,
    stateUI: ProductsState
) {
    TopAppBar(
        navigationIcon = { BackButton(navController) },
        title = { Text(if (item == null) "Nuevo producto" else "Actualizar producto") },
        actions = {
            SaveButton(enabled = !stateUI.loading) {
                if (nameController.isBlank()) {
                    isErrorDialogVisible.value = true
                    viewModel.setError("El nombre del producto es obligatorio")
                    return@SaveButton
                }
                if (viewModel.selectedImages.isEmpty()) {
                    isErrorDialogVisible.value = true
                    viewModel.setError("Debe seleccionar al menos una imagen")
                    return@SaveButton
                }
                if (viewModel.selectedImages.any { it.color.isBlank() }) {
                    isErrorDialogVisible.value = true
                    viewModel.setError("Todos los colores son obligatorios")
                    return@SaveButton
                }
                coroutineScope.launch {
                    val salePrice = salePriceController.toIntOrNull() ?: 0
                    val purchasePrice = purchasePriceController.toIntOrNull() ?: 0
                    if (item == null) {
                        viewModel.addProduct(
                            name = nameController,
                            ref = refController,
                            salePrice = salePrice,
                            purchasePrice = purchasePrice,
                            supplierName = supplierNameController,
                            context = context
                        )
                    } else {
                        viewModel.updateProduct(
                            name = nameController,
                            ref = refController,
                            salePrice = salePrice,
                            purchasePrice = purchasePrice,
                            supplierName = supplierNameController,
                            entity = item,
                            context = context
                        )
                    }
                    if (stateUI.error.isEmpty()) {
                        navController.popBackStack()
                    }

                }
            }
        }
    )
}
