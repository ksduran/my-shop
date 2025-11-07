package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.ui.components.shared.lottie.AnimatedLottie
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersSelectorModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    employeesViewModel: EmployeesViewModel,
    onResetSelected: () -> Unit,
    onEmployeeSelect: (Employee) -> Unit
) {

    if (visible) {

        val employees = employeesViewModel.uiState.collectAsState().value.allItems

        ModalBottomSheet(onDismissRequest = { onDismiss() }) {

            Box(
                Modifier
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(28.dp)
                    )
            ) {
                LazyColumn(Modifier.padding(16.dp)) {

                    item {
                        Box(Modifier.fillMaxWidth()) {
                            AnimatedLottie(
                                Modifier
                                    .size(200.dp)
                                    .align(Alignment.Center),
                                "profile_user_card.json"
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Seleccionar usuario",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    items(employees.size) { index ->
                        val item = employees[index]
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = { Text(item.user) },
                            leadingContent = { Icon(Icons.Rounded.VerifiedUser, "User") },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, "Select") },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            modifier = Modifier
                                .padding(1.5.dp)
                                .clip(RoundedCornerShape(28))
                                .clickable { onEmployeeSelect(item) }
                        )
                    }

                    item {
                        ListItem(
                            headlineContent = { Text("Administrador") },
                            supportingContent = { Text("Restablecer la asignación") },
                            leadingContent = { Icon(Icons.Rounded.VerifiedUser, "User") },
                            trailingContent = { Icon(Icons.Rounded.ChevronRight, "Select") },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            modifier = Modifier
                                .padding(1.5.dp)
                                .clip(RoundedCornerShape(28))
                                .clickable { onResetSelected() }
                        )
                    }

                }
            }

        }

    }

}