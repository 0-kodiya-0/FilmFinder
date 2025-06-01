package com.example.filmfinder.ui.screens.actorsearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.filmfinder.data.local.entity.Movie
import com.example.filmfinder.ui.components.*
import com.example.filmfinder.utils.ImageCacheManager
import com.example.filmfinder.utils.isInLandscapeMode
import com.example.filmfinder.data.remote.model.ActorSearchFilter

/**
 * ActorSearchScreen with filtering capabilities using a separate filter component
 */
@Composable
fun ActorSearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: ActorSearchViewModel = viewModel()
) {
    // State collection
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val isFilterExpanded by viewModel.isFilterExpanded.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Check orientation
    val isLandscape = isInLandscapeMode()

    Scaffold(
        topBar = {
            AppBar(
                title = "Actor Search",
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
            // Choose layout based on orientation
            if (isLandscape) {
                LandscapeLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSearch = {
                        if (searchQuery.isEmpty()) {
                            snackbarMessage = "Please enter an actor name"
                            showSnackbar = true
                        } else {
                            viewModel.searchMoviesByActor(searchQuery)
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    filter = filter,
                    onFilterChange = { viewModel.updateFilter(it) },
                    onApplyFilter = { viewModel.applyFilter() },
                    isLoading = isLoading,
                    hasSearched = hasSearched,
                    movies = movies,
                    actorName = searchQuery
                )
            } else {
                PortraitLayout(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSearch = {
                        if (searchQuery.isEmpty()) {
                            snackbarMessage = "Please enter an actor name"
                            showSnackbar = true
                        } else {
                            viewModel.searchMoviesByActor(searchQuery)
                        }
                    },
                    isFilterExpanded = isFilterExpanded,
                    filter = filter,
                    onFilterChange = { viewModel.updateFilter(it) },
                    onApplyFilter = { viewModel.applyFilter() },
                    isLoading = isLoading,
                    hasSearched = hasSearched,
                    movies = movies,
                    actorName = searchQuery
                )
            }

            // Show snackbar for validation messages
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
 * Landscape layout for the actor search screen with side-by-side layout
 */
@Composable
private fun LandscapeLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    filter: ActorSearchFilter,
    onFilterChange: (ActorSearchFilter) -> Unit,
    onApplyFilter: () -> Unit,
    isLoading: Boolean,
    hasSearched: Boolean,
    movies: List<Movie>,
    actorName: String
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
            // Search bar for actor name
            SearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                onSearch = onSearch,
                placeholder = "Enter actor name"
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = isFilterExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ActorSearchFilterPanel(
                    filter = filter,
                    onFilterChange = onFilterChange,
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
            SearchResultsContent(
                isLoading = isLoading,
                hasSearched = hasSearched,
                movies = movies,
                actorName = actorName
            )
        }
    }
}

/**
 * Portrait layout for the actor search screen with stacked layout
 */
@Composable
private fun PortraitLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFilterExpanded: Boolean,
    filter: ActorSearchFilter,
    onFilterChange: (ActorSearchFilter) -> Unit,
    onApplyFilter: () -> Unit,
    isLoading: Boolean,
    hasSearched: Boolean,
    movies: List<Movie>,
    actorName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search bar for actor name
        SearchBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            onSearch = onSearch,
            placeholder = "Enter actor name"
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = isFilterExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ActorSearchFilterPanel(
                filter = filter,
                onFilterChange = onFilterChange,
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
            SearchResultsContent(
                isLoading = isLoading,
                hasSearched = hasSearched,
                movies = movies,
                actorName = actorName
            )
        }
    }
}

/**
 * Content area showing search results
 */
@Composable
private fun SearchResultsContent(
    isLoading: Boolean,
    hasSearched: Boolean,
    movies: List<Movie>,
    actorName: String
) {
    val listState = rememberLazyListState()

    when {
        isLoading -> {
            LoadingIndicator()
        }
        hasSearched && movies.isEmpty() -> {
            // Show message when no movies are found
            ErrorMessage("No movies found with this actor")
        }
        hasSearched && movies.isNotEmpty() -> {
            // Display list of movies with the actor
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Text(
                        text = "Found ${movies.size} movie(s) with \"$actorName\"",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                    )
                }

                items(movies) { movie ->
                    MovieDisplay(
                        movie = movie,
                        expandable = true,
                        initiallyExpanded = false,
                        showImage = true,
                        screenId = "actor_search_screen",
                        highlightField = "actors"
                    )
                }
            }
        }
        else -> {
            // Initial state - show instructions
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Search for an Actor",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Enter an actor's name to find movies they've appeared in",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Use filters to refine your search by genre, year, and rating",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}