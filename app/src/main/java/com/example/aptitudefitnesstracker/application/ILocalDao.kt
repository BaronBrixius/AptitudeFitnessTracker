package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ILocalDao {
    @Query("SELECT * FROM routines ORDER BY id")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Query("SELECT * FROM exercises WHERE routineId = :routineId ORDER BY position")
    fun getExercisesInRoutine(routineId: Int): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY position")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Update
    suspend fun updateExercises(exercise: List<Exercise>)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}