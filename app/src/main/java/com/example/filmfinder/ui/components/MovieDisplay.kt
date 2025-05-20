package com.example.filmfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.local.entity.Movie
import com.example.filmfinder.data.remote.model.MovieResponse

/**
 * A versatile movie display component that can show movie details from either
 * a local database entity (Movie) or a remote API response (MovieResponse).
 * It supports expandable view and optional image loading.
 * Designed to work with the improved image cache system.
 */
@Composable
fun MovieDisplay(
    modifier: Modifier = Modifier,
    movie: Movie? = null,
    movieResponse: MovieResponse? = null,
    expandable: Boolean = true,
    initiallyExpanded: Boolean = false,
    showImage: Boolean = true,
    screenId: String = "default_screen", // Screen ID for image cache tracking
    highlightField: String? = null,
    onClickAction: (() -> Unit)? = null
) {
    // Verify one of the movie sources is provided
    if (movie == null && movieResponse == null) {
        return
    }

    // Local state
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    // Extract movie data from either source
    val title = movie?.title ?: movieResponse?.title ?: ""
    val year = movie?.year?.toString() ?: movieResponse?.year?.toString() ?: ""
    val rated = movie?.rated ?: movieResponse?.rated ?: ""
    val released = movie?.released ?: movieResponse?.released ?: ""
    val runtime = movie?.runtime ?: movieResponse?.runtime ?: ""

    // Handle genre lists
    val genre = when {
        movie?.genres != null -> movie.genres.joinToString(", ")
        movieResponse?.genres != null -> movieResponse.genres.joinToString(", ")
        else -> ""
    }

    // Handle director
    val director = movie?.director ?: movieResponse?.director ?: ""

    // Handle writers list
    val writer = when {
        movie?.writers != null -> movie.writers.joinToString(", ")
        movieResponse?.writers != null -> movieResponse.writers.joinToString(", ")
        else -> ""
    }

    // Handle actors list
    val actors = when {
        movie?.actors != null -> movie.actors.joinToString(", ")
        movieResponse?.actors != null -> movieResponse.actors.joinToString(", ")
        else -> ""
    }

    val plot = movie?.plot ?: movieResponse?.plot ?: ""
    val posterUrl = movie?.poster ?: movieResponse?.poster ?: ""

    Surface(
        onClick = {
            if (expandable) {
                expanded = !expanded
            }
            onClickAction?.invoke()
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Movie title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Basic movie information row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Only show year if it's not empty or zero
                if (year.isNotEmpty() && year != "0") {
                    Text(
                        text = year,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (genre.isNotEmpty()) {
                    val firstGenre = genre.split(", ").firstOrNull() ?: ""
                    if (firstGenre.isNotEmpty()) {
                        Surface(
                            shape = MaterialTheme.shapes.extraSmall,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = firstGenre,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            // Display image if enabled and available
            if (showImage && posterUrl.isNotEmpty() && posterUrl != "N/A") {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CachedImage(
                        url = posterUrl,
                        contentDescription = "$title poster",
                        screenId = screenId
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Highlight a specific field if requested (for actor search etc.)
            if (highlightField != null) {
                when (highlightField) {
                    "actors" -> HighlightField("Actors", actors)
                    "director" -> HighlightField("Director", director)
                    "writer" -> HighlightField("Writer", writer)
                    // Add more highlight options as needed
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            // Always show runtime if available
            if (runtime.isNotEmpty()) {
                InfoField("Runtime", runtime)
            }

            // Expandable content
            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Additional movie details
                InfoField("Rated", rated)
                InfoField("Released", released)
                InfoField("Director", director)
                InfoField("Writer", writer)

                if (highlightField != "actors") {
                    InfoField("Actors", actors)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Movie plot
                if (plot.isNotEmpty()) {
                    Text(
                        text = "Plot",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = plot,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Additional ratings info if available
                val ratings = movie?.ratings ?: movieResponse?.ratings
                if (ratings != null && ratings.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ratings",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    ratings.forEach { rating ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "${rating.source}: ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = rating.value,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // "Tap for details" hint only for expandable cards
            if (expandable && !expanded) {
                Text(
                    text = "Tap for details",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

/**
 * Helper composable for displaying a highlighted field name and value
 */
@Composable
private fun HighlightField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
        )
    }
}

/**
 * Helper composable for displaying field name and value
 */
@Composable
private fun InfoField(label: String, value: String) {
    if (value.isEmpty() || value == "N/A") return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}