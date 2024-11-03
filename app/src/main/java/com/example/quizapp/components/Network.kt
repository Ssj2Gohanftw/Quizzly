package com.example.quizapp.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
//function to check the network availability and returns a boolean if it is there or not
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager//get the connectivity manager service
    val network = connectivityManager.activeNetwork ?: return false//get the active network
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false//get the network capabilities
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}