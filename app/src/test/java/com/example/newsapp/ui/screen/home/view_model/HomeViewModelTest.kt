package com.example.newsapp.ui.screen.home.view_model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.core.news.NewsRepository
import com.example.newsapp.core.news.model.Article
import com.example.newsapp.core.news.model.NewsResponse
import com.example.newsapp.core.news.model.Source
import com.example.newsapp.ui.utility.NetworkUtils
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var newsRepository: NewsRepository = mockk(relaxed = true)

    @Mock
    private lateinit var context: Context

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(newsRepository)
    }

    @Test
    fun `callNewsApi with internet available fetches news`() = runTest {
        `when`(NetworkUtils.isInternetAvailable(context)).thenReturn(true)

        homeViewModel.callNewsApi(context)

        verify(newsRepository).getNews()
    }

    @Test
    fun `callNewsApi with no internet sets noInternet to true`() = runTest {
        `when`(NetworkUtils.isInternetAvailable(context)).thenReturn(false)

        homeViewModel.callNewsApi(context)

        assert(homeViewModel.noInternet.value)
    }

    @Test
    fun `fetchNews with articles sets list`() = runTest {

        val source: Source = Source(name = "Test Source", id = "1")
        val articles = listOf(Article(title = "Test Article", source = source))
        val response = NewsResponse(
            status = "",
            totalResults = 10,
            articles = articles
        )
        `when`(newsRepository.getNews()).thenReturn(
            flowOf(response)
        )

        homeViewModel.callNewsApi(context)

        assert(homeViewModel.list.value == articles)
    }

    @Test
    fun `fetchNews with no articles sets error to true`() = runTest {
        val response = NewsResponse(
            status = "",
            totalResults = 0,
            articles = emptyList()
        )
        `when`(newsRepository.getNews()).thenReturn(flowOf(response))

        homeViewModel.callNewsApi(context)

        assert(homeViewModel.error.value)
    }
}