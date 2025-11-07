package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kevinduran.myshop.config.extensions.toCurrency

@Composable
fun CalculatorDialog(visible: Boolean, totalToRaised: Int, onDismiss:() -> Unit) {

    if(visible) {

        Dialog(onDismissRequest = { onDismiss() }) {

            Box(Modifier.background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(28.dp))) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    CalculatorLottieAnimation()

                    Text("Total a cobrar", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)

                    Text(
                        totalToRaised.toCurrency(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )

                }
            }

        }

    }

}

@Composable
private fun CalculatorLottieAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("calculator.json")
    )

    LottieAnimation(
        composition = composition,
        modifier = Modifier.size(250.dp),
        iterations = LottieConstants.IterateForever
    )
}
