package com.example.aptitudefitnesstracker.persistence.local

import androidx.room.TypeConverter
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromList(list: ArrayList<Exercise.Detail>): String {
            return Gson().toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun toList(string: String?): ArrayList<Exercise.Detail> {
            if (string != null) {
                val listType = object : TypeToken<ArrayList<Exercise.Detail>>() {}.type
                return Gson().fromJson(string,listType)
            }
            return ArrayList()
        }
    }
}