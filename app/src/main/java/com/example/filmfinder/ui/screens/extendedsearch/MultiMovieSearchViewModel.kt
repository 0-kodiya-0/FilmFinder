package com.example.filmfinder.ui.screens.extendedsearch

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.local.database.AppDatabase
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.SearchItem
import com.example.filmfinder.data.repository.MovieRepository
import com.example.filmfinder.utils.ImageCacheManager
import com.example.filmfinder.utils.NetworkConnectivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

/**
 * ViewModel for the extended movie search screen with filter support.
 * Handles searching for multiple movies with comprehensive filtering options.
 * Designed to support adding individual search results to the database.
 */
class MultiMovieSearchViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private val appContext = application.applicationContext
    private val TAG = "MultiMovieSearchVM"

    // UI state
    private val _searchResults = MutableStateFlow<List<SearchItem>>(emptyList())
    val searchResults: StateFlow<List<SearchItem>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _canLoadMore = MutableStateFlow(false)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore

    private val _totalResults = MutableStateFlow(0)
    val totalResults: StateFlow<Int> = _totalResults

    // New state for handling movie saving feedback
    private val _movieSavedMessage = MutableStateFlow<String?>(null)
    val movieSavedMessage: StateFlow<String?> = _movieSavedMessage

    // Filter-related state
    private val _currentFilter = MutableStateFlow(MovieFilter())
    val currentFilter: StateFlow<MovieFilter> = _currentFilter

    // Separate filter state for the FilterPanel
    private val _panelFilter = MutableStateFlow(MovieFilter())
    val panelFilter: StateFlow<MovieFilter> = _panelFilter

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
     * Update the current search filter used for actual searching.
     * @param filter The new filter to use
     */
    fun updateFilter(filter: MovieFilter) {
        _currentFilter.value = filter
    }

    /**
     * Update only the filter panel state without affecting the current search.
     * @param filter The new filter to use in the panel
     */
    fun updatePanelFilter(filter: MovieFilter) {
        _panelFilter.value = filter
    }

    /**
     * Toggle the filter panel visibility.
     */
    fun toggleFilterPanel() {
        _isFilterExpanded.value = !_isFilterExpanded.value

        // When opening the panel, initialize panel filter with current filter values
        if (_isFilterExpanded.value) {
            _panelFilter.value = _currentFilter.value.copy()
        }
    }

    /**
     * Apply the panel filter to the current filter and search
     */
    fun applyPanelFilter() {
        // Copy searchTerm from current filter if it's not in panel filter
        val searchTerm = if (_panelFilter.value.searchTerm.isEmpty()) {
            _currentFilter.value.searchTerm
        } else {
            _panelFilter.value.searchTerm
        }

        // Apply panel filter to current filter
        _currentFilter.value = _panelFilter.value.copy(
            searchTerm = searchTerm,
            page = 1 // Reset to page 1 when applying new filters
        )

        // Perform search with new filter
        searchMoviesByFilter()
    }

    /**
     * Search for movies using the current filter.
     */
    fun searchMoviesByFilter() {
        val filter = _currentFilter.value

        if (!_isNetworkAvailable.value) {
            _error.value = "No internet connection available"
            return
        }

        if (filter.searchTerm.isEmpty()) {
            _error.value = "Please enter a search term"
            return
        }

        // Reset pagination when starting a new search with different terms
        if (_currentFilter.value.searchTerm != filter.searchTerm ||
            _currentFilter.value.year != filter.year) {

            _searchResults.value = emptyList()
            updateFilter(filter.copy(page = 1))
        }

        viewModelScope.launch {
            try {
                // Reset states before starting search
                _isLoading.value = true
                _error.value = null
                _hasSearched.value = true

                // Use timeout to prevent hanging requests
                withTimeout(10000) { // 10 second timeout
                    val response = repository.searchMoviesByFilter(_currentFilter.value)

                    if (response.response == "True") {
                        // Update search results
                        if (_currentFilter.value.page > 1) {
                            // Append to existing results for pagination
                            _searchResults.value += response.search
                        } else {
                            // First page - replace results
                            _searchResults.value = response.search
                        }

                        // Update pagination state
                        val total = response.totalResults.toIntOrNull() ?: 0
                        _totalResults.value = total
                        _canLoadMore.value = _searchResults.value.size < total

                    } else {
                        // Handle specific API errors
                        val errorMessage = if (response.response == "False") {
                            if (response.totalResults == "0") {
                                "No movies found matching your search"
                            } else {
                                "Too many results. Please refine your search"
                            }
                        } else {
                            "An error occurred while searching"
                        }

                        _error.value = errorMessage
                        _searchResults.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                // Simplified error message
                _error.value = "Error searching for movies. Please try again."
                _searchResults.value = emptyList()
            } finally {
                // Always reset loading state, even on error or timeout
                _isLoading.value = false
            }
        }
    }

    /**
     * Load more search results (next page).
     * This is called when the user scrolls to the bottom of the list.
     */
    fun loadMoreResults() {
        if (!_canLoadMore.value || _isLoadingMore.value || !_isNetworkAvailable.value) {
            return
        }

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true

                // Increment page number
                val nextPage = _currentFilter.value.page + 1
                updateFilter(_currentFilter.value.copy(page = nextPage))

                val response = repository.searchMoviesByFilter(_currentFilter.value)

                if (response.response == "True") {
                    // Append new items to the list
                    _searchResults.value = _searchResults.value + response.search

                    // Update pagination state
                    val total = response.totalResults.toIntOrNull() ?: 0
                    _totalResults.value = total
                    _canLoadMore.value = _searchResults.value.size < total
                } else {
                    // If we get an error on pagination, just stop loading more
                    _canLoadMore.value = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading more results", e)
                // Don't show error message for pagination failures
                _canLoadMore.value = false
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    /**
     * Save a search result movie to the database.
     * First fetches the full movie details using the IMDb ID, then saves to the database.
     */
    fun addMovieToDatabase(searchItem: SearchItem) {
        if (!_isNetworkAvailable.value) {
            _movieSavedMessage.value = "Error: No internet connection"
            return
        }

        viewModelScope.launch {
            try {
                // Create a filter for getting the full movie details by IMDb ID
                val filter = MovieFilter(imdbID = searchItem.imdbID)

                // Fetch full movie details
                val movieResponse = repository.getMovieByFilter(filter)

                if (movieResponse.response == "True") {
                    // Convert to entity and save
                    val movie = repository.movieResponseToEntity(movieResponse)
                    repository.insertMovie(movie)

                    _movieSavedMessage.value = "\"${searchItem.title}\" added to database"
                } else {
                    _movieSavedMessage.value = "Error: Movie details not found"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding movie to database: ${searchItem.imdbID}", e)
                _movieSavedMessage.value = "Error adding movie to database"
            }
        }
    }

    /**
     * Clear the movie saved message.
     * Called after showing the message to the user.
     */
    fun clearMovieSavedMessage() {
        _movieSavedMessage.value = null
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
        ImageCacheManager.clearScreenCache("extended_search_screen")
    }
}