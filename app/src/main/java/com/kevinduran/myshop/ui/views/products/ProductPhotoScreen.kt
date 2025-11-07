package com.kevinduran.myshop.ui.views.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R

@Composable
fun ProductPhotoScreen(
    imagePath: String,
    navController: NavController,
) {

    Scaffold(
        topBar = { TopBar(navController = navController) }
    ) { innerPadding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {

            if (imagePath.isEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = "Image not supported",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagePath)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Product image",
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(R.drawable.failed_download),
                    placeholder = painterResource(R.drawable.downloading)
                )
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Black
        )
    )
}