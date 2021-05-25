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
    val remoteRoutines: LiveData<List<Routine>> by lazy { remoteDao.getAllRoutines() }

    private fun addExercisesToRoutine(routine: Routine): Routine {
        routine.exercises = localDao.getExercisesInRoutine(routine.id)
        return routine
    }

    @WorkerThread
    suspend fun shareRoutine(routine: Routine) {
        remoteDao.insert(routine)
    }

    @WorkerThread
    suspend fun insertRoutine(routine: Routine) {
//        localDao.insert(routine)
        localDao.insert(Exercise(0,1,routine.name,ArrayList()))
    }

    @WorkerThread
    suspend fun update(routine: Routine) {
//        localRoutineDao.up
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        localDao.deleteAllRoutines()
    }


    //These methods were added for the session class to be able to call them
    fun deleteExercise(exercise: Exercise) {
        TODO()
    }

    fun deleteRoutine(routine: Routine) {
        TODO()
    }

    fun addExerciseToRoutine(exercise: Exercise, routine: Routine) {
        TODO()
    }

    fun insertExercise(exercise: Exercise) {
        TODO()
    }

    fun shareExercise(exercise: Exercise) {
        TODO()
    }

    fun insertDetail(exercise: Exercise, detail: Detail) {
        TODO()
    }

}
