package com.example.newsapp.ui.screen.home.view_model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.core.news.NewsRepository
import com.example.newsapp.core.news.model.Article
import com.example.newsapp.ui.utility.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    val list = mutableStateOf<List<Article>>(emptyList())
    val error = mutableStateOf<Boolean>(false)
    val noInternet = mutableStateOf<Boolean>(false)


    fun callNewsApi(context: Context) {
        if (NetworkUtils.isInternetAvailable(context)) {
            fetchNews()
        } else {
            noInternet.value = true
        }
    }

    private fun fetchNews() {
        viewModelScope.launch {
            newsRepository.getNews().collect { newsResponse ->
                if (newsResponse?.articles?.isNotEmpty() == true) {
                    list.value = newsResponse.articles.toMutableList()
                } else {
                    error.value = true
                }

            }
        }
    }
}