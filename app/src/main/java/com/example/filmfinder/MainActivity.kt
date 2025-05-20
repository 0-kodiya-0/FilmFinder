package com.example.filmfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.filmfinder.ui.navigation.NavGraph
import com.example.filmfinder.ui.theme.FilmFinderTheme

/**
 * Main entry point for the Film Finder application.
 * Combines the functionality of both MainActivity and MovieApp into a single file.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmFinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FilmFinderApp()
                }
            }
        }
    }
}

/**
 * Main composable function for the application.
 * Sets up the navigation controller and navigation graph.
 */
@Composable
fun FilmFinderApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}