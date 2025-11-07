package com.kevinduran.myshop.ui.components.shared.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButtonsContainerItem(
    text: String,
    icon: ImageVector? = null,
    buttonHeight: Dp = 60.dp,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.height(buttonHeight)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
    ) {
        if (icon != null) Icon(
            icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

