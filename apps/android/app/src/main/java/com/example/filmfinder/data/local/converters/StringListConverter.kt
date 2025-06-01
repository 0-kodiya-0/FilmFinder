package com.example.filmfinder.data.local.converters

import androidx.room.TypeConverter
import org.json.JSONArray

/**
 * Type converter for Room database to handle List<String> objects.
 * Converts between List<String> and JSON string representation.
 */
class StringListConverter {
    @TypeConverter
    fun fromStringList(strings: List<String>): String {
        val jsonArray = JSONArray()
        strings.forEach { str ->
            jsonArray.put(str)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toStringList(stringListJson: String): List<String> {
        val strings = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(stringListJson)
            for (i in 0 until jsonArray.length()) {
                strings.add(jsonArray.getString(i))
            }
        } catch (e: Exception) {
            // Return empty list if parsing fails
        }
        return strings
    }
}