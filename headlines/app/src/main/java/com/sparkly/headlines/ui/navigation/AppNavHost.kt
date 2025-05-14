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
            TopHeadlines(onItemClicked = {json ->
                navController.navigate("${Screen.Details.route}/$json")
            })
        }
        composable(Screen.Details.route + "/{news-json}",
            arguments = listOf(navArgument(Arg.NEWS_JSON) {
                type = NavType.StringType
                nullable = true
            })) {
            HeadlineDetails()
        }
    }
}

object Arg {
    const val NEWS_JSON = "news-json"
}