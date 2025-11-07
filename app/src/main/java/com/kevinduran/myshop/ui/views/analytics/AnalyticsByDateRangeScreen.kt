package com.kevinduran.myshop.ui.views.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.extensions.toDateString
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.modals.SalesDateRangeModal
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel
import kotlinx.coroutines.launch

@Composable
fun AnalyticsByDateRangeScreen(
    navController: NavController,
    viewModel: SalesViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val isDatePickerOpen = remember { mutableStateOf(false) }
    val dateRange = rememberSaveable { mutableStateOf(viewModel.getCurrentDayRange()) }
    var summary by remember { mutableStateOf(EarningsSummary(0, 0)) }
    val scope = rememberCoroutineScope()

    LoadingDialog(visible = uiState.loading)

    ErrorDialog(
        visible = uiState.error.isNotEmpty(),
        content = uiState.error
    ) { viewModel.dismissError() }

    LaunchedEffect(Unit) {
        summary = viewModel.getEarningByDateRange(dateRange.value.first, dateRange.value.second)
    }

    Scaffold(topBar = { TopBar(navController, isDatePickerOpen) }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            SalesDateRangeModal(
                visible = isDatePickerOpen.value,
                onDismiss = { isDatePickerOpen.value = false }
            ) {
                scope.launch {
                    isDatePickerOpen.value = false
                    dateRange.value = it
                    summary = viewModel.getEarningByDateRange(
                        dateRange.value.first,
                        dateRange.value.second
                    )
                }
            }

            Column(Modifier.padding(horizontal = 16.dp)) {

                Header()
                Cards(dateRange.value, summary.earnings, summary.totalSold)

            }

        }
    }

}

@Composable
private fun AnalyticsItem(title: String, value: Int, icon: ImageVector = Icons.Rounded.Savings) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(value.toCurrency()) },
        leadingContent = { Icon(icon, "Money", tint = MaterialTheme.colorScheme.primary) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(28))
            .padding(vertical = 1.5.dp)
    )
}

@Composable
private fun Header() {
    Box(
        Modifier
            .defaultMinSize(minHeight = 100.dp)
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(28.dp))
    ) {
        Text(
            text = "Ganancia por fechas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun Cards(dateRange: Pair<Long, Long>, earnings: Int, totalSold: Int) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(28.dp))
    ) {
        Column {
            Box(Modifier.weight(.5f)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Filtro actual",
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${dateRange.first.toDateString("dd MMM")} al ${
                            dateRange.second.toDateString(
                                "dd MMM"
                            )
                        }",
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Box(
                Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        RoundedCornerShape(28.dp)
                    )
            ) {
                Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsItem("Ganancias", earnings, Icons.Rounded.Savings)
                    AnalyticsItem("Total recaudado", totalSold, Icons.Rounded.AttachMoney)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, isDatePickerOpen: MutableState<Boolean>) {
    TopAppBar(
        title = { Text("Filtrar ganancias") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        },
        actions = {
            TextButton(onClick = { isDatePickerOpen.value = true }) {
                Text("Filtrar")
            }
        }
    )
}