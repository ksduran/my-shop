package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListItemTrailing(selected: Boolean, syncStatus: Int, content: @Composable () -> Unit) {
    Box(Modifier.size(60.dp)) {
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(tween(300)) + scaleIn(tween(300)),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = "Selected",
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center)
                )
            }
        }

        AnimatedVisibility(
            visible = !selected,
            enter = fadeIn(tween(300)) + scaleIn(tween(300)),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)),
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}