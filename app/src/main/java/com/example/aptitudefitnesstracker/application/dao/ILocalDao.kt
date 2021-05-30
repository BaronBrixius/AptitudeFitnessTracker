package com.example.aptitudefitnesstracker.application.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.data.Routine

@Dao
interface ILocalDao {
    @Query("SELECT * FROM routines ORDER BY position")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Query("SELECT * FROM exercises WHERE routineId = :routineId ORDER BY position")
    fun getExercisesInRoutine(routineId: Int): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY position")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise) : Long

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Update
    suspend fun updateRoutines(routineList: List<Routine>)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Update
    suspend fun updateExercises(exercise: List<Exercise>)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}