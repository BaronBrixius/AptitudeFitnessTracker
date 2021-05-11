package com.example.aptitudefitnesstracker.presentation

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import com.example.aptitudefitnesstracker.presentation.activities.LoginActivity
import com.example.aptitudefitnesstracker.presentation.activities.RoutineListActivity

//singleton class to manage GUI stuff and link to application data
class Presenter : Application() {


    val session: Session by lazy { Session(this) }
    var settings: Settings? = null
    val routineList by lazy { session.routineList }

    init {

    }

    override fun onCreate() {
        super.onCreate()
        startActivity(Intent(this, LoginActivity::class.java))
    }


    fun insert(routineEntity: RoutineEntity) {
        session.insert(routineEntity)
    }

    fun deleteAllRoutines() {
        session.deleteAllRoutines()
    }





}