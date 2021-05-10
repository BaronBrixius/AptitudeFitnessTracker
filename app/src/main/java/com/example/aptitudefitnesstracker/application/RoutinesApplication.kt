package com.example.aptitudefitnesstracker.application

import android.app.Application
import com.example.aptitudefitnesstracker.persistence.LocalRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RoutinesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { LocalRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { Repository(database.routineDao()) }
}