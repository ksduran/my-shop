package com.kevinduran.myshop.ui.components.shared.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DuranMenu(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    items: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column { items() }
    }
}