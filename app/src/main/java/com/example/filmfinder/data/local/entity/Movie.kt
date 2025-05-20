package com.example.filmfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.filmfinder.data.local.converters.RatingConverter
import com.example.filmfinder.data.local.converters.StringListConverter

/**
 * Entity class representing a movie in the local database.
 * Contains detailed information about a movie retrieved from the OMDb API.
 * With proper type handling for arrays, dates, and numbers.
 */
@Entity(tableName = "movies")
@TypeConverters(RatingConverter::class, StringListConverter::class)
data class Movie(
    @PrimaryKey
    val imdbID: String,
    val title: String,
    val year: Int,
    val rated: String,
    val released: String,
    val runtime: String,
    val genres: List<String> = emptyList(),
    val director: String,
    val writers: List<String> = emptyList(),
    val actors: List<String> = emptyList(),
    val plot: String,
    val languages: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val awards: String,
    val poster: String,
    val ratings: List<Rating> = emptyList(),
    val metascore: Int = 0,
    val imdbRating: Float = 0.0f,
    val imdbVotes: String,
    val type: String,
    val dvd: String = "N/A",
    val boxOffice: String = "N/A",
    val production: String = "N/A",
    val website: String = "N/A",
    val response: String = "True"
)

/**
 * Data class representing a movie rating from a specific source.
 */
data class Rating(
    val source: String,
    val value: String
)