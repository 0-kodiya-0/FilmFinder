package com.example.filmfinder.ui.screens.moviesearch

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.local.database.AppDatabase
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.MovieResponse
import com.example.filmfinder.data.repository.MovieRepository
import com.example.filmfinder.utils.ImageCacheManager
import com.example.filmfinder.utils.NetworkConnectivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for the movie search screen with filter support.
 * Handles searching for movies by title with various filter options.
 */
class MovieSearchViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private val appContext = application.applicationContext
    private val TAG = "MovieSearchViewModel"

    // UI state
    private val _movieResponse = MutableStateFlow<MovieResponse?>(null)
    val movieResponse: StateFlow<MovieResponse?> = _movieResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _movieSaved = MutableStateFlow(false)
    val movieSaved: StateFlow<Boolean> = _movieSaved

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable

    // New filter-related state
    private val _currentFilter = MutableStateFlow(MovieFilter())
    val currentFilter: StateFlow<MovieFilter> = _currentFilter

    private val _isFilterExpanded = MutableStateFlow(false)
    val isFilterExpanded: StateFlow<Boolean> = _isFilterExpanded

    init {
        // Initialize repository with database and API service
        val movieDao = AppDatabase.getInstance(application).movieDao()
        repository = MovieRepository(movieDao)

        // Check network connectivity on startup
        checkNetworkConnectivity()

        // Observe network changes
        viewModelScope.launch {
            NetworkConnectivity.observeNetworkConnectivity(appContext).collectLatest { isConnected ->
                _isNetworkAvailable.value = isConnected
            }
        }
    }

    /**
     * Update the current search filter.
     * @param filter The new filter to use
     */
    fun updateFilter(filter: MovieFilter) {
        _currentFilter.value = filter
    }

    /**
     * Toggle the filter panel visibility.
     */
    fun toggleFilterPanel() {
        _isFilterExpanded.value = !_isFilterExpanded.value
    }

    /**
     * Search for a movie by title using the current filter.
     */
    fun searchMovieByFilter() {
        val filter = _currentFilter.value

        if (!_isNetworkAvailable.value) {
            _error.value = "No internet connection available"
            return
        }

        if (filter.title.isEmpty() && filter.imdbID.isEmpty()) {
            _error.value = "Please enter a movie title or IMDb ID"
            return
        }

        // Clear previous results and errors
        _movieResponse.value = null
        _error.value = null

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getMovieByFilter(filter)
                _movieResponse.value = response

                // Check if the response indicates an error
                if (response.response == "False") {
                    _error.value = "Movie not found"
                    _movieResponse.value = null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching for movie", e)

                // Simplified error handling
                _error.value = "Error searching for movie. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Save the currently displayed movie to the database.
     */
    fun saveMovieToDatabase() {
        viewModelScope.launch {
            _movieResponse.value?.let { response ->
                if (response.response == "True") {
                    val movie = repository.movieResponseToEntity(response)
                    repository.insertMovie(movie)
                    _movieSaved.value = true
                }
            }
        }
    }

    /**
     * Reset the movie saved flag.
     * This is called after showing a success message to the user.
     */
    fun resetMovieSaved() {
        _movieSaved.value = false
    }

    /**
     * Check if the device has a network connection.
     */
    fun checkNetworkConnectivity() {
        _isNetworkAvailable.value = NetworkConnectivity.isNetworkAvailable(appContext)
    }

    /**
     * Clean up resources when ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        // Clear the image cache for this screen
        ImageCacheManager.clearScreenCache("movie_search_screen")
    }
}