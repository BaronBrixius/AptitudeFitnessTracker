package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.aptitudefitnesstracker.persistence.RoutineEntity

class Session(private val repository: Repository) {
    //User loggedInUser? = null
    val routineList: LiveData<List<RoutineEntity>> = repository.allRoutines.asLiveData()

    suspend fun insert(routine: RoutineEntity) {
        repository.insert(routine)
    }

    fun isLoggedIn(): Boolean {
        return false
    }
}