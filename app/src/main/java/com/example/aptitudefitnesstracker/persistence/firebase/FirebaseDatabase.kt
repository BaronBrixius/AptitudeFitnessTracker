package com.example.aptitudefitnesstracker.persistence.firebase

import com.example.aptitudefitnesstracker.application.RoutineDao
import com.example.aptitudefitnesstracker.persistence.local.RoutineEntity
import kotlinx.coroutines.flow.Flow

abstract class FirebaseDatabase : RoutineDao {
    override fun getAllRoutines(): Flow<List<RoutineEntity>> {
        TODO("Not yet implemented")
    }
}