package com.example.filmfinder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.filmfinder.data.local.dao.MovieDao
import com.example.filmfinder.data.local.entity.Movie

/**
 * Room database class for the application.
 * Defines the database configuration and serves as the main access point
 * for the underlying connection to the SQLite database.
 */
@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Abstract method to get the Movie DAO.
     */
    abstract fun movieDao(): MovieDao

    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Get a database instance or create one if it doesn't exist.
         * Uses singleton pattern to ensure only one instance of the database exists.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}