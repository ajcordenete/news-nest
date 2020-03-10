package com.aljon.newsnest.news

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
import androidx.lifecycle.*
import com.aljon.newsnest.database.NewsDatabase
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.model.asDomainModel
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.networking.NewsApi
import com.aljon.newsnest.repository.ArticlesRepository
import com.aljon.newsnest.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException



class NewsViewModel(application: Application, val category: String): AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus> get() = _apiStatus

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _navigateToArticleDetail = MutableLiveData<String>()
    val navigateToArticleDetail: LiveData<String> get() = _navigateToArticleDetail

    private val database = NewsDatabase.getDatabase(application)
    private val articlesRepository = ArticlesRepository(database)

    init {
        getNews()
    }

    fun getNews() {
        coroutineScope.launch {
            try {
                _apiStatus.value = ApiStatus.LOADING

                var response = NewsApi.retrofitService.getNews(category = category)
                if(response.isSuccessful) {
                    _apiStatus.value = ApiStatus.SUCCESS
                    _articles.value = (response.body()!!.articles).asDomainModel()

                   /* if(category.isNullOrEmpty()) {
                        articlesRepository.refreshDatabaseArticles(response.body()!!.articles)
                        _articles.value = articlesRepository.articles.value
                    } else {
                        _articles.value = (response.body()!!.articles).asDomainModel()
                    }*/

                    _apiStatus.value = ApiStatus.DONE
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun navigateToNewsDetail(url: String) {
        _navigateToArticleDetail.value = url
    }

    fun doneNavigatingToNewsDetail() {
        _navigateToArticleDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val application: Application, val category: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(application, category) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}