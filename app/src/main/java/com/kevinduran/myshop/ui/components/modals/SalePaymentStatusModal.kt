package com.kevinduran.myshop.ui.components.modals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.Lucide
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalePaymentStatusModal(
    visible: Boolean,
    salesViewModel: SalesViewModel,
    onChange: () -> Unit,
    onDismiss: () -> Unit
) {

    AnimatedVisibility(
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        visible = visible
    ) {
        val salesState = salesViewModel.uiState.collectAsState().value
        val selectedSales = salesState.selectedItems

        Box(Modifier.fillMaxSize()) {
            Dialog(onDismissRequest = onDismiss) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = .5f),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Actualizar estado",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
                        )

                        PaymentButton(title = "Pago Transferencia") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.PaidByTransfer,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Pago Efectivo") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.PaidByCash,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Pago Local") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.PaidInLocal,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Devolución") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.Return,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Pendiente") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.Pending,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Por cobrar") {
                            salesViewModel.setSalePaymentStatus(
                                Payment.ByRaised,
                                selectedSales
                            )
                            onDismiss()
                        }

                        PaymentButton(title = "Cambio") {
                            onDismiss()
                            onChange()
                        }


                    }
                }
            }
        }
    }

}

@Composable
private fun PaymentButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Box(modifier.padding(vertical = 2.5.dp)) {
        ListItem(
            headlineContent = { Text(title) },
            leadingContent = { Icon(Lucide.BadgeCheck, "check") },
            modifier = Modifier
                .clickable { onClick() }
                .clip(MaterialTheme.shapes.large),
        )
    }
}
