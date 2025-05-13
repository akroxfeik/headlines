package com.sparkly.headlines.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparkly.headlines.ui.screens.Headline.HeadlineDetails
import com.sparkly.headlines.ui.screens.TopHeadline.TopHeadlines

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            TopHeadlines()
        }
        composable(Screen.Details.route + "/{news-id}",
            arguments = listOf(navArgument(Arg.NEWS_ID) {
                type = NavType.StringType
                nullable = true
            })) {
            HeadlineDetails()
        }
    }
}

object Arg {
    const val NEWS_ID = "news-id"
}