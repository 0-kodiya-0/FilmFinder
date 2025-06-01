package com.example.filmfinder.data.remote.model

/**
 * Data class representing filter options for actor search.
 * Contains all the parameters that can be used to filter movie results
 * when searching by actor name.
 */
data class ActorSearchFilter(
    val genre: String = "",          // Filter by movie genre
    val fromYear: Int? = null,       // Filter movies released after this year
    val toYear: Int? = null,         // Filter movies released before this year
    val highRatedOnly: Boolean = false  // Show only movies with IMDb rating >= 7.0
)