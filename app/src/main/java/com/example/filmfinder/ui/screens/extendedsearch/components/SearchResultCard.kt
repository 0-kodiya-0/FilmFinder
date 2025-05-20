package com.example.filmfinder.ui.screens.extendedsearch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.remote.model.SearchItem
import com.example.filmfinder.ui.components.CachedImage

/**
 * Search result card with image and add to database button
 * This component is optimized for the lightweight search results
 * Designed to work with the improved image cache system and to include an add button
 */
@Composable
fun SearchResultCard(
    movie: SearchItem,
    onAddToDatabase: (SearchItem) -> Unit,
    screenId: String = "extended_search_screen" // Screen ID for image cache tracking
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Always show an image area - either with image or placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(2.dp, MaterialTheme.shapes.small)
            ) {
                if (movie.poster.isNotEmpty() && movie.poster != "N/A") {
                    CachedImage(
                        url = movie.poster,
                        contentDescription = "${movie.title} poster",
                        screenId = screenId,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Placeholder when no image URL is available
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = movie.title.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Main content column
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Movie title
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Year and type badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = movie.year.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = movie.type.capitalize(),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // IMDb ID
                Text(
                    text = "IMDb: ${movie.imdbID}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Add to DB button
            Button(
                onClick = { onAddToDatabase(movie) },
                modifier = Modifier.padding(start = 8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add to Database",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add to DB")
            }
        }
    }
}

// Helper extension to capitalize the first letter of a string
private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}