package com.aljon.newsnest.ui.news

import androidx.lifecycle.*
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.model.asDomainModel
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.networking.NewsApi
import com.aljon.newsnest.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException
import kotlin.reflect.jvm.internal.impl.load.java.Constant

/**
 * ViewModel implementation for News
 * This is being reused for all fragment
 * that displays list of articles
 *
 *@param: category - category for request. Request will default to all if this is empty
 */
class NewsViewModel(val category: String): ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus> get() = _apiStatus

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    var query: String = ""

    init {
        // Don't perform init of news when searching...
        if(category != Constants.SEARCH)
            getNews()
    }

    /**
     * Get request for top headlines using the category
     */
    fun getNews() {
        coroutineScope.launch {
            try {
                _apiStatus.value = ApiStatus.LOADING

                var response = NewsApi.retrofitService.getNews(category = category)
                if(response.isSuccessful) {
                    _apiStatus.value = ApiStatus.SUCCESS
                    _articles.value = (response.body()!!.articles).asDomainModel()

                    _apiStatus.value = ApiStatus.DONE
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun searchNews(keyword: String) {
        query = keyword
        coroutineScope.launch {
            try {
                _apiStatus.value = ApiStatus.LOADING

                var response = NewsApi.retrofitService.searchNews(keyword)
                if(response.isSuccessful) {
                    _apiStatus.value = ApiStatus.DONE
                    _articles.value = (response.body()?.articles)?.asDomainModel()
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                _apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val category: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(category) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}