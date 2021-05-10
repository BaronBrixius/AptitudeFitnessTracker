package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.*
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import kotlinx.coroutines.launch

class Session(private val repository: Repository) : ViewModel() {
    //User loggedInUser? = null
    val routineList: LiveData<List<RoutineEntity>> = repository.allRoutines.asLiveData()

    fun insert(routine: RoutineEntity) = viewModelScope.launch {
        repository.insert(routine)
    }

    fun isLoggedIn(): Boolean {
        return false
    }
}