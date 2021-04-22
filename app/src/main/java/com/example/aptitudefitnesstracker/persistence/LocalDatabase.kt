package com.example.aptitudefitnesstracker.persistence

import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.IExerciseDao
import com.example.aptitudefitnesstracker.application.IRoutineDao
import com.example.aptitudefitnesstracker.application.ISettingsDao

class LocalDatabase : IExerciseDao, IRoutineDao, ISettingsDao {
    override fun getAllExercises(): List<Exercise> {
        TODO("Not yet implemented")
    }
}