package com.example.filmfinder.data.local.converters

import androidx.room.TypeConverter
import com.example.filmfinder.data.local.entity.Rating
import org.json.JSONArray
import org.json.JSONObject

/**
 * Type converter for Room database to handle Rating objects.
 * Converts between Rating list and JSON string representation.
 */
class RatingConverter {
    @TypeConverter
    fun fromRatingsList(ratings: List<Rating>): String {
        val jsonArray = JSONArray()
        ratings.forEach { rating ->
            val jsonObject = JSONObject()
            jsonObject.put("source", rating.source)
            jsonObject.put("value", rating.value)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toRatingsList(ratingsString: String): List<Rating> {
        val ratings = mutableListOf<Rating>()
        try {
            val jsonArray = JSONArray(ratingsString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val source = jsonObject.getString("source")
                val value = jsonObject.getString("value")
                ratings.add(Rating(source, value))
            }
        } catch (e: Exception) {
            // Return empty list if parsing fails
        }
        return ratings
    }
}