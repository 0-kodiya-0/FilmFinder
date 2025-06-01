package com.example.filmfinder.data.repository

import com.example.filmfinder.data.local.dao.MovieDao
import com.example.filmfinder.data.local.entity.Movie
import com.example.filmfinder.data.remote.api.ApiService
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.MovieResponse
import com.example.filmfinder.data.remote.model.SearchResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository for movie data with enhanced filtering options.
 * Handles communication between the data sources (local database and remote API).
 */
class MovieRepository(
    private val movieDao: MovieDao
) {
    /**
     * Insert a movie into the local database.
     */
    suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    /**
     * Insert multiple movies into the local database.
     */
    suspend fun insertMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }

    /**
     * Search for movies by actor name in the local database.
     */
    fun searchMoviesByActor(actorName: String): Flow<List<Movie>> {
        return movieDao.searchMoviesByActor(actorName)
    }

    /**
     * Get movie details using a comprehensive filter.
     * This is the new primary method for searching movies with detailed filtering.
     */
    suspend fun getMovieByFilter(filter: MovieFilter): MovieResponse {
        return ApiService.getMovieByFilter(filter)
    }

    /**
     * Search for movies using a comprehensive filter.
     * This is the new primary method for searching multiple movies with detailed filtering.
     */
    suspend fun searchMoviesByFilter(filter: MovieFilter): SearchResponse {
        return ApiService.searchMoviesByFilter(filter)
    }

    /**
     * Convert a MovieResponse to a Movie entity for database storage.
     */
    fun movieResponseToEntity(response: MovieResponse): Movie {
        return Movie(
            imdbID = response.imdbID,
            title = response.title,
            year = response.year,
            rated = response.rated,
            released = response.released,
            runtime = response.runtime,
            genres = response.genres,
            director = response.director,
            writers = response.writers,
            actors = response.actors,
            plot = response.plot,
            languages = response.languages,
            countries = response.countries,
            awards = response.awards,
            poster = response.poster,
            ratings = response.ratings,
            metascore = response.metascore,
            imdbRating = response.imdbRating,
            imdbVotes = response.imdbVotes,
            type = response.type,
            dvd = response.dvd,
            boxOffice = response.boxOffice,
            production = response.production,
            website = response.website,
            response = response.response
        )
    }
}