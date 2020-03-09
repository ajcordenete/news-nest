package com.aljon.newsnest.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.model.asDomainModel
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.networking.NewsApi
import com.aljon.newsnest.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var query: String = ""

    private val _searchStatus = MutableLiveData<ApiStatus>()
    val searchStatus: LiveData<ApiStatus> get() = _searchStatus

    private val _navigateToArticleDetail = MutableLiveData<String>()
    val navigateToArticleDetail: LiveData<String> get() = _navigateToArticleDetail

    private val _searchResults = MutableLiveData<List<Article>>()
    val searchResults: LiveData<List<Article>> get() = _searchResults

    fun searchNews(keyword: String) {
        query = keyword
        coroutineScope.launch {
            try {
                _searchStatus.value = ApiStatus.LOADING

                var response = NewsApi.retrofitService.searchNews(keyword)
                if(response.isSuccessful) {
                    _searchStatus.value = ApiStatus.DONE
                    _searchResults.value = (response.body()?.articles)?.asDomainModel()
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                _searchStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun navigateToArticleDetail(url: String) {
        _navigateToArticleDetail.value = url
    }

    fun doneNavigatingToNewsDetail() {
        _navigateToArticleDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}