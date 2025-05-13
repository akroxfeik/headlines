package com.sparkly.headlines.data.repository

import com.sparkly.headlines.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun getTopHeadlines(source: String?) = apiHelper.getTopHeadlines(source)
}