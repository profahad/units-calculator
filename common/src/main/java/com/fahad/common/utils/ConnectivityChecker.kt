package com.fahad.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ConnectivityChecker @Inject constructor(
    @ActivityContext private val context: Context,
) {

    fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val deviceData = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        val deviceWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        val deviceEthernet = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false

        return deviceData || deviceWifi || deviceEthernet
    }
}
