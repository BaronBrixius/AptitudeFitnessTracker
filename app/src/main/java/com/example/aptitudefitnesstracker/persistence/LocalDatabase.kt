package com.example.aptitudefitnesstracker.persistence

import com.example.aptitudefitnesstracker.application.RoutineDao
import com.example.aptitudefitnesstracker.application.ISettingsDao
import kotlinx.coroutines.flow.Flow

abstract class LocalDatabase : RoutineDao,
    ISettingsDao {
    override fun getAllRoutines(): Flow<List<RoutineEntity>> {
        TODO("Not yet implemented")
    }
}