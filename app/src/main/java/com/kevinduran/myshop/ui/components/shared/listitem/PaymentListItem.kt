package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.extensions.generatePaymentsImageFullPath
import com.kevinduran.myshop.config.extensions.toHumanDate
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.ui.viewmodel.PaymentsViewModel

@Composable
fun PaymentListItem(
    modifier: Modifier = Modifier,
    payment: TransferPayment,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onImagePressed: () -> Unit,
    viewModel: PaymentsViewModel
) {

    val context = LocalContext.current
    val license by viewModel.uiState.collectAsState()
    val imageUrl = payment.imagePath.generatePaymentsImageFullPath(license.license)

    Box(
        modifier
            .fillMaxSize()
            .padding(5.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
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
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Payment image",
                            modifier = Modifier
                                .fillMaxSize()
                                .combinedClickable(
                                    onClick = onImagePressed,
                                    onLongClick = onLongClick
                                ),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.failed_download),
                            placeholder = painterResource(R.drawable.downloading)
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "Local: ${payment.storeName}",
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "Productos: ${payment.products.size}",
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = payment.createdAt.toHumanDate(),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End)
                    )

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
                imageVector = Icons.Rounded.CheckCircle,
                tint = Color.Green,
                contentDescription = "",
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}
