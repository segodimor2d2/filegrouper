package com.reccode.filegrouper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reccode.filegrouper.ui.screen.HomeScreen
import com.reccode.filegrouper.viewmodel.AppViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"  // Mantém a Home como tela inicial
    ) {
        // Tela Home (original)
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
