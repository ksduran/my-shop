package com.kevinduran.myshop.ui.components.shared.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun BackButton(navController: NavController) {

    val isPressed = remember { mutableStateOf(false) }

    IconButton(onClick = {
        if(!isPressed.value) {
            isPressed.value = true
            navController.popBackStack()
        }
    }) {
        Icon(Icons.Rounded.ArrowBackIosNew, "Back")
    }

}