package com.kevinduran.myshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kevinduran.myshop.config.navigation.AppNavigation
import com.kevinduran.myshop.ui.theme.CoronaRosaMaterialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoronaRosaMaterialTheme {
                AppNavigation()
            }
        }
    }
}
