package com.example.filmfinder.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmfinder.ui.components.FloatingSnackbar
import com.example.filmfinder.utils.isInLandscapeMode

/**
 * Main screen of the application with clean separation of portrait and landscape layouts
 */
@Composable
fun MainScreen(
    onNavigateToMovieSearch: () -> Unit,
    onNavigateToActorSearch: () -> Unit,
    onNavigateToExtendedSearch: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    // State collection
    val isAddingMovies by viewModel.isAddingMovies.collectAsState()
    val addMoviesSuccess by viewModel.addMoviesSuccess.collectAsState()
    var showSnackbar by remember { mutableStateOf(false) }

    // Check orientation
    val isLandscape = isInLandscapeMode()

    // Show success message when movies are added
    LaunchedEffect(addMoviesSuccess) {
        if (addMoviesSuccess) {
            showSnackbar = true
            viewModel.resetAddMoviesSuccess()
        }
    }

    // Define navigation actions
    val buttonActions = listOf(
        ButtonAction("Add Movies to DB", viewModel::addPredefinedMoviesToDb, isAddingMovies),
        ButtonAction("Search for Movies", onNavigateToMovieSearch, false),
        ButtonAction("Search for Actors", onNavigateToActorSearch, false),
        ButtonAction("Extended Movie Search", onNavigateToExtendedSearch, false)
    )

    // Main content without an AppBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Choose layout based on orientation
        if (isLandscape) {
            MainScreenLandscapeLayout(buttonActions)
        } else {
            MainScreenPortraitLayout(buttonActions)
        }

        // Show snackbar when movies are added successfully
        if (showSnackbar) {
            FloatingSnackbar(
                message = "Movies added to database successfully",
                onDismiss = { showSnackbar = false }
            )
        }
    }
}

/**
 * Data class to hold button information and actions
 */
private data class ButtonAction(
    val text: String,
    val onClick: () -> Unit,
    val isLoading: Boolean
)

/**
 * Landscape layout for the main screen with side-by-side title and buttons
 */
@Composable
private fun MainScreenLandscapeLayout(
    buttonActions: List<ButtonAction>
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title and description on the left side
        AppTitleAndDescription(
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp)
        )

        // Buttons in a column on the right side
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MainButtonsList(buttonActions)
        }
    }
}

/**
 * Portrait layout for the main screen with stacked title and buttons
 */
@Composable
private fun MainScreenPortraitLayout(
    buttonActions: List<ButtonAction>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App title and description at the top
        AppTitleAndDescription(
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Buttons in a vertical list
        MainButtonsList(buttonActions)
    }
}

/**
 * Component for displaying the app title and description
 */
@Composable
private fun AppTitleAndDescription(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Film Finder",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Discover and explore your favorite movies",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

/**
 * Component for displaying the main navigation buttons
 */
@Composable
private fun MainButtonsList(
    buttonActions: List<ButtonAction>
) {
    buttonActions.forEachIndexed { index, (text, onClick, loading) ->
        MainNavigationButton(
            text = text,
            onClick = onClick,
            isLoading = loading,
            isPrimary = index == 0
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Individual navigation button component
 */
@Composable
private fun MainNavigationButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    isPrimary: Boolean
) {
    Surface(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium,
        color = if (isPrimary)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface,
        enabled = !isLoading
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isPrimary)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}