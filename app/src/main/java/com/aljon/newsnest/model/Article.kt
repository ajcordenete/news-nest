package com.aljon.newsnest.model

import com.squareup.moshi.Json

data class Article (
    var sourceName: String?,

    var title: String,

    var author: String?,

    var description: String?,

    var url: String,

    var urlToImage: String?,

    var datePublished: String?
)