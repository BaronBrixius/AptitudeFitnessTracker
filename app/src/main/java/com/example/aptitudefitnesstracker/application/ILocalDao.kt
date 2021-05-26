package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ILocalDao {
    @Query("SELECT * FROM routines ORDER BY name")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Query("SELECT * FROM exercises WHERE routineId = :routineId")
    fun getExercisesInRoutine(routineId: Int): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("DELETE FROM routines") //fixme testing for now I suppose, but yikes
    suspend fun deleteAllRoutines()

    @Update
    suspend fun update(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

}