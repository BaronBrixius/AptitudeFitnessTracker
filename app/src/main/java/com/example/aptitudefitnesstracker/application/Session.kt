package com.example.aptitudefitnesstracker.application

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.aptitudefitnesstracker.persistence.LocalRoomDatabase
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session() : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed rather than when the application starts
    val database by lazy { LocalRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { Repository(database.routineDao()) }
    val routineList: LiveData<List<RoutineEntity>> by lazy { repository.allRoutines.asLiveData() }

    fun insert(routine: RoutineEntity) = insert(routine, applicationScope)  //todo delete this later, should actually declare a scope once the Session/Presenter are up and running properly

    fun insert(routine: RoutineEntity, scope: CoroutineScope) = scope.launch {
        repository.insert(routine)
    }

    fun deleteAllRoutines() = applicationScope.launch {
        repository.deleteAllRoutines()
    }

    //User loggedInUser? = null
    fun isLoggedIn(): Boolean {
        return false
    }
}