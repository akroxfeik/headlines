package com.sparkly.headlines.ui.screens.Headline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparkly.headlines.ui.screens.CustomImage
import com.sparkly.headlines.viewmodel.HeadlineViewModel

@Composable
fun HeadlineDetails(viewModel: HeadlineViewModel = hiltViewModel()) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CustomImage(viewModel.receivedObject.value?.urlToImage, viewModel.receivedObject.value?.title!!)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Text(
                    text = viewModel.receivedObject.value?.title!!,
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Description: ${viewModel.receivedObject.value?.description!!}"
                )
                Text(
                    text = "Content: ${viewModel.receivedObject.value?.content!!}"
                )
            }
        }
    }
}