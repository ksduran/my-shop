package com.kevinduran.myshop.ui.views.suppliers

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.ui.components.shared.list.RoundedListContainer
import com.kevinduran.myshop.ui.components.shared.listitem.RoundedListContainerItem
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel

@Composable
fun SupplierDataPreviewScreen(
    supplierData: SupplierData,
    navController: NavController,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {

    val totalQuantity = supplierData.quantity
    val productsState by productsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBar(navController) }
    ) { innerPadding ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Header(supplierData, productsState.license)
            Spacer(Modifier.height(50.dp))
            SupplierDataDetailsRecordInfo(totalQuantity, supplierData)

        }
    }

}

@Composable
fun SupplierDataDetailsRecordInfo(totalQuantity: Int, supplierData: SupplierData) {
    RoundedListContainer(
        items = listOf(
            {
                RoundedListContainerItem(
                    title = "Cantidad total",
                    subtitle = totalQuantity.toString(),
                )
            },
            {
                RoundedListContainerItem(
                    title = "Precio de compra",
                    subtitle = supplierData.purchasePrice.toCurrency(),
                )
            },
            {
                RoundedListContainerItem(
                    title = "Total invertido",
                    subtitle = (totalQuantity * supplierData.purchasePrice).toCurrency(),
                )
            },
            {
                RoundedListContainerItem(
                    title = "Modificado por",
                    subtitle = supplierData.updatedBy,
                )
            },
        )
    )
}

@Composable
private fun Header(data: SupplierData, license: String) {
    ListItem(
        headlineContent = {
            Text(
                data.title,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = { Text(data.createdAt.toDateString()) },
        leadingContent = { DataLeading(data.image, license) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(28))
    )
}

@Composable
private fun DataLeading(image: String, license: String) {
    Box(
        Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(28))
    ) {
        AnimatedContent(
            targetState = image.isNotEmpty(),
            modifier = Modifier.align(Alignment.Center)
        ) { exists ->
            if (exists) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image.generateProductImageFullPath(license))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.failed_download),
                    placeholder = painterResource(R.drawable.downloading)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Vista previa") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        }
    )
}
