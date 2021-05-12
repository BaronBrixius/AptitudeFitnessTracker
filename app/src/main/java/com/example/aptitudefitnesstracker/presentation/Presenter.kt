package com.example.aptitudefitnesstracker.presentation

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import com.example.aptitudefitnesstracker.presentation.activities.*

//singleton class to manage GUI stuff and link to application data
class Presenter : Application() {


    val session: Session by lazy { Session(this) }
    var settings: Settings? = null
    val routineList by lazy { session.routineList }

    init {

    }

    /*
    RoutineListActivity
     */
    fun insert(routineEntity: RoutineEntity) {
        session.insert(routineEntity)
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



    /*
    SignupActivity
     */


}