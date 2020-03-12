package com.aljon.newsnest.networking

import com.aljon.newsnest.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.reflect.jvm.internal.impl.load.java.Constant

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getNews(@Query("country") country: String = Constants.COUNTRY,
                        @Query("category") category: String = "",
                        @Query("pageSize") size: Int = 10,
                        @Query("apiKey") key: String = Constants.API_KEY) : Response<NewsResponse>

    @GET("everything")
    suspend fun searchNews(@Query("q", encoded = true) keyword: String,
                           @Query("sortBy") sortBy: String = Constants.PUBLISHED_AT,
                           @Query("language") language: String = Constants.LANGUAGE,
                           @Query("apiKey") key: String = Constants.API_KEY) : Response<NewsResponse>
}