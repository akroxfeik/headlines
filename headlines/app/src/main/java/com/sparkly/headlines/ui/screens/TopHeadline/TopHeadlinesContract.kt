package com.sparkly.headlines.ui.screens.TopHeadline

import com.sparkly.headlines.data.model.Article

class TopHeadlinesContract {
    data class State (
        val list: List<Article> = listOf(),
        val isLoading: Boolean = false,
        val isConnected: Boolean = false
    )
}