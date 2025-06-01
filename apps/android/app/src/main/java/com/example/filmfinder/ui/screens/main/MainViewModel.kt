package com.example.filmfinder.ui.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.local.database.AppDatabase
import com.example.filmfinder.data.repository.MovieRepository
import com.example.filmfinder.data.utils.PredefinedMovies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the main screen.
 * Manages the operation of adding predefined movies to the database.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    // UI state
    private val _isAddingMovies = MutableStateFlow(false)
    val isAddingMovies: StateFlow<Boolean> = _isAddingMovies

    private val _addMoviesSuccess = MutableStateFlow(false)
    val addMoviesSuccess: StateFlow<Boolean> = _addMoviesSuccess

    init {
        // Initialize repository with database
        val movieDao = AppDatabase.getInstance(application).movieDao()
        repository = MovieRepository(movieDao)
    }

    /**
     * Add predefined movies to the local database.
     * This function is called when the user clicks the "Add Movies to DB" button.
     */
    fun addPredefinedMoviesToDb() {
        viewModelScope.launch {
            _isAddingMovies.value = true
            try {
                repository.insertMovies(PredefinedMovies.movies)
                _addMoviesSuccess.value = true
            } finally {
                _isAddingMovies.value = false
            }
        }
    }

    /**
     * Reset the add movies success flag.
     * This is called after showing a success message to the user.
     */
    fun resetAddMoviesSuccess() {
        _addMoviesSuccess.value = false
    }
}