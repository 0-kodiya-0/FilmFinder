package com.example.filmfinder.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmfinder.ui.screens.actorsearch.ActorSearchScreen
import com.example.filmfinder.ui.screens.extendedsearch.ExtendedMovieSearchScreen
import com.example.filmfinder.ui.screens.main.MainScreen
import com.example.filmfinder.ui.screens.moviesearch.MovieSearchScreen

/**
 * Navigation routes for the application
 */
object Routes {
    const val MAIN = "main"
    const val MOVIE_SEARCH = "movie_search"
    const val ACTOR_SEARCH = "actor_search"
    const val EXTENDED_SEARCH = "extended_search"
}

/**
 * Navigation graph for the application.
 * Defines the navigation routes and their corresponding composable destinations.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        // Main screen route
        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToMovieSearch = { navController.navigate(Routes.MOVIE_SEARCH) },
                onNavigateToActorSearch = { navController.navigate(Routes.ACTOR_SEARCH) },
                onNavigateToExtendedSearch = { navController.navigate(Routes.EXTENDED_SEARCH) }
            )
        }

        // Movie search screen route
        composable(Routes.MOVIE_SEARCH) {
            MovieSearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Actor search screen route
        composable(Routes.ACTOR_SEARCH) {
            ActorSearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Extended movie search screen route
        composable(Routes.EXTENDED_SEARCH) {
            ExtendedMovieSearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}