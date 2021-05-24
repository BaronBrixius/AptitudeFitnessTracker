package com.example.aptitudefitnesstracker.application

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class Repository(private val localRoutineDao: RoutineDao, private val remoteRoutineDao: RoutineDao) {
    val localRoutines: LiveData<List<Routine>> by lazy { localRoutineDao.getAllRoutines() }
    val remoteRoutines: LiveData<List<Routine>> by lazy { remoteRoutineDao.getAllRoutines() }

    @WorkerThread
    suspend fun share(routine: Routine) {
        remoteRoutineDao.insert(routine)
    }

    @WorkerThread
    suspend fun insert(routine: Routine) {
        localRoutineDao.insert(routine)
    }

    @WorkerThread
    suspend fun update(routine: Routine) {
//        localRoutineDao.up
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        localRoutineDao.deleteAllRoutines()
    }
}