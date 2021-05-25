package com.example.aptitudefitnesstracker.application

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map

class Repository(private val localDao: ILocalDao, private val remoteDao: IRemoteDao) {
    val localRoutines: LiveData<List<Routine>> by lazy {
        localDao.getAllRoutines().map {
            it.map { routine ->
                addExercisesToRoutine(routine)
            }
        }
    }

    private fun addExercisesToRoutine(routine: Routine): Routine {
        routine.exercises = localDao.getExercisesInRoutine(routine.id)
        return routine
    }

    @WorkerThread
    suspend fun insertRoutine(routine: Routine) {
        localDao.insertRoutine(routine)
    }

    @WorkerThread
    suspend fun update(routine: Routine) {
//        localRoutineDao.up
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        localDao.deleteAllRoutines()
    }

    @WorkerThread
    fun downloadRemoteRoutines(): LiveData<List<Routine>> {
        return remoteDao.getAllRoutines()
    }

    @WorkerThread
    suspend fun shareRoutine(routine: Routine) {
        remoteDao.insertRoutine(routine)
    }

    @WorkerThread
    fun downloadRemoteExercises(): LiveData<List<Exercise>> {
        return remoteDao.getAllExercises()
    }

    @WorkerThread
    suspend fun shareExercise(exercise: Exercise) {
        remoteDao.insertExercise(exercise)
    }
}
