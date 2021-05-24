package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData

interface IRemoteDao {
    fun getAllRoutines(): LiveData<List<Routine>>
    suspend fun insert(routine: Routine)
    fun getAllExercises(): LiveData<List<Exercise>>
    suspend fun insert(exercise: Exercise)
}