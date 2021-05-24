package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutineDao {
//    fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM routines ORDER BY name")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine)

    @Query("DELETE FROM routines") //fixme testing for now I suppose, but yikes
    suspend fun deleteAllRoutines()
}