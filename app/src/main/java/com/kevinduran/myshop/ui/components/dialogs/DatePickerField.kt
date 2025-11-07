package com.kevinduran.myshop.ui.components.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.kevinduran.myshop.config.extensions.correctToLocalStartOfDayMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(visible: Boolean, onDismiss: () -> Unit, onConfirm: (Long?)->Unit) {

    if(visible) {

        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = { TextButton(
                onClick = { onConfirm(datePickerState.selectedDateMillis?.correctToLocalStartOfDayMillis()) },
                enabled = confirmEnabled.value
            ) { Text("Seleccionar") } },
            dismissButton = { TextButton(onClick = { onDismiss() }) { Text("Cerrar") } }
        ) {

            DatePicker(state = datePickerState)

        }
    }
}