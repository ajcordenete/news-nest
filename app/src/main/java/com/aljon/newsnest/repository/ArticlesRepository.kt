package com.aljon.newsnest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.aljon.newsnest.database.NewsDatabase
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.model.NetworkArticle
import com.aljon.newsnest.model.asDatabaseModel
import com.aljon.newsnest.model.asDomainModel
import com.aljon.newsnest.networking.NewsApi
import com.aljon.newsnest.networking.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesRepository(val database: NewsDatabase) {

    val articles: LiveData<List<Article>> =
        Transformations.map(database.articleDao.getAllArticles()) {
            it.asDomainModel()
        }

    suspend fun refreshDatabaseArticles(articles: List<NetworkArticle>) {
        withContext(Dispatchers.IO) {
            database.articleDao.deleteAll()
            database.articleDao.insertAll(*articles.asDatabaseModel())
        }
    }
}