package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinduran.myshop.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListItemImage(image: String, onClick: () -> Unit) {
    Box(
        Modifier
            .size(60.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(Modifier.size(60.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Product image",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClick() },
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.failed_download),
                placeholder = painterResource(R.drawable.downloading)
            )
        }
    }
}