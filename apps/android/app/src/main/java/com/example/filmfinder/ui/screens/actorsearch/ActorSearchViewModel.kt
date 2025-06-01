package com.example.filmfinder.ui.screens.actorsearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.local.database.AppDatabase
import com.example.filmfinder.data.local.entity.Movie
import com.example.filmfinder.data.remote.model.ActorSearchFilter
import com.example.filmfinder.data.repository.MovieRepository
import com.example.filmfinder.utils.ImageCacheManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the actor search screen with filtering capabilities.
 * Handles searching for movies by actor name in the local database with additional filtering options.
 */
class ActorSearchViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: MovieRepository

    // UI state
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched

    // Filter state
    private val _filter = MutableStateFlow(ActorSearchFilter())
    val filter: StateFlow<ActorSearchFilter> = _filter

    private val _isFilterExpanded = MutableStateFlow(false)
    val isFilterExpanded: StateFlow<Boolean> = _isFilterExpanded

    // Store the latest search query
    private var currentActorQuery = ""

    init {
        // Initialize repository with database
        val movieDao = AppDatabase.getInstance(application).movieDao()
        repository = MovieRepository(movieDao)
    }

    /**
     * Toggle the filter panel visibility.
     */
    fun toggleFilterPanel() {
        _isFilterExpanded.value = !_isFilterExpanded.value
    }

    /**
     * Update the current search filter.
     * @param newFilter The new filter to use
     */
    fun updateFilter(newFilter: ActorSearchFilter) {
        _filter.value = newFilter
    }

    /**
     * Apply the current filter to the search results.
     * This re-runs the search with the current actor name and applies any filters.
     */
    fun applyFilter() {
        if (currentActorQuery.isNotEmpty()) {
            searchMoviesByActor(currentActorQuery, false)
        }
    }

    /**
     * Search for movies by actor name in the local database.
     * The search is case insensitive and matches substrings.
     *
     * @param actorName The name of the actor to search for
     * @param resetFilter Whether to reset the filter to default values
     */
    fun searchMoviesByActor(actorName: String, resetFilter: Boolean = true) {
        // Store current search query for filter reapplication
        currentActorQuery = actorName

        // Reset filter if requested (typically on new searches)
        if (resetFilter) {
            _filter.value = ActorSearchFilter()
        }

        // Clear previous search results when starting a new search
        _movies.value = emptyList()

        if (actorName.isEmpty()) {
            _hasSearched.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _hasSearched.value = true

            // Get current filter values
            val currentFilter = _filter.value

            repository.searchMoviesByActor(actorName).map { movies ->
                // Apply filters to the search results
                movies.filter { movie ->
                    var passes = true

                    // Apply genre filter if set
                    if (currentFilter.genre.isNotEmpty()) {
                        passes = passes && movie.genres.any {
                            it.contains(currentFilter.genre, ignoreCase = true)
                        }
                    }

                    // Apply year range filter if set
                    currentFilter.fromYear?.let { fromYear ->
                        passes = passes && movie.year >= fromYear
                    }

                    currentFilter.toYear?.let { toYear ->
                        passes = passes && movie.year <= toYear
                    }

                    // Apply rating filter if set
                    if (currentFilter.highRatedOnly) {
                        passes = passes && movie.imdbRating >= 7.0f
                    }

                    passes
                }
            }.collect { filteredMovies ->
                _movies.value = filteredMovies
                _isLoading.value = false
            }
        }
    }

    /**
     * Clean up resources when ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        // Clear the image cache for this screen
        ImageCacheManager.clearScreenCache("actor_search_screen")
    }
}