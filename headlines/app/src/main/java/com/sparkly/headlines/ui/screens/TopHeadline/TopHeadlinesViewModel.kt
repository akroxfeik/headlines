package com.sparkly.headlines.ui.screens.TopHeadline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sparkly.headlines.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repo: MainRepository
): ViewModel() {

}