package com.kevinduran.myshop.ui.components.shared.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TopBarTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.ExtraBold
    )
}