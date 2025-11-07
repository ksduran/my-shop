package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.Lucide
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.ui.viewmodel.ProductsViewModel

@Composable
fun ProductListItem(
    modifier: Modifier = Modifier,
    product: Product,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onImagePressed: () -> Unit,
    viewModel: ProductsViewModel
) {
    val context = LocalContext.current
    val firstImage = viewModel.getFirstImage(
        entity = product
    )

    Box(
        modifier
            .fillMaxSize()
            .padding(5.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                .5.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = .3f),
                MaterialTheme.shapes.extraLarge
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                hapticFeedbackEnabled = true
            )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {

            Box {
                Column(Modifier.fillMaxSize()) {
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(firstImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.downloading),
                            error = painterResource(R.drawable.failed_download),
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onImagePressed() }
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = product.salePrice.toCurrency(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    IconButton(onClick, modifier = Modifier.align(Alignment.End)) {
                        Icon(
                            imageVector = Lucide.Eye,
                            contentDescription = ""
                        )
                    }

                    Spacer(Modifier.height(5.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = selected,
            modifier = Modifier.align(Alignment.TopEnd),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Icon(
                imageVector = Lucide.BadgeCheck,
                tint = Color.Green,
                contentDescription = "",
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}