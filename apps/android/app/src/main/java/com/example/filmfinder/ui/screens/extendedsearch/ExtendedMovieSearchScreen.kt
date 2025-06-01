package com.example.filmfinder.ui.screens.extendedsearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmfinder.data.remote.model.MovieFilter
import com.example.filmfinder.data.remote.model.SearchItem
import com.example.filmfinder.ui.components.*
import com.example.filmfinder.ui.screens.extendedsearch.components.SearchResultCard
import com.example.filmfinder.utils.ImageCacheManager
import com.example.filmfinder.utils.isInLandscapeMode
import kotlinx.coroutines.delay

/**
 * ExtendedMovieSearchScreen allows searching for multiple movies with filter options
 */
@Composable
fun ExtendedMovieSearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: MultiMovieSearchViewModel = viewModel()
) {
    // State collection
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val canLoadMore by viewModel.canLoadMore.collectAsState()
    val totalResults by viewModel.totalResults.collectAsState()
    val filter by viewModel.currentFilter.collectAsState()
    val panelFilter by viewModel.panelFilter.collectAsState()
    val isFilterExpanded by viewModel.isFilterExpanded.collectAsState()
    val movieSavedMessage by viewModel.movieSavedMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // Check orientation
    val isLandscape = isInLandscapeMode()

    // Update search query from filter when changed
    LaunchedEffect(filter.searchTerm) {
        if (filter.searchTerm.isNotEmpty() && searchQuery != filter.searchTerm) {
            searchQuery = filter.searchTerm
        }
    }

    // Show snackbar message when a movie is saved to database
    LaunchedEffect(movieSavedMessage) {
        if (movieSavedMessage != null) {
            snackbarMessage = movieSavedMessage as String
            showSnackbar = true
            // Clear the message after showing
            viewModel.clearMovieSavedMessage()
        }
    }

    // Handle pagination with LaunchedEffect
    LaunchedEffect(listState) {
        snapshotFlow {
            if (listState.layoutInfo.totalItemsCount == 0) return@snapshotFlow false

            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val lastIndex = listState.layoutInfo.totalItemsCount - 1

            lastVisibleItem != null && lastVisibleItem.index >= lastIndex - 3 // Load more when 3 items from end
        }.collect { shouldLoadMore ->
            if (shouldLoadMore && canLoadMore && !isLoadingMore && !isLoading) {
                // Small delay to prevent multiple rapid requests
                delay(300)
                viewModel.loadMoreResults()
            }
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
                title = "Extended Movie Search",
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
            // Select layout based on orientation
            if (isLandscape) {
                LandscapeLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        viewModel.updateFilter(filter.copy(searchTerm = it))
                    },
                    onSearch = {
                        if (searchQuery.length < 3) {
                            snackbarMessage = "Please enter at least 3 characters"
                            showSnackbar = true
                        } else {
                            viewModel.searchMoviesByFilter()
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    panelFilter = panelFilter,
                    onFilterChange = { viewModel.updatePanelFilter(it) },
                    onApplyFilter = { viewModel.applyPanelFilter() },
                    listState = listState,
                    searchResults = searchResults,
                    isLoading = isLoading,
                    isLoadingMore = isLoadingMore,
                    error = error,
                    hasSearched = hasSearched,
                    totalResults = totalResults,
                    canLoadMore = canLoadMore,
                    onAddToDatabase = { viewModel.addMovieToDatabase(it) },
                    onRetry = { viewModel.searchMoviesByFilter() }
                )
            } else {
                PortraitLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        viewModel.updateFilter(filter.copy(searchTerm = it))
                    },
                    onSearch = {
                        if (searchQuery.length < 3) {
                            snackbarMessage = "Please enter at least 3 characters"
                            showSnackbar = true
                        } else {
                            viewModel.searchMoviesByFilter()
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    panelFilter = panelFilter,
                    onFilterChange = { viewModel.updatePanelFilter(it) },
                    onApplyFilter = { viewModel.applyPanelFilter() },
                    listState = listState,
                    searchResults = searchResults,
                    isLoading = isLoading,
                    isLoadingMore = isLoadingMore,
                    error = error,
                    hasSearched = hasSearched,
                    totalResults = totalResults,
                    canLoadMore = canLoadMore,
                    onAddToDatabase = { viewModel.addMovieToDatabase(it) },
                    onRetry = { viewModel.searchMoviesByFilter() }
                )
            }

            // Show snackbar for messages
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
 * Landscape layout for the extended movie search screen with side-by-side panels
 */
@Composable
private fun LandscapeLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    panelFilter: MovieFilter,
    onFilterChange: (MovieFilter) -> Unit,
    onApplyFilter: () -> Unit,
    listState: LazyListState,
    searchResults: List<SearchItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    error: String?,
    hasSearched: Boolean,
    totalResults: Int,
    canLoadMore: Boolean,
    onAddToDatabase: (SearchItem) -> Unit,
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
                placeholder = "Enter movie title (min. 3 characters)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter panel
            AnimatedVisibility(
                visible = isFilterExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FilterPanel(
                    currentFilter = panelFilter,
                    onFilterChanged = onFilterChange,
                    onApplyFilter = onApplyFilter
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp)
                .fillMaxHeight()
        ) {
            MovieSearchContent(
                listState = listState,
                searchResults = searchResults,
                isLoading = isLoading,
                isLoadingMore = isLoadingMore,
                error = error,
                hasSearched = hasSearched,
                totalResults = totalResults,
                canLoadMore = canLoadMore,
                onAddToDatabase = onAddToDatabase,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Portrait layout for the extended movie search screen with stacked elements
 */
@Composable
private fun PortraitLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    panelFilter: MovieFilter,
    onFilterChange: (MovieFilter) -> Unit,
    onApplyFilter: () -> Unit,
    listState: LazyListState,
    searchResults: List<SearchItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    error: String?,
    hasSearched: Boolean,
    totalResults: Int,
    canLoadMore: Boolean,
    onAddToDatabase: (SearchItem) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search bar
        SearchBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            onSearch = onSearch,
            placeholder = "Enter movie title (min. 3 characters)"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter panel
        AnimatedVisibility(
            visible = isFilterExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            FilterPanel(
                currentFilter = panelFilter,
                onFilterChanged = onFilterChange,
                onApplyFilter = onApplyFilter
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MovieSearchContent(
                listState = listState,
                searchResults = searchResults,
                isLoading = isLoading,
                isLoadingMore = isLoadingMore,
                error = error,
                hasSearched = hasSearched,
                totalResults = totalResults,
                canLoadMore = canLoadMore,
                onAddToDatabase = onAddToDatabase,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Shared content component for displaying search results, loading, and error states
 */
@Composable
private fun MovieSearchContent(
    listState: LazyListState,
    searchResults: List<SearchItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    error: String?,
    hasSearched: Boolean,
    totalResults: Int,
    canLoadMore: Boolean,
    onAddToDatabase: (SearchItem) -> Unit,
    onRetry: () -> Unit
) {
    when {
        isLoading && !isLoadingMore -> {
            LoadingIndicator()
        }
        error != null && hasSearched -> {
            ErrorWithRetry(
                errorMessage = error,
                onRetry = onRetry
            )
        }
        hasSearched && searchResults.isNotEmpty() -> {
            SearchResultsList(
                listState = listState,
                searchResults = searchResults,
                totalResults = totalResults,
                isLoadingMore = isLoadingMore,
                canLoadMore = canLoadMore,
                onAddToDatabase = onAddToDatabase
            )
        }
        hasSearched -> {
            ErrorMessage("No movies found matching your search")
        }
        else -> {
            WelcomeMessage()
        }
    }
}

/**
 * Error display with retry button and report option
 */
@Composable
private fun ErrorWithRetry(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Try Again")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { /* Non-functional report button */ }
        ) {
            Text("Report Issue")
        }
    }
}

/**
 * List display for search results with pagination support
 */
@Composable
private fun SearchResultsList(
    listState: LazyListState,
    searchResults: List<SearchItem>,
    totalResults: Int,
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    onAddToDatabase: (SearchItem) -> Unit
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            Text(
                text = "Found $totalResults movies (showing ${searchResults.size})",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
            )
        }

        items(
            items = searchResults,
            key = { it.imdbID }
        ) { movie ->
            SearchResultCard(
                movie = movie,
                onAddToDatabase = onAddToDatabase,
                screenId = "extended_search_screen"
            )
        }

        // Loading indicator for pagination
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }

        // End of results message
        if (!canLoadMore && !isLoadingMore && searchResults.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "End of results",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Initial welcome message for the search screen
 */
@Composable
private fun WelcomeMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Extended Movie Search",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Search for multiple movies by title from the OMDb database",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Use filters to refine your search by year, type, and more",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}