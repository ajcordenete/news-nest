package com.aljon.newsnest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "database_article_table")
data class DatabaseArticle(

    @PrimaryKey
    var url: String,

    var sourceName: String?,

    var title: String,

    var author: String?,

    var description: String?,

    var urlToImage: String?,

    var datePublished: String?,

    var isSaved: Boolean = false
)

fun List<DatabaseArticle>.asDomainModel() : List<Article> {
    return this.map {
        Article (
            sourceName = it.sourceName,
            title = it.title,
            author = it.author,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            datePublished = it.datePublished
        )
    }
}