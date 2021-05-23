package com.example.aptitudefitnesstracker.persistence

import androidx.room.TypeConverter

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toCommaSeparatedString(list: List<String>): String {
            return list.joinToString(separator = ", ")
        }

        @TypeConverter
        @JvmStatic
        fun toList(string: String): List<String> {
            return string.split(", ")
        }
    }
}


//fun Routine.toRoutineEntity() = RoutineEntity(
//    id = id,
//    name = name
//)
//
//fun RoutineEntity.toRoutine() = Routine(
//    id = id,
//    name = name
//)