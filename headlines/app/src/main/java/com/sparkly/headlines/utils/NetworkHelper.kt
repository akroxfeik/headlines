package com.sparkly.headlines.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context) {
    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}

@Composable
fun InternetConnectivityChanges(
    onAvailable: (network: Network) -> Unit,
    onLost: (network: Network) -> Unit
) {
    val context = LocalContext.current
    val networkCallback = remember {
        object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                onAvailable(network)
            }
            override fun onLost(network: Network) {
                onLost(network)
            }
        }
    }
    LaunchedEffect(key1 = context) {
        // an utility method that you've created for this
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
        connectivityManager?.registerNetworkCallback(request, networkCallback)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            // an utility method that you've created for this
            val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }
}