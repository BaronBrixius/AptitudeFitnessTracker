package com.example.aptitudefitnesstracker.application

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map

class Repository(private val localDao: ILocalDao, private val remoteDao: IRemoteDao) {
    val localRoutines: LiveData<List<RoutineWithExercises>> by lazy { localDao.getRoutinesWithExercises() }
    val remoteRoutines: LiveData<List<Routine>> by lazy { remoteDao.getAllRoutines() }

    @WorkerThread
    suspend fun shareRoutine(routine: Routine) {
        remoteDao.insert(routine)
    }

    @WorkerThread
    suspend fun insertRoutine(routine: Routine) {
        localDao.insert(routine)
    }

    @WorkerThread
    suspend fun update(routine: Routine) {
//        localRoutineDao.up
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        localDao.deleteAllRoutines()
    }
}