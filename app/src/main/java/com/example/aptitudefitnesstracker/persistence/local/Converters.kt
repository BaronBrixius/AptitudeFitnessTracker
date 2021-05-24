package com.example.aptitudefitnesstracker.persistence.local

import androidx.room.TypeConverter

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toCommaSeparatedString(list: List<String>): String {
            return list.joinToString(", ")
        }

        @TypeConverter
        @JvmStatic
        fun toList(string: String?): List<String> {
            if (string != null) {
                return string.split(", ")
            }
            return ArrayList()
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