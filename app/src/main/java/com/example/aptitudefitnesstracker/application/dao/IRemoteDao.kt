package com.example.aptitudefitnesstracker.application.dao

import androidx.lifecycle.LiveData
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.data.Routine

interface IRemoteDao {
    fun getAllRoutines(): LiveData<List<Routine>>
    suspend fun insertRoutine(routine: Routine)
    fun getAllExercises(): LiveData<List<Exercise>>
    suspend fun insertExercise(exercise: Exercise)
}