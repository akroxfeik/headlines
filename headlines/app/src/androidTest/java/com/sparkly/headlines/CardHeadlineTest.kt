package com.sparkly.headlines

import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.gson.Gson
import com.sparkly.headlines.data.model.Article
import com.sparkly.headlines.data.model.Source
import com.sparkly.headlines.ui.screens.TopHeadline.CardHeadline
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CardHeadlineTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleArticle = Article(
        source = Source("bbc-news","BBC News"),
        author = "Jane Doe",
        title = "Breaking News",
        description = "Something happened",
        url = "https://example.com",
        urlToImage = "https://example.com/image.jpg",
        publishedAt = "2025-05-18T12:00:00Z",
        content = "Full content here"
    )

    @Test
    fun cardHeadline_displaysTitle() {
        composeTestRule.setContent {
            CardHeadline(item = sampleArticle)
        }

        // Assert that the title string is shown
        composeTestRule
            .onNodeWithText(sampleArticle.title)
            .assertExists()
    }

    @Test
    fun cardHeadline_clickInvokesCallbackWithEncodedJson() {
        var clickedJson: String? = null

        composeTestRule.setContent {
            CardHeadline(item = sampleArticle) { json ->
                clickedJson = json
            }
        }

        // Perform click on the title node (the Card is clickable around it)
        composeTestRule
            .onNodeWithText(sampleArticle.title)
            .performClick()

        // Compute what we expect
        val expectedJson = Uri.encode(Gson().toJson(sampleArticle))
        assertEquals(expectedJson, clickedJson)
    }
}
