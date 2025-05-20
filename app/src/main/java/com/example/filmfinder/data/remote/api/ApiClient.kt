package com.example.filmfinder.data.remote.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Enhanced API client for the OMDb API with extended filter options.
 * Uses HttpURLConnection instead of Retrofit.
 */
object ApiClient {
    // Base URL for the OMDb API
    private const val BASE_URL = "https://www.omdbapi.com/"

    // API key for the OMDb API
    // TODO: Replace with your own API key
    const val API_KEY = "4b324f15"

    /**
     * Make a GET request to the OMDb API with enhanced filter options.
     * @param params Map of query parameters
     * @return String containing the JSON response
     */
    suspend fun get(params: Map<String, String>): String = withContext(Dispatchers.IO) {
        val urlBuilder = StringBuilder(BASE_URL)
        urlBuilder.append("?")

        // Add API key to parameters
        val paramsWithKey = params.toMutableMap()
        paramsWithKey["apikey"] = API_KEY

        // Build query string
        paramsWithKey.forEach { (key, value) ->
            urlBuilder.append("${key}=${URLEncoder.encode(value, "UTF-8")}&")
        }

        // Remove trailing '&'
        if (urlBuilder.last() == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length - 1)
        }

        Log.d("ApiClient", "Request URL: ${urlBuilder.toString()}")

        val url = URL(urlBuilder.toString())
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                return@withContext response.toString()
            } else {
                throw Exception("HTTP Error: $responseCode")
            }
        } catch (e: Exception) {
            Log.e("ApiClient", "Error making API request", e)
            throw e
        } finally {
            connection.disconnect()
        }
    }
}