package com.sparkly.headlines.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sparkly.headlines.data.model.Article
import com.sparkly.headlines.data.repository.MainRepository
import com.sparkly.headlines.ui.navigation.Arg
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeadlineViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repo: MainRepository
): ViewModel() {
    private val _receivedObject = mutableStateOf<Article?>(null)
    val receivedObject: State<Article?> = _receivedObject
    init {
        stateHandle.get<String>(Arg.NEWS_JSON)?.let {
            _receivedObject.value = Gson().fromJson(it, Article::class.java)
        }
    }
}