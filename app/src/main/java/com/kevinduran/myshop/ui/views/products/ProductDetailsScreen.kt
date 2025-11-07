package com.kevinduran.myshop.ui.views.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.gson.Gson
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.model.ProductVariant
import com.kevinduran.myshop.ui.components.dialogs.DeleteDialog
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.list.RoundedListContainer
import com.kevinduran.myshop.ui.components.shared.listitem.RoundedListContainerItem
import com.kevinduran.myshop.ui.components.shared.text.TopBarTitle
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    product: Product,
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val stateUI by viewModel.uiState.collectAsState()
    val license = stateUI.license
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ErrorDialog(
        visible = stateUI.error.isNotEmpty(),
        content = stateUI.error
    ) { viewModel.dismissError() }

    LoadingDialog(stateUI.loading)

    Scaffold(
        topBar = {
            ProductDetailsTopBar(
                product,
                navController,
                topBarScrollBehavior
            ) { isDeleteDialogVisible.value = true }
        },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DeleteDialog(
                visible = isDeleteDialogVisible.value,
                content = "¿Realmente desea borrar el producto? esta acción no se puede deshacer",
                onDismiss = { isDeleteDialogVisible.value = false }
            ) {
                scope.launch {
                    isDeleteDialogVisible.value = false
                    viewModel.deleteProduct(product)
                    if (stateUI.error.isEmpty()) {
                        navController.popBackStack()
                    }
                }
            }

            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(bottom = 50.dp)
            ) {
                Header(product, license)
                ProductInfo(product)
            }
        }
    }
}

@Composable
private fun Header(product: Product, license: String) {
    val variants = Gson().fromJson(product.variants, Array<ProductVariant>::class.java).toList()
    val context = LocalContext.current

    LazyRow(
        modifier = Modifier
            .height(250.dp)
            .padding(vertical = 10.dp)
    ) {
        items(variants.size) { index ->
            val variant = variants[index]
            Box(
                Modifier
                    .size(240.dp)
                    .padding(horizontal = 5.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(variant.image.generateProductImageFullPath(license))
                        .build(),
                    contentDescription = "Product image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.downloading),
                    error = painterResource(R.drawable.failed_download)
                )
            }
        }
    }
}

@Composable
private fun ProductInfo(product: Product) {
    Column {
        Text(
            text = "Información",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
        )
        RoundedListContainer(
            items = listOf(
                {
                    RoundedListContainerItem(
                        title = product.name,
                        subtitle = "Creado el ${product.createdAt.toDateString()}"
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Precio de compra",
                        subtitle = product.purchasePrice.toCurrency()
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Precio de venta",
                        subtitle = product.salePrice.toCurrency()
                    )
                },
                {
                    RoundedListContainerItem(
                        title = "Proveedor",
                        subtitle = product.supplierName.ifBlank { "No registra" }
                    )
                },
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsTopBar(
    product: Product,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    onDelete: () -> Unit
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { TopBarTitle("Detalles de producto") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navController.popBackStack()
                navController.navigate(Routes.UpdateProduct.createRoute(product))
            }) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = {
                onDelete()
            }) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
            }
        }
    )
}
