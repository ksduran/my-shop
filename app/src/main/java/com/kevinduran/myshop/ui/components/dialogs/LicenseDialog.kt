package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LicenseDialog(
    visible: Boolean,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onFailure: () -> Unit,
    onSuccess: () -> Unit
) {

    if (visible) {

        val scope = rememberCoroutineScope()
        val controller = rememberSaveable { mutableLongStateOf(0L) }

        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(28.dp))
                    .padding(16.dp)
            ) {
                Column {
                    AnimatedLottie(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally),
                        asset = "key.json"
                    )
                    Text(
                        text = "License",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    RoundedTextField(
                        value = controller.longValue.toString(),
                        label = "Codigo",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    ) {
                        controller.longValue =
                            it.replace(Regex("[^0-9]"), "").ifBlank { "0" }.toLong()
                    }

                    Spacer(Modifier.height(10.dp))

                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    authViewModel.validateLicense(
                                        license = controller.longValue.toString(),
                                        onSuccess = onSuccess,
                                        onFailure = onFailure
                                    )
                                }
                            },
                        ) {
                            Text("Validar licencia")
                        }
                        Spacer(Modifier.height(5.dp))
                        TextButton(
                            onClick = {
                                scope.launch {
                                    authViewModel.validateLicense(
                                        license = "7001234567",
                                        onSuccess = onSuccess,
                                        onFailure = onFailure
                                    )
                                }
                            }
                        ) {
                            Text("Usar licencia pública")
                        }
                    }

                }
            }
        }
    }

}