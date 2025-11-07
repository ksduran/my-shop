package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RoundedListContainerItem(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Rounded.Info,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    leadingShapeColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {

    ListItem(
        headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Card(
                shape = MaterialShapes.Cookie4Sided.toShape(),
                colors = CardDefaults.cardColors().copy(
                    containerColor = leadingShapeColor
                )
            ) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = icon,
                    contentDescription = "icon",
                    tint = iconColor
                )
            }
        },
    )

}