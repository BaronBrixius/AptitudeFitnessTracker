package com.example.aptitudefitnesstracker.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoutineDao {
//    fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM routines ORDER BY name")
    abstract fun getAllRoutines(): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine)

    @Query("DELETE FROM routines") //fixme testing for now I suppose, but yikes
    abstract suspend fun deleteAllRoutines()
}