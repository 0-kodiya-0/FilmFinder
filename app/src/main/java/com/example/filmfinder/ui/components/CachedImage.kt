package com.example.filmfinder.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.filmfinder.utils.ImageCacheManager

/**
 * A composable that displays an image loaded from a URL with caching.
 * The image will only be loaded once and will be cached for future use.
 * Designed to handle cache clearing when component is disposed.
 */
@Composable
fun CachedImage(
    url: String,
    contentDescription: String,
    screenId: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderText: String = "No Image"
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf(false) }

    // Using LaunchedEffect with the URL as the key ensures that
    // loading only happens when the URL changes, not on every recomposition
    LaunchedEffect(key1 = url) {
        if (url.isEmpty() || url == "N/A") {
            isLoading = false
            loadError = true
            return@LaunchedEffect
        }

        isLoading = true
        loadError = false

        // Load from cache or network using our manager
        val result = ImageCacheManager.loadImage(url, screenId)

        bitmap = result
        isLoading = false
        loadError = (result == null)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                // Show a loading indicator
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            bitmap != null -> {
                // Show the loaded bitmap
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                // Show an error placeholder
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(1.dp, MaterialTheme.shapes.small),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}