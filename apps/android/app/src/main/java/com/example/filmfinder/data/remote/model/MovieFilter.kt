package com.example.filmfinder.data.remote.model

/**
 * A filter class for movie searches using the OMDb API.
 * Encapsulates all possible filter parameters supported by the API.
 */
data class MovieFilter(
    // Common search parameters
    val title: String = "",              // Movie title search (t parameter)
    val searchTerm: String = "",         // Search term for multiple results (s parameter)
    val imdbID: String = "",             // Exact IMDb ID (i parameter)

    // Year filters
    val year: Int? = null,               // Specific year of release

    // Response format
    val includeRatings: Boolean = false, // Include Rotten Tomatoes data

    // Request page for search results pagination (1-100)
    val page: Int = 1
) {
    /**
     * Convert this filter to a map of query parameters for the API request.
     */
    fun toQueryMap(): Map<String, String> {
        val params = mutableMapOf<String, String>()

        // Add core search parameters - one of these must be present
        if (imdbID.isNotEmpty()) {
            params["i"] = imdbID
        } else if (title.isNotEmpty()) {
            params["t"] = title
        } else if (searchTerm.isNotEmpty()) {
            params["s"] = searchTerm
        }

        params["type"] = MediaType.MOVIE.toString()

        year?.let { params["y"] = it.toString() }

        if (includeRatings) {
            params["tomatoes"] = "true"
        }

        // Add pagination for search results
        if (searchTerm.isNotEmpty() && page > 1) {
            params["page"] = page.toString()
        }

        return params
    }
}

/**
 * Supported media types for filtering
 */
enum class MediaType(val value: String) {
    MOVIE("movie"),
}