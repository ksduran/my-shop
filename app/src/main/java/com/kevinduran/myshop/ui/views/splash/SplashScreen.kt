package com.kevinduran.myshop.ui.views.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.R
import com.kevinduran.myshop.config.routes.Routes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(1000)
        val user = Firebase.auth.currentUser
        navController.popBackStack()
        if(user == null) {
            navController.navigate(Routes.Welcome.route)
        } else {
            navController.navigate(Routes.Home.route)
        }
    }

    Scaffold { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .clip(MaterialShapes.Cookie9Sided.toShape())
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(30.dp)
                    .align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter).padding(vertical = 30.dp)
            ) {
                Text("by", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Kevin Duran",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}