package com.example.aptitudefitnesstracker.application

import androidx.annotation.WorkerThread
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import kotlinx.coroutines.flow.Flow

class Repository(private val routineDao: RoutineDao) {
    val allRoutines: Flow<List<RoutineEntity>> = routineDao.getAllRoutines()

    @WorkerThread
    suspend fun insert(routine: RoutineEntity) {
        routineDao.insert(routine)
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        routineDao.deleteAllRoutines()
    }
}