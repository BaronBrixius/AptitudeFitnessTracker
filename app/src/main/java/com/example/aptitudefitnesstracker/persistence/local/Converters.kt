package com.example.aptitudefitnesstracker.persistence.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromMap(map: LinkedHashMap<String,Double>): String {
            return Gson().toJson(map)
        }

        @TypeConverter
        @JvmStatic
        fun toMap(string: String?): LinkedHashMap<String,Double> {
            if (string != null) {
                val mapType = object : TypeToken<LinkedHashMap<String, Double>>() {}.type
                return Gson().fromJson(string,mapType)
            }
            return LinkedHashMap()
        }
    }
}