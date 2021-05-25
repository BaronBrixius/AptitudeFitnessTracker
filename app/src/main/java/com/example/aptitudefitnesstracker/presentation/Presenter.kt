package com.example.aptitudefitnesstracker.presentation

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.EditText
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.activities.AccountActivity
import com.example.aptitudefitnesstracker.presentation.activities.DatabaseTestActivity
import com.example.aptitudefitnesstracker.presentation.activities.RoutineListActivity
import com.example.aptitudefitnesstracker.presentation.activities.SignupActivity

//singleton class to manage GUI stuff and link to application data
class Presenter : Application() {


    val session: Session by lazy { Session(this) }
    var settings: Settings? = null
    val routineList by lazy { session.repository.localRoutines }

    init {

    }

    /*
    RoutineListActivity
     */
    fun insert(routine: Routine) {
        session.insertRoutine(routine)
    }

    fun deleteAllRoutines() {
        session.deleteAllRoutines()
    }

    fun addNewItemButtonPressed() { //temp for testing
        var intent = Intent(
            this,
            DatabaseTestActivity::class.java
        ) //TODO replace "DatabaseTestActivity" with appropriate class later
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun accountSettingButton() { //temp for testing
        var intent = Intent(
            this,
            AccountActivity::class.java
        ) //TODO replace "DatabaseTestActivity" with appropriate class later
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /*
    LoginActivity
     */
    fun loginButtonPressed(inputEmail: EditText, inputPassword: EditText) {

        if (session.authenticateLogin(inputEmail!!, inputPassword!!)) {
            var intent = Intent(this, RoutineListActivity::class.java)
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    fun resetButtonPressed() {
        var intent = Intent(this, AccountActivity::class.java)
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun signupButtonPressed() {
        var intent = Intent(this, SignupActivity::class.java)
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun routineSelected(routine: Routine){
        //take you to exerciseListActivity
        //Pass Exercise list
    }

    /*
    SignupActivity
     */
}