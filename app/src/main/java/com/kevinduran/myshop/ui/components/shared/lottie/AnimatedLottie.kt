package com.kevinduran.myshop.ui.components.shared.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AnimatedLottie(
    modifier: Modifier = Modifier,
    asset: String,
    iterations: Int = LottieConstants.IterateForever
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(asset)
    )

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        modifier = modifier
    )

}