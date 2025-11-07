package com.kevinduran.myshop.ui.components.shared.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EmployeeListItem(
    modifier: Modifier = Modifier,
    employee: Employee,
    selected: Boolean,
    viewModel: EmployeesViewModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {

    val scope = rememberCoroutineScope()

    ListItem(
    headlineContent = { Text(employee.name) },
    supportingContent = { Text(employee.user) },
    leadingContent = { Leading(scope, employee, viewModel) },
    trailingContent = {
        ListItemTrailing(
            selected = selected,
            syncStatus = employee.syncStatus,
        ) { }
    },
    modifier = modifier
        .padding(1.5.dp)
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    )
}

@Composable
private fun Leading(scope: CoroutineScope, employee: Employee, viewModel: EmployeesViewModel) {
    Box(
        Modifier
        .size(60.dp)
        .clickable { scope.launch { viewModel.toggleUserAccess(employee) } }
    ) {
        if(employee.status == 0) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Lock",
                tint = Color.Red,
                modifier = Modifier.size(50.dp).align(Alignment.Center)
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.LockOpen,
                contentDescription = "Unlock",
                tint = Color.Green,
                modifier = Modifier.size(50.dp).align(Alignment.Center)
            )
        }
    }
}