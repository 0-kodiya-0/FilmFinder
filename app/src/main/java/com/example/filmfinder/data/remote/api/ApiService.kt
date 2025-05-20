package com.example.filmfinder.data.remote.api

import android.util.Log
import com.example.filmfinder.data.local.entity.Rating
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.MovieResponse
import com.example.filmfinder.data.remote.model.SearchItem
import com.example.filmfinder.data.remote.model.SearchResponse
import org.json.JSONArray
import org.json.JSONObject

/**
 * Native API service for the OMDb API with filter support.
 */
object ApiService {
    private const val TAG = "ApiService"

    // Track current search state for pagination
    private var lastSearchFilter: MovieFilter? = null
    private var currentPage = 1

    /**
     * Get movie details by title with enhanced filter options.
     * @param filter The MovieFilter object containing all search parameters
     * @return MovieResponse object containing the movie details
     */
    suspend fun getMovieByFilter(filter: MovieFilter): MovieResponse {
        try {
            Log.d(TAG, "Searching for movie with filter: $filter")

            // Convert filter to API parameters
            val params = filter.toQueryMap()

            // Make API request
            val jsonString = ApiClient.get(params)
            Log.d(TAG, "Received response: $jsonString")

            // Parse response
            return parseMovieResponse(JSONObject(jsonString))
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving movie by filter", e)
            throw e
        }
    }

    /**
     * Search for movies by title with enhanced filter options.
     * @param filter The MovieFilter object containing all search parameters
     * @return SearchResponse object containing a list of search results
     */
    suspend fun searchMoviesByFilter(filter: MovieFilter): SearchResponse {
        try {
            // Save current filter for pagination
            lastSearchFilter = filter
            currentPage = filter.page

            Log.d(TAG, "Searching for movies with filter: $filter (page $currentPage)")

            // Convert filter to API parameters
            val params = filter.toQueryMap()

            // Make API request
            val jsonString = ApiClient.get(params)
            Log.d(TAG, "Received search response: $jsonString")

            // Parse response
            return parseSearchResponse(JSONObject(jsonString))
        } catch (e: Exception) {
            Log.e(TAG, "Error searching movies by filter", e)
            throw e
        }
    }

    /**
     * Parse a JSON object into a MovieResponse.
     * Updated to handle complex data types.
     */
    private fun parseMovieResponse(json: JSONObject): MovieResponse {
        // Parse ratings array
        val ratingsArray = json.optJSONArray("Ratings") ?: JSONArray()
        val ratings = mutableListOf<Rating>()
        for (i in 0 until ratingsArray.length()) {
            val ratingJson = ratingsArray.getJSONObject(i)
            ratings.add(
                Rating(
                    source = ratingJson.optString("Source", ""),
                    value = ratingJson.optString("Value", "")
                )
            )
        }

        // Parse actors, genres, writers, languages and countries as lists
        val actors = json.optString("Actors", "").split(", ").filter { it.isNotEmpty() }
        val genres = json.optString("Genre", "").split(", ").filter { it.isNotEmpty() }
        val writers = json.optString("Writer", "").split(", ").filter { it.isNotEmpty() }
        val languages = json.optString("Language", "").split(", ").filter { it.isNotEmpty() }
        val countries = json.optString("Country", "").split(", ").filter { it.isNotEmpty() }

        // Parse numeric fields with proper conversion
        val yearStr = json.optString("Year", "0")
        val year = try {
            yearStr.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        val metascoreStr = json.optString("Metascore", "0")
        val metascore = if (metascoreStr == "N/A") 0 else try {
            metascoreStr.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        val imdbRatingStr = json.optString("imdbRating", "0")
        val imdbRating = if (imdbRatingStr == "N/A") 0.0f else try {
            imdbRatingStr.toFloat()
        } catch (e: NumberFormatException) {
            0.0f
        }

        return MovieResponse(
            title = json.optString("Title", ""),
            year = year,
            rated = json.optString("Rated", ""),
            released = json.optString("Released", ""),
            runtime = json.optString("Runtime", ""),
            genres = genres,
            director = json.optString("Director", ""),
            writers = writers,
            actors = actors,
            plot = json.optString("Plot", ""),
            languages = languages,
            countries = countries,
            awards = json.optString("Awards", ""),
            poster = json.optString("Poster", ""),
            ratings = ratings,
            metascore = metascore,
            imdbRating = imdbRating,
            imdbVotes = json.optString("imdbVotes", ""),
            imdbID = json.optString("imdbID", ""),
            type = json.optString("Type", ""),
            dvd = json.optString("DVD", "N/A"),
            boxOffice = json.optString("BoxOffice", "N/A"),
            production = json.optString("Production", "N/A"),
            website = json.optString("Website", "N/A"),
            response = json.optString("Response", "")
        )
    }

    /**
     * Parse a JSON object into a SearchResponse.
     */
    private fun parseSearchResponse(json: JSONObject): SearchResponse {
        val response = json.optString("Response", "False")
        val totalResults = json.optString("totalResults", "0")

        if (response == "False") {
            return SearchResponse(emptyList(), "0", "False", currentPage)
        }

        val searchItems = mutableListOf<SearchItem>()
        val searchArray = json.optJSONArray("Search") ?: JSONArray()

        for (i in 0 until searchArray.length()) {
            val item = searchArray.getJSONObject(i)

            // Parse year as integer
            val yearStr = item.optString("Year", "0")
            val year = try {
                yearStr.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            searchItems.add(
                SearchItem(
                    title = item.optString("Title", ""),
                    year = year,
                    imdbID = item.optString("imdbID", ""),
                    type = item.optString("Type", ""),
                    poster = item.optString("Poster", "")
                )
            )
        }

        return SearchResponse(
            search = searchItems,
            totalResults = totalResults,
            response = response,
            page = currentPage
        )
    }
}