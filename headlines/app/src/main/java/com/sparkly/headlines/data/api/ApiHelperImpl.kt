package com.sparkly.headlines.data.api

import com.sparkly.headlines.data.model.Headline
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getTopHeadlines(source: String?): Response<List<Headline>> = apiService.getTopHeadlines(source)
}