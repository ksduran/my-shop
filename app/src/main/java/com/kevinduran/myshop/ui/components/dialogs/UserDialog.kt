package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie
import com.kevinduran.myshop.ui.components.shared.textfields.RoundedTextField
import com.kevinduran.myshop.ui.viewmodel.AuthViewModel
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import kotlinx.coroutines.launch

@Composable
fun UserDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    employeesViewModel: EmployeesViewModel = hiltViewModel()
) {

    if (visible) {
        val authState by authViewModel.uiState.collectAsState()
        val employeesState by employeesViewModel.uiState.collectAsState()
        val employees = employeesState.allItems
        val isLoading = authState.loading || employeesState.loading
        val scope = rememberCoroutineScope()
        val controller = rememberSaveable { mutableStateOf("") }

        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceBright,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    AnimatedLottie(
                        Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally),
                        "profile_user_card.json"
                    )
                    Text(
                        text = "Usuario",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    RoundedTextField(
                        value = controller.value,
                        label = "Usuario",
                    ) { controller.value = it }

                    Spacer(Modifier.height(10.dp))

                    AnimatedVisibility(isLoading) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(vertical = 10.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    AnimatedVisibility(
                        !isLoading,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (controller.value.isBlank()) {
                                    return@OutlinedButton
                                }
                                scope.launch {
                                    authViewModel.validateAndSaveUser(
                                        controller.value,
                                        onSuccess,
                                    )
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Guardar usuario")
                        }
                    }
                }
            }
        }
    }
}