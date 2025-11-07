package com.kevinduran.myshop.ui.components.shared.dataempty

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie

@Composable
fun DataEmpty(visible: Boolean, text: String, modifier: Modifier) {

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Column {
                AnimatedLottie(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally),
                    asset = "aruba_no_data.json",
                    iterations = 1
                )
                Text(text, textAlign = TextAlign.Center)
            }
        }
    }

}