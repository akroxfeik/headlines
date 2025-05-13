package com.sparkly.headlines.data.api

import com.sparkly.headlines.data.model.Headline
import retrofit2.Response

interface ApiHelper {
    suspend fun getTopHeadlines(source: String?): Response<List<Headline>>
}