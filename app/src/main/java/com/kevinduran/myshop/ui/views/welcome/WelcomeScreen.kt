package com.kevinduran.myshop.ui.views.welcome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.UserCheck
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.ui.components.dialogs.ErrorDialog
import com.kevinduran.myshop.ui.components.dialogs.LicenseDialog
import com.kevinduran.myshop.ui.components.dialogs.LoadingDialog
import com.kevinduran.myshop.ui.components.dialogs.UserDialog
import com.kevinduran.myshop.ui.viewmodel.AuthViewModel
import com.kevinduran.myshop.ui.viewmodel.EmployeesViewModel
import com.kevinduran.myshop.ui.viewmodel.PreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    employeesViewModel: EmployeesViewModel = hiltViewModel()
) {

    //States
    val authUiState by authViewModel.uiState.collectAsState()
    val pageCount = 6
    val pagerState = rememberPagerState { pageCount }

    val scope = rememberCoroutineScope()
    var showLicenseDialog by remember { mutableStateOf(false) }
    var isLicenseValueSuccess by rememberSaveable { mutableStateOf(false) }
    val showBlur by remember {
        derivedStateOf { showLicenseDialog || authUiState.loading || authUiState.error.isNotEmpty() }
    }
    var showUserDialog by remember { mutableStateOf(false) }
    val authLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        scope.launch { authViewModel.handleSignInResult(result.data) }
    }

    LoadingDialog(authUiState.loading) { }

    UserDialog(
        visible = showUserDialog,
        onDismiss = { showUserDialog = false },
        onSuccess = { nextPage(scope, pagerState); showUserDialog = false },
        authViewModel = authViewModel,
        employeesViewModel = employeesViewModel
    )

    LicenseDialog(
        authViewModel = authViewModel,
        visible = showLicenseDialog,
        onDismiss = { showLicenseDialog = false },
        onFailure = { isLicenseValueSuccess = false },
        onSuccess = {
            isLicenseValueSuccess = true
            showLicenseDialog = false
        }
    )

    ErrorDialog(
        visible = authUiState.error.isNotEmpty(),
        content = authUiState.error
    ) { authViewModel.dismissError() }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    renderEffect = if (showBlur) BlurEffect(15f, 15f) else null
                }
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->

                when (page) {
                    0 -> {
                        WelcomePage(
                            image = { LottieAnimated("aruba_welcome.json") },
                            title = stringResource(R.string.welcome_title),
                            description = stringResource(R.string.welcome_body),
                            onClick = { nextPage(scope, pagerState) }
                        )
                    }

                    1 -> {
                        WelcomePage(
                            image = { LottieAnimated("aruba_data_security.json") },
                            title = stringResource(R.string.welcome_security_title),
                            description = stringResource(R.string.welcome_security_body)
                        ) { nextPage(scope, pagerState) }
                    }

                    2 -> {
                        val nextButtonText = stringResource(R.string.btn_next)
                        val licenseButtonText = stringResource(R.string.btn_record_license)
                        WelcomePage(
                            image = { LottieAnimated("aruba_access_denied.json") },
                            title = stringResource(R.string.welcome_private_access_title),
                            textButton = if (isLicenseValueSuccess) nextButtonText
                            else licenseButtonText,
                            description = stringResource(R.string.welcome_private_access_body)
                        ) {
                            if (!isLicenseValueSuccess) {
                                showLicenseDialog = true
                            } else {
                                nextPage(scope, pagerState)
                            }
                        }
                    }

                    3 -> {
                        WelcomeUserTypeSelector(
                            image = {},
                        ) { isAdmin ->
                            if (!isAdmin) {
                                showUserDialog = true
                            } else {
                                scope.launch {
                                    preferencesViewModel.addIsAdmin(isAdmin)
                                    nextPage(scope, pagerState)
                                }
                            }
                        }
                    }

                    4 -> {
                        val nextButtonText = stringResource(R.string.btn_next)
                        val loginButtonText = stringResource(R.string.btn_login)
                        WelcomePage(
                            image = { LottieAnimated("aruba_login.json") },
                            title = stringResource(R.string.welcome_login_title),
                            textButton = if (authUiState.user == null) loginButtonText else nextButtonText,
                            description = stringResource(R.string.welcome_login_body)
                        ) {
                            if (authUiState.user == null) {
                                authLauncher.launch(authViewModel.getGoogleSingInIntent())
                            } else {
                                nextPage(scope, pagerState)
                            }
                        }
                    }

                    5 -> {
                        WelcomePage(
                            image = { LottieAnimated("canela_happy_animation.json") },
                            title = stringResource(R.string.welcome_thanks_title),
                            textButton = stringResource(R.string.btn_start),
                            description = stringResource(R.string.welcome_thanks_body)
                        ) {
                            navController.popBackStack()
                            navController.navigate(Routes.Home.route)
                        }
                    }
                }
            }
            LinearProgressIndicator(
                progress = { (pagerState.currentPage + 1) / pageCount.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

private fun nextPage(scope: CoroutineScope, pagerState: PagerState) {
    scope.launch {
        pagerState.animateScrollToPage(
            pagerState.currentPage + 1
        )
    }
}


@Composable
private fun LottieAnimated(asset: String, iterations: Int = LottieConstants.IterateForever) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(asset)
    )

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun WelcomePage(
    image: @Composable () -> Unit,
    title: String,
    description: String,
    textButton: String = stringResource(R.string.btn_next),
    onClick: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            image()
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = description,
                overflow = TextOverflow.Ellipsis
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(50.dp),
            shape = RoundedCornerShape(35f),
            onClick = onClick
        ) {
            Text(textButton)
        }
    }
}

@Composable
private fun WelcomeUserTypeSelector(
    image: @Composable () -> Unit,
    textButton: String = stringResource(R.string.btn_next),
    onClick: (Boolean) -> Unit
) {
    var isAdminSelected by remember { mutableStateOf(true) }
    var isEmployeeSelected by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            image()
        }
        Text(
            text = "Cargo",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
        Column(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp)
        ) {
            ListItem(
                headlineContent = { Text("Administrador") },
                leadingContent = { Icon(Lucide.UserCheck, "") },
                trailingContent = {
                    Checkbox(
                        checked = isAdminSelected,
                        onCheckedChange = {
                            if (it) {
                                isAdminSelected = true
                                isEmployeeSelected = false
                            } else {
                                isAdminSelected = false
                                isEmployeeSelected = true
                            }
                        }
                    )
                }
            )
            ListItem(
                headlineContent = { Text("Empleado") },
                leadingContent = { Icon(Lucide.UserCheck, "") },
                trailingContent = {
                    Checkbox(
                        checked = isEmployeeSelected,
                        onCheckedChange = {
                            if (it) {
                                isAdminSelected = false
                                isEmployeeSelected = true
                            } else {
                                isAdminSelected = true
                                isEmployeeSelected = false
                            }
                        }
                    )
                }
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(50.dp),
            shape = RoundedCornerShape(35f),
            onClick = { onClick(isAdminSelected) }
        ) {
            Text(textButton)
        }
    }
}