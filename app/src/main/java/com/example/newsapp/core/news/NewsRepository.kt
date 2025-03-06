package com.example.newsapp.core.news

import com.example.newsapp.core.news.model.NewsResponse
import com.example.newsapp.ui.utility.Constants.API_KEY
import com.example.newsapp.ui.utility.Constants.FROM_DATE
import com.example.newsapp.ui.utility.Constants.QUERY
import com.example.newsapp.ui.utility.Constants.SORT_BY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {

    fun getNews(): Flow<NewsResponse?> {
        return flow {
            val response = newsService.getNews(
                apiKey = API_KEY,
                from = FROM_DATE,
                sortBy = SORT_BY,
                query = QUERY
            )
            if (response.isSuccessful) {
                emit(response.body())
            } else {
                emit(null)
            }
        }
    }
}