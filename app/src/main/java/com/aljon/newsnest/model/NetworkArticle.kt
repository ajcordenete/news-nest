package com.aljon.newsnest.model

import androidx.lifecycle.Transformations
import com.squareup.moshi.Json

data class NetworkArticle (
    var source: Source?,

    var title: String,

    var author: String?,

    var description: String?,

    var url: String,

    var urlToImage: String?,

    @Json(name = "publishedAt")
    var datePublished: String?

) {
    data class Source(var name: String?)
}

fun List<NetworkArticle>.asDatabaseModel() : Array<DatabaseArticle> {
    return map {
        DatabaseArticle(
            url = it.url,
            sourceName = it.source?.name,
            title = it.title,
            author = it.author,
            description = it.description,
            urlToImage = it.urlToImage,
            datePublished = it.datePublished,
            isSaved = false
        )
    }.toTypedArray()
}

fun List<NetworkArticle>.asDomainModel() : List<Article> {
    return map {
        Article(
            url = it.url,
            sourceName = it.source?.name,
            title = it.title,
            author = it.author,
            description = it.description,
            urlToImage = it.urlToImage,
            datePublished = it.datePublished
        )
    }
}