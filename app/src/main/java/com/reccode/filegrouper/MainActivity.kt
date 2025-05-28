package com.reccode.filegrouper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.reccode.filegrouper.navigation.AppNavGraph
import com.reccode.filegrouper.viewmodel.AppViewModel
import com.reccode.filegrouper.ui.theme.FilegrouperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewModel (mantenha assim)
        val viewModel: AppViewModel by viewModels()

        setContent {
            FilegrouperTheme {
                // 1. NavController deve ser criado dentro do Composition
                val navController = rememberNavController()

                // 2. Passa as dependências para o grafo de navegação
                AppNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
