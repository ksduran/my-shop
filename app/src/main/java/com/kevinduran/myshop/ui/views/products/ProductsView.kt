package com.kevinduran.myshop.ui.views.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.ui.components.shared.dataempty.DataEmpty
import com.kevinduran.myshop.ui.components.shared.listitem.ProductListItem
import com.kevinduran.myshop.ui.components.shared.loading.ScreenLoading
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductsView(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {

    val products by viewModel.allProducts.collectAsState()
    val stateUI by viewModel.uiState.collectAsState()
    val license = stateUI.license
    val selectedItems = stateUI.selectedItems
    var query by remember { mutableStateOf("") }
    val filteredProducts = remember(query, products) {
        if (query.isBlank()) products else products.filter {
            it.supplierName.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.ref.contains(query, ignoreCase = true)
        }
    }
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getAll()
                isRefreshing = false
            }
        }
    ) {
        Box(Modifier.fillMaxSize()) {

            DataEmpty(
                visible = products.isEmpty() && !stateUI.loading,
                text = "Cuando registre productos nuevos los verá aquí",
                modifier = Modifier
                    .align(Alignment.Center)
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RoundedTextField(
                    value = query,
                    label = "Buscar...",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions.Default,
                    onValueChange = { query = it }
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts.size, key = { it }) { index ->
                        val item = filteredProducts[index]
                        val selected = selectedItems.contains(item)
                        val context = LocalContext.current
                        ProductListItem(
                            modifier = Modifier
                                .animateItem()
                                .aspectRatio(.7f),
                            product = item,
                            selected = selected,
                            onClick = {
                                if (selectedItems.isEmpty()) {
                                    navController.navigate(
                                        Routes.ProductPreview.createRoute(
                                            item
                                        )
                                    )
                                } else {
                                    viewModel.toggleSelectedItem(item)
                                }
                            },
                            onLongClick = { viewModel.toggleSelectedItem(item) },
                            onImagePressed = {
                                scope.launch {
                                    val image = viewModel.getFirstImage(item)
                                    navController.navigate(Routes.ProductPhoto.createRoute(image))
                                }
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }

            ScreenLoading(visible = stateUI.loading)
        }
    }

}
