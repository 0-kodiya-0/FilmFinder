package com.example.filmfinder.data.remote.model

/**
 * Data class representing a search response from the OMDb API.
 * Contains a list of search items and metadata.
 */
data class SearchResponse(
    val search: List<SearchItem>,
    val totalResults: String,
    val response: String,
    val page: Int = 1
)

/**
 * Data class representing an individual search result in the search response.
 * Contains basic information about a movie.
 */
data class SearchItem(
    val title: String,
    val year: Int,
    val imdbID: String,
    val type: String,
    val poster: String
)