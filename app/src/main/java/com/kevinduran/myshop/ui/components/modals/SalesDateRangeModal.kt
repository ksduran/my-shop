package com.kevinduran.myshop.ui.components.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.config.extensions.endOfDay
import com.kevinduran.myshop.config.extensions.startOfDay
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.ui.components.dialogs.DatePickerField
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesDateRangeModal(visible: Boolean, onDismiss: () -> Unit, onSearchPressed: (Pair<Long, Long>) -> Unit) {

    if(visible) {

        val isDatePickerVisible = remember { mutableStateOf(false) }
        var startDate by remember { mutableLongStateOf(Instant.now().toEpochMilli().startOfDay()) }
        var endDate by remember { mutableLongStateOf(Instant.now().toEpochMilli().endOfDay()) }
        val selectedIndex = remember { mutableIntStateOf(0) }

        ModalBottomSheet(onDismissRequest = { onDismiss() }) {

            DatePickerField(
                visible = isDatePickerVisible.value,
                onDismiss = { isDatePickerVisible.value = false }
            ) {
                isDatePickerVisible.value = false
                it?.let { if(selectedIndex.intValue == 0) startDate = it else endDate = it }
            }

            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Title()
                RangeItem(
                    title = "Desde el:",
                    index = 0,
                    date = startDate.startOfDay(),
                    isDatePickerVisible = isDatePickerVisible,
                    selectedIndex = selectedIndex
                )
                Spacer(Modifier.height(10.dp))
                RangeItem(
                    title = "Hasta el:",
                    index = 1,
                    date = endDate.endOfDay(),
                    isDatePickerVisible = isDatePickerVisible,
                    selectedIndex = selectedIndex
                )
                Spacer(Modifier.height(30.dp))
                Button(
                    onClick = { onSearchPressed(Pair(startDate.startOfDay(), endDate.endOfDay())) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) { Text("Filtrar") }
                Spacer(Modifier.height(30.dp))
            }

        }

    }

}


@Composable
fun RangeItem(title:String, index: Int, date:Long, isDatePickerVisible: MutableState<Boolean>, selectedIndex: MutableState<Int>) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        supportingContent = { Text(date.toDateString("EEEE, dd MMM yyyy")) },
        leadingContent = { Icon(Icons.Rounded.Today, "Date range") },
        trailingContent = { IconButton(onClick = {
            selectedIndex.value = index
            isDatePickerVisible.value = true
        }) {
            Icon(Icons.Rounded.EditCalendar, "Edit range", tint = MaterialTheme.colorScheme.primary)
        } },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.clip(RoundedCornerShape(28))
    )
}

@Composable
private fun Title() {
    Text(
        text = "Filtrar por fechas",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(vertical = 20.dp)
    )
}
