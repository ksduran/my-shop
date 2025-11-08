package com.kevinduran.myshop.ui.components.shared.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DuranMenuItem(
    title: String,
    imageVector: ImageVector,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    titleFontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
) {
    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    color = contentColor,
                    fontWeight = titleFontWeight
                )
            },
            trailingContent = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = contentColor
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(onClick = onClick)
        )
        HorizontalDivider(
            thickness = .5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = .2f),
            modifier = Modifier.padding(start = 16.dp)
        )
    }

}