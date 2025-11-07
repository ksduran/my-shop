package com.kevinduran.myshop.ui.components.modals

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.ui.components.dialogs.products.MyModalProductColorSelector
import com.kevinduran.myshop.ui.components.dialogs.products.MyProductModalSelector
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProductModal(
    visible: Boolean,
    sale: Sale?,
    productsViewModel: ProductsViewModel,
    salesViewModel: SalesViewModel,
    isLoading: MutableState<Boolean>,
    onDismiss: () -> Unit
) {

    if (visible) {

        val scope = rememberCoroutineScope()
        val products = productsViewModel.allProducts.collectAsState()
        var product by remember { mutableStateOf<Product?>(null) }
        var size by remember { mutableStateOf("") }
        var color by remember { mutableStateOf("") }
        var isModalProductSelectorVisible by remember { mutableStateOf(false) }
        var isModalProductColorSelectorVisible by remember { mutableStateOf(false) }
        val isConfirmEnabled =
            remember { derivedStateOf { product != null && size.isNotBlank() && color.isNotBlank() } }

        ModalBottomSheet(onDismissRequest = { onDismiss() }) {

            MyProductModalSelector(
                isModalProductSelectorVisible,
                products = products.value,
                onSelectProduct = {
                    product = it
                    size = ""
                    color = ""
                    isModalProductSelectorVisible = false
                },
                onDismiss = { isModalProductSelectorVisible = false }
            )
            MyModalProductColorSelector(
                isModalProductColorSelectorVisible,
                product,
                onSelect = {
                    color = it
                    size = ""
                    isModalProductColorSelectorVisible = false
                },
                onDismiss = { isModalProductColorSelectorVisible = false }
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Header(
                    visible = true,
                    product = product,
                    size = size,
                    color = color,
                    viewModel = productsViewModel,
                    onSelectProduct = { isModalProductSelectorVisible = true },
                    onChangeColorSelect = { isModalProductColorSelectorVisible = true },
                    onDelete = {
                        product = null
                        size = ""
                        color = ""
                    }
                )

                RoundedTextField(
                    value = size,
                    label = "Talla",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Characters
                    )
                ) { size = it }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading.value = true
                            sale?.let { sl ->
                                salesViewModel.setSaleChange(
                                    sl,
                                    product?.uuid ?: "",
                                    size,
                                    color
                                )
                            }
                            onDismiss()
                            isLoading.value = false
                        }
                    },
                    enabled = isConfirmEnabled.value
                ) { Text(stringResource(R.string.generateSaleChange)) }
                Spacer(Modifier.height(20.dp))
            }
        }

    }

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
    onDelete: () -> Unit
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
    onDelete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val license = uiState.license
    val context = LocalContext.current
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
                supportingContent = { Text("Talla: ${size.ifBlank { "No registra" }}") },
                leadingContent = {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .crossfade(true)
                                .data(image ?: tempImage)
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
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete"
                        )
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
