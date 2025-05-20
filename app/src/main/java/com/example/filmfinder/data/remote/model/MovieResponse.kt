package com.example.filmfinder.data.remote.model

import com.example.filmfinder.data.local.entity.Rating
import java.io.Serializable

/**
 * Data class representing a movie response from the OMDb API.
 */
data class MovieResponse(
    val title: String,
    val year: Int,
    val rated: String,
    val released: String,
    val runtime: String,
    val genres: List<String>,
    val director: String,
    val writers: List<String>,
    val actors: List<String>,
    val plot: String,
    val languages: List<String>,
    val countries: List<String>,
    val awards: String,
    val poster: String,
    val ratings: List<Rating>,
    val metascore: Int,
    val imdbRating: Float,
    val imdbVotes: String,
    val imdbID: String,
    val type: String,
    val dvd: String,
    val boxOffice: String,
    val production: String,
    val website: String,
    val response: String
) : Serializable