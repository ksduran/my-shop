package com.kevinduran.myshop.ui.views.analytics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinduran.myshop.config.extensions.toCurrency
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.shared.list.RoundedListContainer
import com.kevinduran.myshop.ui.components.shared.listitem.RoundedListContainerItem
import com.kevinduran.myshop.ui.viewmodel.SalesViewModel


@Composable
fun AnalyticsScreen(
    navController: NavController,
    salesViewModel: SalesViewModel = hiltViewModel()
) {
    var summaryOfDay by remember { mutableStateOf(EarningsSummary()) }
    var summaryOfWeek by remember { mutableStateOf(EarningsSummary()) }
    var summaryOfMonth by remember { mutableStateOf(EarningsSummary()) }
    var summaryOfYear by remember { mutableStateOf(EarningsSummary()) }
    val scrollState = rememberScrollState()
    val uiState by salesViewModel.uiState.collectAsState()

    LoadingDialog(visible = uiState.loading)

    ErrorDialog(
        visible = uiState.error.isNotEmpty(),
        content = uiState.error
    ) { salesViewModel.dismissError() }

    LaunchedEffect(Unit) {
        val day = salesViewModel.getCurrentDayRange()
        val week = salesViewModel.getCurrentWeekRange()
        val month = salesViewModel.getCurrentMonthRange()
        val year = salesViewModel.getCurrentYearRange()

        summaryOfDay = salesViewModel.getEarningByDateRange(day.first, day.second)
        summaryOfWeek = salesViewModel.getEarningByDateRange(week.first, week.second)
        summaryOfMonth = salesViewModel.getEarningByDateRange(month.first, month.second)
        summaryOfYear = salesViewModel.getEarningByDateRange(year.first, year.second)
    }

    Scaffold(
        topBar = { TopBar(navController = navController) }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(Modifier.verticalScroll(scrollState)) {
                AnalyticsSection("Día en curso", summaryOfDay.earnings, summaryOfDay.totalSold)
                AnalyticsSection("Esta semana", summaryOfWeek.earnings, summaryOfWeek.totalSold)
                AnalyticsSection("Este mes", summaryOfMonth.earnings, summaryOfMonth.totalSold)
                AnalyticsSection("Este año", summaryOfYear.earnings, summaryOfYear.totalSold)
            }
        }
    }

}

@Composable
private fun AnalyticsSection(title: String, earnings: Int, sold: Int) {
    SectionTitle(title)
    RoundedListContainer(
        items = listOf(
            {
                RoundedListContainerItem(
                    title = "Ganancias",
                    subtitle = earnings.toCurrency(),
                    icon = Icons.Rounded.Savings
                )
            },
            {
                RoundedListContainerItem(
                    title = "Total recaudado",
                    subtitle = sold.toCurrency(),
                    icon = Icons.Rounded.AttachMoney
                )
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Analytics") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, "Back")
            }
        },
        actions = {
            TextButton(onClick = { navController.navigate(Routes.AnalyticsByDateRange.route) }) {
                Text("Personalizado")
            }
        }
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
    )
}