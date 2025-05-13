package com.sparkly.headlines.data.model

import com.squareup.moshi.Json

data class Headline(
    @Json(name = "id")
    val id: String
)