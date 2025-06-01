package com.example.filmfinder.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

/**
 * Simple utility to check if the current orientation is landscape
 */
@Composable
fun isInLandscapeMode(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}