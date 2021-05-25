package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData

interface IRemoteDao {
    fun getAllRoutines(): LiveData<List<Routine>>
    suspend fun insertRoutine(routine: Routine)
    fun getAllExercises(): LiveData<List<Exercise>>
    suspend fun insertExercise(exercise: Exercise)
}