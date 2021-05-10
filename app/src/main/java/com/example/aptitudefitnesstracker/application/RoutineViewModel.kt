package com.example.aptitudefitnesstracker.application

import androidx.lifecycle.*
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: Repository) : ViewModel() {  //fixme deleteme, this won't be used I'm just following a tutorial, will clean later  --Max
    val allRoutines: LiveData<List<RoutineEntity>> = repository.allRoutines.asLiveData()

    fun insert(routine: RoutineEntity) = viewModelScope.launch {
        repository.insert(routine)
    }
}

class RoutineViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoutineViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
