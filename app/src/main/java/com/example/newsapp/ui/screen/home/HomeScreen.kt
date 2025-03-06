package com.example.newsapp.ui.screen.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.R
import com.example.newsapp.core.news.model.Article
import com.example.newsapp.ui.common.ErrorMessage
import com.example.newsapp.ui.common.NewsToolbar
import com.example.newsapp.ui.screen.home.view_model.HomeViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = hiltViewModel()

    homeViewModel.callNewsApi(context)

    Column(modifier = Modifier.fillMaxSize()) {
        NewsToolbar(
            title = stringResource(id = R.string.app_name),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        )

        if (homeViewModel.noInternet.value) {
            ErrorMessage(stringResource(id = R.string.no_internet_connection))
        } else {

            if (homeViewModel.error.value) {
                ErrorMessage(errorMsg = stringResource(id = R.string.error_fetching_news))
            } else {
                if (homeViewModel.list.value.isNotEmpty()) {
                    HomeScreenUI(
                        articles = homeViewModel.list.value,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
                    ErrorMessage(stringResource(id = R.string.loading_news))
                }
            }

        }

    }
}

@Composable
private fun HomeScreenUI(articles: List<Article>, modifier: Modifier) {

    LazyColumn(modifier = modifier) {
        items(articles.size) { index ->
            ArticleItem(article = articles[index])
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = getNewsImage(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "By ${article.author ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Published at: ${article.publishedAt}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun getNewsImage(imageUrl: String) = rememberAsyncImagePainter(
    model = imageUrl,
    placeholder = painterResource(id = R.drawable.ic_launcher_background)
)




