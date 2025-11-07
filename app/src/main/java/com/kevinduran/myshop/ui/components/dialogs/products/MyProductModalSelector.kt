package com.kevinduran.myshop.ui.components.dialogs.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductModalSelector(
    visible: Boolean,
    products: List<Product>,
    viewModel: ProductsViewModel = hiltViewModel(),
    onSelectProduct: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {

        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            Box(Modifier.padding(16.dp)) {
                var query by remember { mutableStateOf("") }
                val filtered = remember(query, products) {
                    if (query.isBlank()) products else products.filter {
                        it.supplierName.contains(query, ignoreCase = true) ||
                                it.name.contains(query, ignoreCase = true) ||
                                it.ref.contains(query, ignoreCase = true)
                    }
                }

                Column {
                    MyTitle()
                    RoundedTextField(
                        value = query,
                        label = "Buscar...",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions.Default,
                        onValueChange = { query = it }
                    )
                    MyList(filtered, viewModel, onSelectProduct)
                }
            }
        }
    }
}

@Composable
private fun MyList(
    products: List<Product>,
    viewModel: ProductsViewModel,
    onSelectProduct: (Product) -> Unit
) {

    Box(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(16.dp)
    ) {
        LazyColumn {
            items(products.size) { index ->
                val item = products[index]
                val image = viewModel.getFirstImage(
                    entity = item
                )

                MyListItem(item, image, onSelectProduct)
            }
        }
    }
}

@Composable
private fun MyTitle() {
    Text(
        "Seleccionar producto",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
private fun MyListItem(
    item: Product,
    image: String,
    onSelectProduct: (Product) -> Unit
) {
    ListItem(
        headlineContent = { Text(item.name) },
        trailingContent = {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = "Select"
            )
        },
        leadingContent = {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(28))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
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
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .clip(shape = RoundedCornerShape(28))
            .padding(vertical = 1.5.dp)
            .clickable { onSelectProduct(item) }
    )
}
