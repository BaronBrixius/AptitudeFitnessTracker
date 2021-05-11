package com.example.aptitudefitnesstracker.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
//    fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM routines ORDER BY name")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity)

    @Query("DELETE FROM routines") //fixme testing for now I suppose, but yikes
    suspend fun deleteAllRoutines()
}