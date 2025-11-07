package com.kevinduran.myshop.ui.components.shared.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedListContainer(
    modifier: Modifier = Modifier,
    items: List<@Composable () -> Unit>,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    Column(modifier = modifier) {
        val minRadius = 8.dp
        val maxRadius = 28.dp
        items.forEachIndexed { index, itemContent ->
            val shape = when {
                items.size == 1 -> RoundedCornerShape(maxRadius)
                index == 0 -> RoundedCornerShape(topStart = maxRadius, topEnd = maxRadius, bottomEnd = minRadius, bottomStart = minRadius)
                index == items.size - 1 -> RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp, topStart = minRadius, topEnd = minRadius)
                else -> RoundedCornerShape(minRadius)
            }

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(250)) + expandVertically(tween(250)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(150))
            ) {
                Surface(
                    shape = shape,
                    color = containerColor,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp, horizontal = 16.dp)
                ) {
                    Box(modifier = Modifier.padding(contentPadding)) {
                        itemContent()
                    }
                }
            }
        }
    }
}
