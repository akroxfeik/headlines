package com.sparkly.headlines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel // Import for ViewModel
import com.sparkly.headlines.data.model.Article
import com.sparkly.headlines.data.model.Headline
import com.sparkly.headlines.data.model.Source
import com.sparkly.headlines.data.repository.MainRepository
import com.sparkly.headlines.utils.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Response

// 1. Define Interfaces
interface INetworkHelper {
    fun isNetworkConnected(): Boolean
}

interface IMainRepository {
    suspend fun getTopHeadlines(source: String): Response<Headline>
}

// 2.  Make Real Classes Implement Interfaces
class NetworkHelper : INetworkHelper {
    override fun isNetworkConnected(): Boolean {
        // Your implementation here
        return true; //Added a return to compile, you need to add your impl
    }
}

class MainRepository : IMainRepository {
    override suspend fun getTopHeadlines(source: String): Response<Headline> {
        //  Your implementation here
        return Response.success(Headline("ok", 0, emptyList())) //Added a return to compile, you need to add your impl
    }
}

// 3. Define a minimal TopHeadlinesState for the test
data class TopHeadlinesState(
    val isConnected: Boolean = true,
    val isLoading: Boolean = false,
    val list: List<Article> = emptyList()
)

// 4. Update TopHeadlinesViewModel to use interfaces
class TopHeadlinesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: IMainRepository, // Use the interface!
    private val networkHelper: INetworkHelper    // Use the interface!
) : ViewModel() {

    // ... (rest of your ViewModel code)
    val state = TopHeadlinesState()
    fun reconnection() {

    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TopHeadlinesViewModelTest {

    // Makes LiveData & Compose state updates execute immediately
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Swaps Dispatchers.Main for a TestDispatcher
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 5. Test doubles Implement Interfaces
    class TestNetworkHelper(private var isConnected: Boolean = true) : INetworkHelper {
        override fun isNetworkConnected(): Boolean {
            return isConnected
        }

        fun setNetworkConnected(connected: Boolean) {
            isConnected = connected
        }
    }

    class TestMainRepository(private var headlinesResponse: Response<Headline>?) : IMainRepository {
        override suspend fun getTopHeadlines(source: String): Response<Headline> {
            return headlinesResponse ?: error("Repository response not set for test")
        }

        fun setResponse(response: Response<Headline>?) {
            headlinesResponse = response
        }
    }

    private fun makeArticle(title: String, publishedAt: String) = Article(
        source = Source("bbc-news", "BBC News"),
        author = "Author",
        title = title,
        description = "",
        url = "",
        urlToImage = "",
        publishedAt = publishedAt,
        content = ""
    )

    @Test
    fun testNoNetworkStateReflectsDisconnectedAndEmptyList() = runTest {
        val testNetworkHelper = TestNetworkHelper(false)
        val testRepo = TestMainRepository(null)

        val vm = TopHeadlinesViewModel(SavedStateHandle(), testRepo, testNetworkHelper)
        advanceUntilIdle()

        Assert.assertFalse(vm.state.isConnected)
        Assert.assertFalse(vm.state.isLoading)
        Assert.assertTrue(vm.state.list.isEmpty())
    }

    @Test
    fun testNetworkAvailableLoadsAndSortsArticlesByPublishedAtDescending() = runTest {
        val testNetworkHelper = TestNetworkHelper(true)
        val testRepo = TestMainRepository(null)

        val a1 = makeArticle("First", "2025-05-17T10:00:00Z")
        val a2 = makeArticle("Second", "2025-05-18T09:00:00Z")
        val a3 = makeArticle("BadDate", "not-a-date")
        val articles = listOf(a1, a2, a3)

        testRepo.setResponse(
            Response.success(
                Headline(
                    status = "ok",
                    totalResults = articles.size,
                    articles = articles
                )
            )
        )

        val vm = TopHeadlinesViewModel(SavedStateHandle(), testRepo, testNetworkHelper)
        advanceUntilIdle()

        // After loading completes
        Assert.assertTrue(vm.state.isConnected)
        Assert.assertFalse(vm.state.isLoading)

        // Should be sorted: a2 (newest), a1, a3 (fallback to MIN)
        Assert.assertEquals(listOf(a2, a1, a3), vm.state.list)
    }

    @Test
    fun testReconnectionRetriesGetTopHeadlines() = runTest {
        val testNetworkHelper = TestNetworkHelper(false)
        val testRepo = TestMainRepository(null)

        val vm = TopHeadlinesViewModel(SavedStateHandle(), testRepo, testNetworkHelper)
        advanceUntilIdle()
        Assert.assertFalse(vm.state.isConnected)
        Assert.assertTrue(vm.state.list.isEmpty())

        // Now: network comes back
        testNetworkHelper.setNetworkConnected(true)
        val a = makeArticle("Reconnect", "2025-05-18T12:00:00Z")
        testRepo.setResponse(Response.success(Headline("ok", 1, listOf(a))))

        vm.reconnection()
        advanceUntilIdle()

        Assert.assertTrue(vm.state.isConnected)
        Assert.assertEquals(listOf(a), vm.state.list)
    }
}

/**
 * JUnit Rule that swaps Dispatchers.Main with a TestDispatcher.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
