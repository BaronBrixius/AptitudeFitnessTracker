package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ILocalDao {
    @Transaction
    @Query("SELECT * FROM routines")
    fun getRoutinesWithExercises(): LiveData<List<RoutineWithExercises>>

    @Query("SELECT * FROM routines ORDER BY name")
    fun getAllRoutines(): LiveData<List<Routine>>

//    @Query("""
//        SELECT * FROM exercises
//        INNER JOIN routineExerciseJoin ON exercises.exerciseID = routineExerciseJoin.exerciseId
//        WHERE routineExerciseJoin.routineId = :routineId
//
//    """)
//    fun getExercisesInRoutine(routineId: Int): LiveData<List<Exercise>>

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    fun insert(join: RoutineWithExercises)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exercise: Exercise)

    @Query("DELETE FROM routines") //fixme testing for now I suppose, but yikes
    suspend fun deleteAllRoutines()

    @Update
    suspend fun update(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

}