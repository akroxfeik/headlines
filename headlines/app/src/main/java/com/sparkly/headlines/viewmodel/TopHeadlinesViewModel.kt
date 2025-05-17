package com.sparkly.headlines.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparkly.headlines.BuildConfig
import com.sparkly.headlines.data.repository.MainRepository
import com.sparkly.headlines.ui.screens.TopHeadline.TopHeadlinesContract
import com.sparkly.headlines.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repo: MainRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    var state by mutableStateOf(
        TopHeadlinesContract.State(
            list = listOf(),
            isLoading = true,
            isConnected = true
        )
    )

    init {
        viewModelScope.launch { getTopHeadlines() }
    }

    private fun getTopHeadlines() {
        viewModelScope.launch {
            val isConnected = networkHelper.isNetworkConnected()
            val response = if(isConnected) repo.getTopHeadlines(BuildConfig.NEWS_SOURCE) else null

            state = state.copy(
                list = response?.body()?.articles?.sortedByDescending { article ->
                    try {
                        OffsetDateTime.parse(article.publishedAt)
                    } catch (e: Exception) {
                        OffsetDateTime.MIN
                    }
                } ?: listOf(),
                isLoading = false,
                isConnected = isConnected
            )
        }
    }

    fun reconnection() {
        getTopHeadlines()
    }
}