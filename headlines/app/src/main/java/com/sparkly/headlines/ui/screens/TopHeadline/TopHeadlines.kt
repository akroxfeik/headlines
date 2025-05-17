package com.sparkly.headlines.ui.screens.TopHeadline

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.sparkly.headlines.BuildConfig
import com.sparkly.headlines.R
import com.sparkly.headlines.data.model.Article
import com.sparkly.headlines.ui.screens.CustomImage
import com.sparkly.headlines.ui.screens.LoadingBar
import com.sparkly.headlines.utils.InternetConnectivityChanges
import com.sparkly.headlines.viewmodel.TopHeadlinesViewModel

@Composable
fun TopHeadlines(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
    onItemClicked: (json: String) -> Unit)
{
    Box {
        Column {
            when {
                viewModel.state.isLoading -> LoadingBar()
                !viewModel.state.isConnected -> Text(
                    text = stringResource(R.string.not_connected),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                )
            }
            Text(
                text = "Source: ${BuildConfig.NEWS_SOURCE}"
            )
            LazyColumn (Modifier.fillMaxSize()) {
                items(viewModel.state.list) { item ->
                    CardHeadline(item, onItemClicked = onItemClicked)
                }
            }
        }
    }
    /**
     * Force try if connectivity changed.
     * Example:
     * - Open app without internet (http request fails)
     * - Internet connection changes (connects into network)
     * - App retry loading data
     */
    InternetConnectivityChanges(onAvailable = {
        viewModel.reconnection()
    }, onLost = {})
}

@Composable
fun CardHeadline(item: Article, onItemClicked: (json: String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .animateContentSize()
            .clickable {
                onItemClicked(Uri.encode(Gson().toJson(item))) }
            .padding(5.dp)
    ) {
        CustomImage(item.urlToImage, item.title)
        Text(
            modifier = Modifier.padding(5.dp),
            text = item.title,
            textAlign = TextAlign.Left,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            minLines = 1,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}