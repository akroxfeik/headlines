package com.sparkly.headlines.ui.screens.Headline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sparkly.headlines.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeadlineViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repo: MainRepository
): ViewModel() {

}