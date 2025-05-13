package com.sparkly.headlines.ui.navigation

sealed class Screen(val route: String) {
    object List : Screen("list_screen")
    object Details : Screen("details_screen")
}