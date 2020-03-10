package com.aljon.newsnest.ui.articledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleDetailViewModel(val url: String): ViewModel() {

    private val _webUrl = MutableLiveData<String>()
    val webUrl: LiveData<String> get() = _webUrl

    init {
        _webUrl.value = url
    }
}