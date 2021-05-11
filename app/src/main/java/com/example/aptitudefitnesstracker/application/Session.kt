package com.example.aptitudefitnesstracker.application

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.aptitudefitnesstracker.persistence.Repository
import com.example.aptitudefitnesstracker.persistence.local.LocalRoomDatabase
import com.example.aptitudefitnesstracker.persistence.local.RoutineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session(context: Context)  {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed rather than when the application starts
    val database by lazy { LocalRoomDatabase.getDatabase(context, applicationScope) }
    val repository by lazy { Repository(database.routineDao()) }
    val routineList: LiveData<List<RoutineEntity>> by lazy { repository.allRoutines.asLiveData() }

    fun insert(routine: Routine) = applicationScope.launch { //insertion runs in REPLACE mode so this functions as update as well
            TODO("Need to implement translator to replace RoutineEntity method.")
    }
    fun insert(routine: RoutineEntity) = applicationScope.launch {
        repository.insert(routine)
    }

    fun delete(routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun deleteAllRoutines() = applicationScope.launch {
        repository.deleteAllRoutines()
    }

    fun insert(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun delete(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun share(routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun share(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun downloadRoutineDatabase(scope: CoroutineScope) = scope.launch {
        TODO("Firebase hookup")
    }

    var loggedInUser : User? = null
    fun userIsLoggedIn(): Boolean {
        return loggedInUser != null
    }
}