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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.AlarmClockPlus
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.CornerUpLeft
import com.composables.icons.lucide.CornerUpRight
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Nfc
import com.composables.icons.lucide.Repeat
import com.composables.icons.lucide.Store
import com.composables.icons.lucide.Trash
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.Undo
import com.composables.icons.lucide.Undo2
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.ui.components.shared.menu.DuranMenu
import com.kevinduran.myshop.ui.components.shared.menu.DuranMenuItem
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

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {

                Text(
                    text = "Nuevo estado",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                DuranMenu {
                    DuranMenuItem(
                        title = "Pago por transferencia",
                        imageVector = Lucide.Nfc
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.PaidByTransfer,
                            selectedSales
                        )
                        onDismiss()
                    }
                    DuranMenuItem(
                        title = "Pago en efectivo",
                        imageVector = Lucide.DollarSign
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.PaidByCash,
                            selectedSales
                        )
                        onDismiss()
                    }
                    DuranMenuItem(
                        title = "Pago en local",
                        imageVector = Lucide.Store
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.PaidInLocal,
                            selectedSales
                        )
                        onDismiss()
                    }
                }

                Spacer(Modifier.height(16.dp))

                DuranMenu {
                    DuranMenuItem(
                        title = "Cambio",
                        imageVector = Lucide.Repeat
                    ) {
                        onDismiss()
                        onChange()
                    }
                }

                Spacer(Modifier.height(16.dp))

                DuranMenu {
                    DuranMenuItem(
                        title = "Devolución",
                        imageVector = Lucide.Undo
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.Return,
                            selectedSales
                        )
                        onDismiss()
                    }
                    DuranMenuItem(
                        title = "Pendiente",
                        imageVector = Lucide.AlarmClockPlus
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.Pending,
                            selectedSales
                        )
                        onDismiss()
                    }
                    DuranMenuItem(
                        title = "Por cobrar",
                        imageVector = Lucide.Undo2
                    ) {
                        salesViewModel.setSalePaymentStatus(
                            Payment.ByRaised,
                            selectedSales
                        )
                        onDismiss()
                    }
                }
                Spacer(Modifier.height(30.dp))
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
