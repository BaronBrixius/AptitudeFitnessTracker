package com.example.aptitudefitnesstracker.persistence

import com.example.aptitudefitnesstracker.application.RoutineDao
import kotlinx.coroutines.flow.Flow

abstract class FirebaseDatabase : RoutineDao {
    override fun getAllRoutines(): Flow<List<RoutineEntity>> {
        TODO("Not yet implemented")
    }
}