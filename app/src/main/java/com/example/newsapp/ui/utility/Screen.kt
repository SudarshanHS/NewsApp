package com.example.newsapp.ui.utility

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Splash : Screen("splash")
}
