package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingDialog(visible: Boolean, onDismiss: () -> Unit = {}) {
    if (visible) {
        Box(Modifier.fillMaxSize()) {
            Dialog(onDismissRequest = onDismiss) {
                Column(Modifier.align(Alignment.Center)) {
                    AnimatedLottie(
                        modifier = Modifier.size(200.dp),
                        asset = "sandy_loading.json"
                    )
                    //ContainedLoadingIndicator(modifier = Modifier.size(150.dp))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}