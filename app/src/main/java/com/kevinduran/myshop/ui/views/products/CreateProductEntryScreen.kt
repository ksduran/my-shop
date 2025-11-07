package com.kevinduran.myshop.ui.views.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.ui.components.shared.buttons.BackButton
import com.kevinduran.myshop.ui.components.shared.buttons.SaveButton
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel

@Composable
fun CreateProductEntryScreen(
    navController: NavController,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = { TopBar(navController) }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Header(
                visible = true,
                product = null,
                color = "selectedProductColor",
                viewModel = productsViewModel,
                onDelete = { },
                onSelectProduct = { },
                onChangeColorSelect = { }
            )
        }
    }


}

@Composable
private fun Header(
    visible: Boolean,
    product: Product?,
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
    color: String,
    viewModel: ProductsViewModel,
    onChangeColorSelect: () -> Unit,
    onDelete: () -> Unit
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
                supportingContent = { Text("Color: $color") },
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
            //MyProductColorSelector { onChangeColorSelect() }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Nuevo ingreso") },
        navigationIcon = { BackButton(navController) },
        actions = { SaveButton { } }
    )
}
