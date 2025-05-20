package com.example.filmfinder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.filmfinder.data.local.entity.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Movie entity.
 * Defines database operations for the movies table.
 */
@Dao
interface MovieDao {
    /**
     * Insert a single movie into the database.
     * If a movie with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    /**
     * Insert multiple movies into the database.
     * If any movie with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    /**
     * Search for movies by actor name.
     * Since actors is now a JSON array in the database, we need to use a special query
     * that searches within the JSON string representation.
     * The search is case insensitive and matches partial strings.
     */
    @Query("SELECT * FROM movies WHERE actors LIKE '%' || :actorName || '%' COLLATE NOCASE")
    fun searchMoviesByActor(actorName: String): Flow<List<Movie>>

    /**
     * Get all movies from the database.
     */
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>
}