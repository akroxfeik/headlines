package com.sparkly.headlines.ui.screens.TopHeadline

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparkly.headlines.R
import com.sparkly.headlines.data.model.Article
import com.sparkly.headlines.ui.screens.CustomImage
import com.sparkly.headlines.ui.screens.LoadingBar

@Composable
fun TopHeadlines(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
    onItemClicked: (itemId: String) -> Unit)
{
    Box {
        when {
            viewModel.state.isLoading -> LoadingBar()
            !viewModel.state.isConnected -> Text(
                text = stringResource(R.string.not_connected),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            )
        }
        Column(Modifier.fillMaxSize()) {
            LazyRow {
                items(viewModel.state.list) { item ->
                    CardHeadline(item, onItemClicked = onItemClicked)
                }
            }
        }
    }
}

@Composable
fun CardHeadline(item: Article, onItemClicked: (id: String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .animateContentSize()
            .clickable { onItemClicked(item.url) }
            .padding(5.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxSize()
        ) {
            CustomImage(item.urlToImage, item.title, 100.dp)
            Text(
                text = item.title,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                minLines = 1
            )
        }
    }
}