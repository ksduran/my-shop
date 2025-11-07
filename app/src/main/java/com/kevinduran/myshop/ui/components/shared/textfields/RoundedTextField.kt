package com.kevinduran.myshop.ui.components.shared.textfields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RoundedTextField(
    value: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    label: String = "",
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        readOnly = readOnly,
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = placeholder,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .border(
                .5.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = .5f),
                MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        )
    )
}