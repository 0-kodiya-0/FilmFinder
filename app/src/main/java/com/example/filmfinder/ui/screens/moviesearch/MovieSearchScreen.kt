package com.example.filmfinder.ui.screens.moviesearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.MovieResponse
import com.example.filmfinder.ui.components.*
import com.example.filmfinder.utils.isInLandscapeMode

/**
 * Main Movie Search Screen
 */
@Composable
fun MovieSearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: MovieSearchViewModel = viewModel()
) {
    // State collection
    val movieResponse by viewModel.movieResponse.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val movieSaved by viewModel.movieSaved.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val filter by viewModel.currentFilter.collectAsState()
    val isFilterExpanded by viewModel.isFilterExpanded.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Check orientation
    val isLandscape = isInLandscapeMode()

    // Update search query from filter when changed
    LaunchedEffect(filter.title) {
        if (filter.title.isNotEmpty() && searchQuery != filter.title) {
            searchQuery = filter.title
        }
    }

    // Show success message when a movie is saved
    LaunchedEffect(movieSaved) {
        if (movieSaved) {
            snackbarMessage = "Movie saved to database successfully"
            showSnackbar = true
            viewModel.resetMovieSaved()
        }
    }

    // Show network error screen if no connectivity
    if (!isNetworkAvailable) {
        NetworkErrorScreen(
            onRetry = { viewModel.checkNetworkConnectivity() },
            onNavigateBack = onNavigateBack
        )
        return
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "Movie Search",
                onBackClick = onNavigateBack,
                actions = {
                    // Filter toggle button
                    IconButton(onClick = { viewModel.toggleFilterPanel() }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Toggle Filters",
                            tint = if (isFilterExpanded)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Save button - only visible when a movie is found
                    if (movieResponse != null && movieResponse?.response == "True") {
                        IconButton(onClick = { viewModel.saveMovieToDatabase() }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Save to Database",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Choose layout based on orientation
            if (isLandscape) {
                MovieSearchLandscapeLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        viewModel.updateFilter(filter.copy(title = it))
                    },
                    onSearch = {
                        if (searchQuery.isEmpty()) {
                            snackbarMessage = "Please enter a movie title"
                            showSnackbar = true
                        } else {
                            viewModel.searchMovieByFilter()
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    filter = filter,
                    onFilterChanged = { viewModel.updateFilter(it) },
                    onApplyFilter = { viewModel.searchMovieByFilter() },
                    isLoading = isLoading,
                    error = error,
                    movieResponse = movieResponse,
                    onRetry = { viewModel.searchMovieByFilter() }
                )
            } else {
                MovieSearchPortraitLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        viewModel.updateFilter(filter.copy(title = it))
                    },
                    onSearch = {
                        if (searchQuery.isEmpty()) {
                            snackbarMessage = "Please enter a movie title"
                            showSnackbar = true
                        } else {
                            viewModel.searchMovieByFilter()
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    filter = filter,
                    onFilterChanged = { viewModel.updateFilter(it) },
                    onApplyFilter = { viewModel.searchMovieByFilter() },
                    isLoading = isLoading,
                    error = error,
                    movieResponse = movieResponse,
                    onRetry = { viewModel.searchMovieByFilter() }
                )
            }

            // Show snackbar for messages (shared between layouts)
            if (showSnackbar) {
                FloatingSnackbar(
                    message = snackbarMessage,
                    onDismiss = { showSnackbar = false }
                )
            }
        }
    }
}

/**
 * Landscape layout for the movie search screen - shows controls and content side by side
 */
@Composable
private fun MovieSearchLandscapeLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    filter: MovieFilter,
    onFilterChanged: (MovieFilter) -> Unit,
    onApplyFilter: () -> Unit,
    isLoading: Boolean,
    error: String?,
    movieResponse: MovieResponse?,
    onRetry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Search bar
            SearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onSearch = onSearch,
                placeholder = "Enter movie title"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter panel
            AnimatedVisibility(
                visible = isFilterExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FilterPanel(
                    currentFilter = filter,
                    onFilterChanged = onFilterChanged,
                    onApplyFilter = onApplyFilter
                )
            }

            // Add extra space at bottom to ensure scrollability
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp)
                .fillMaxHeight()
        ) {
            MovieSearchContent(
                isLoading = isLoading,
                error = error,
                movieResponse = movieResponse,
                searchQuery = searchQuery,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Portrait layout for the movie search screen - stacks controls and content vertically
 */
@Composable
private fun MovieSearchPortraitLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    filter: MovieFilter,
    onFilterChanged: (MovieFilter) -> Unit,
    onApplyFilter: () -> Unit,
    isLoading: Boolean,
    error: String?,
    movieResponse: MovieResponse?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search controls in a scrollable container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Search bar
            SearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onSearch = onSearch,
                placeholder = "Enter movie title"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter panel
            AnimatedVisibility(
                visible = isFilterExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FilterPanel(
                    currentFilter = filter,
                    onFilterChanged = onFilterChanged,
                    onApplyFilter = onApplyFilter
                )
            }

            // Add extra space at bottom to ensure scrollability
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Content area in a separate box to allow independent scrolling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MovieSearchContent(
                isLoading = isLoading,
                error = error,
                movieResponse = movieResponse,
                searchQuery = searchQuery,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Content area for the movie search screen - shared between layouts
 */
@Composable
private fun MovieSearchContent(
    isLoading: Boolean,
    error: String?,
    movieResponse: MovieResponse?,
    searchQuery: String,
    onRetry: () -> Unit
) {
    when {
        isLoading -> {
            LoadingIndicator()
        }
        error != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorMessage(error)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRetry,
                    enabled = searchQuery.isNotEmpty()
                ) {
                    Text("Try Again")
                }
            }
        }
        movieResponse != null -> {
            if (movieResponse.response == "True") {
                // Scrollable container for movie details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Movie display with expanded details
                    MovieDisplay(
                        movieResponse = movieResponse,
                        expandable = false,
                        initiallyExpanded = true,
                        showImage = true,
                        screenId = "movie_search_screen"
                    )
                }
            } else {
                ErrorMessage("Movie not found")
            }
        }
        else -> {
            // Initial empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Search for a Movie",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Enter a movie title in the search bar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}