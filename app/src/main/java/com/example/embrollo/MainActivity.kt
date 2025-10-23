package com.example.embrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.embrollo.ui.theme.EmbrolloTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.embrollo.navigation.NavigationEvent
import com.example.embrollo.navigation.Screen
import com.example.embrollo.ui.screens.HomeScreen
import com.example.embrollo.ui.screens.ProfileScreen
import com.example.embrollo.ui.screens.SettingsScreen
import com.example.embrollo.ui.screens.RegistrationScreen
import com.example.embrollo.ui.screens.SummaryScreen
import com.example.embrollo.viewmodels.MainViewModel
import com.example.embrollo.viewmodels.RegistrationViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmbrolloTheme {
                val viewModel: MainViewModel = viewModel()
                // Instancia el nuevo ViewModel de Registro
                val regViewModel: RegistrationViewModel = viewModel()
                val navController = rememberNavController()

                // Escuchar eventos de MainViewModel
                LaunchedEffect(key1 = viewModel) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }

                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()

                        }
                    }
                }

                // Escuchar eventos de RegistrationViewModel (para la navegación de éxito)
                LaunchedEffect(key1 = regViewModel) {
                    regViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    // Al navegar al resumen, eliminamos la pantalla de registro de la pila
                                    popUpTo(Screen.Registration.route) { inclusive = true }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                                regViewModel.navigationEventHandled()
                            }
                            else -> {}
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        // Cambiamos el inicio para ir a Registro
                        startDestination = Screen.Registration.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Pantallas existentes
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = viewModel)
                        }

                        // NUEVAS PANTALLAS
                        composable(route = Screen.Registration.route) {
                            RegistrationScreen(
                                viewModel = regViewModel,
                                // La navegación ocurre en el LaunchedEffect, esta lambda es solo para consistencia
                                onRegistrationSuccess = { /* No-op, la navegación es reactiva a regViewModel.navigationEvents */ }
                            )
                        }

                        composable(route = Screen.Summary.route) {
                            SummaryScreen(
                                viewModel = regViewModel,
                                onNavigateToHome = {
                                    // Volver a Home y limpiar el back stack después del resumen
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true } // Se asegura que Summary se elimine
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}