package com.sparkly.headlines.data.api

import com.sparkly.headlines.data.model.Headline
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("sources") sources: String?): Response<List<Headline>>
}