package com.example.my_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_api.viewmodels.ApiViewModel
import com.example.my_api.ui.theme.My_ApiTheme
import com.example.my_api.views.ResultScreen
import com.example.my_api.views.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            My_ApiTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val apiViewModel: ApiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable("search") {
            SearchScreen(
                onSearch = { artist, track ->
                    apiViewModel.searchLyrics(artist, track)
                    navController.navigate("results")
                }
            )
        }
        composable("results") {
            ResultScreen(
                apiViewModel = apiViewModel,
                onBack = {
                    apiViewModel.cancelRequest()
                    navController.popBackStack()
                }
            )
        }
    }
}
