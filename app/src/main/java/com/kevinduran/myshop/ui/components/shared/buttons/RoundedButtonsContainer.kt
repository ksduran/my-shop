package com.kevinduran.myshop.ui.components.shared.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButtonsContainer(
    modifier: Modifier = Modifier,
    items: List<@Composable () -> Unit>,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp)
) {
    Row(modifier = modifier) {
        val minRadius = 8.dp
        val maxRadius = 28.dp
        items.forEachIndexed { index, itemContent ->
            val shape = when {
                items.size == 1 -> RoundedCornerShape(maxRadius)
                index == 0 -> RoundedCornerShape(
                    topStart = maxRadius,
                    bottomStart = maxRadius,
                    topEnd = minRadius,
                    bottomEnd = minRadius
                )
                index == items.lastIndex -> RoundedCornerShape(
                    topEnd = maxRadius,
                    bottomEnd = maxRadius,
                    topStart = minRadius,
                    bottomStart = minRadius
                )
                else -> RoundedCornerShape(minRadius)
            }

            Surface(
                shape = shape,
                color = containerColor,
                tonalElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 1.dp, vertical = 1.dp)
            ) {
                Box(modifier = Modifier.padding(contentPadding)) {
                    itemContent()
                }
            }
        }
    }
}

