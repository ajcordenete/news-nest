package com.aljon.newsnest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


private const val SECOND_MILLIS = 1000;
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS;
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS;
private const val DAY_MILLIS = 24 * HOUR_MILLIS;

fun hasNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val networkInfo = connectivityManager?.activeNetworkInfo
    return networkInfo?.isConnected ?: false
}

fun hasInternetConnected(context: Context): Boolean {
    if (hasNetworkAvailable(context)) {
        try {
            val connection = URL(Constants.READABILITY_SERVER).openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Test")
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 1000
            connection.connect()
            Log.d("", "hasInternetConnected: ${(connection.responseCode == 200)}")
            return (connection.responseCode == 200)
        } catch (e: IOException) { }
    }
    return false
}

fun timeAgo(timeStamp: String): String {
    var time = LocalDateTime.parse(timeStamp.removeSuffix("Z"))
        .toEpochSecond(ZoneOffset.UTC) * SECOND_MILLIS
    val now = System.currentTimeMillis()
    val diff = now - time;

    return when {
        diff < MINUTE_MILLIS -> "Just now"
        diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS}m ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS}hr ago"
        else -> "${diff / DAY_MILLIS}d ago"
    }
}