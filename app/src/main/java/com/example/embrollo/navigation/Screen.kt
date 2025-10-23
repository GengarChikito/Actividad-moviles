package com.example.embrollo.navigation

sealed class Screen(val route: String ){
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")

    data object Registration : Screen("registration_page")
    data object Summary : Screen("summary_page")
}