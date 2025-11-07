package com.kevinduran.myshop.ui.components.shared.textfields

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MySelectableTextField(
    value: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    TextField(
        readOnly = true,
        suffix = { Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "Arrow Down") },
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = keyboardOptions,
        placeholder = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 5.dp)
            .border(width = 1.dp, shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}