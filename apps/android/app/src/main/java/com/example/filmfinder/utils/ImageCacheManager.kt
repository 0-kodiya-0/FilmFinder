package com.example.filmfinder.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * A simple image cache manager that stores loaded bitmaps in memory.
 * This helps prevent reloading images when scrolling in lists.
 * Designed to handle cache clearing when leaving screens.
 */
object ImageCacheManager {
    private const val TAG = "ImageCacheManager"

    // Calculate cache size based on available memory (1/8th of available memory)
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8

    // Create LruCache to store and manage bitmaps
    private val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            // Return the size of the bitmap in kilobytes
            return bitmap.byteCount / 1024
        }
    }

    // Map to track which screen is using which images
    private val screenCacheMap = mutableMapOf<String, MutableSet<String>>()

    /**
     * Load an image from the cache or download it if not available.
     * @param url URL of the image to load
     * @param screenId Identifier for the screen loading the image
     * @return Loaded bitmap or null if loading failed
     */
    suspend fun loadImage(url: String, screenId: String): Bitmap? {
        // Register this URL with the screen ID
        registerImageWithScreen(url, screenId)

        // First check the cache
        val cachedBitmap = getBitmapFromCache(url)
        if (cachedBitmap != null) {
            return cachedBitmap
        }

        // If not in cache, download the image
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 3000 // 3 seconds
                connection.readTimeout = 3000    // 3 seconds
                connection.useCaches = true      // Use HTTP caching if available

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                // Clean up resources
                inputStream.close()
                connection.disconnect()

                // Store in cache if loading succeeded
                if (bitmap != null) {
                    addBitmapToCache(url, bitmap)
                }

                bitmap
            } catch (e: IOException) {
                Log.e(TAG, "Error loading image: $url", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error loading image: $url", e)
                null
            }
        }
    }

    /**
     * Get a bitmap from the memory cache.
     * @param url The URL used as the cache key
     * @return The cached bitmap or null if not found
     */
    private fun getBitmapFromCache(url: String): Bitmap? {
        return memoryCache.get(url)
    }

    /**
     * Add a bitmap to the memory cache.
     * @param url The URL used as the cache key
     * @param bitmap The bitmap to cache
     */
    private fun addBitmapToCache(url: String, bitmap: Bitmap) {
        if (getBitmapFromCache(url) == null) {
            memoryCache.put(url, bitmap)
        }
    }

    /**
     * Register an image URL with a specific screen.
     * This helps track which images are used by which screens.
     */
    private fun registerImageWithScreen(url: String, screenId: String) {
        if (!screenCacheMap.containsKey(screenId)) {
            screenCacheMap[screenId] = mutableSetOf()
        }
        screenCacheMap[screenId]?.add(url)
    }

    /**
     * Clear all cached images used by a specific screen.
     * Call this when leaving a screen to free up memory.
     * @param screenId Identifier for the screen
     */
    fun clearScreenCache(screenId: String) {
        val urls = screenCacheMap[screenId] ?: return

        for (url in urls) {
            memoryCache.remove(url)
        }

        // Clear the tracking set after removing images
        screenCacheMap.remove(screenId)

        Log.d(TAG, "Cleared cache for screen: $screenId (${urls.size} images)")
    }

    /**
     * Clear the entire image cache.
     * Call this when you need to free up memory or when the app is closing.
     */
    fun clearCache() {
        memoryCache.evictAll()
        screenCacheMap.clear()
        Log.d(TAG, "Cleared entire image cache")
    }
}