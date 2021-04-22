package com.example.aptitudefitnesstracker.persistence

import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.IExerciseDao
import com.example.aptitudefitnesstracker.application.IRoutineDao

class FirebaseDatabase : IExerciseDao, IRoutineDao {
    override fun getAllExercises(): List<Exercise> {
        TODO("Not yet implemented")
    }
}