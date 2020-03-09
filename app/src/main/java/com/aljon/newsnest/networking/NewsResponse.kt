package com.aljon.newsnest.networking

import com.aljon.newsnest.model.Article
import com.aljon.newsnest.model.NetworkArticle
import com.squareup.moshi.Json

data class NewsResponse(
    val status: String,

    val totalResults: Int,

    @Json(name = "articles")
    val articles: List<NetworkArticle>
)
