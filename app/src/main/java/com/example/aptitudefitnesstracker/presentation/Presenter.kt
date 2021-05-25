package com.example.aptitudefitnesstracker.presentation

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.EditText
import androidx.lifecycle.LiveData
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.activities.AccountActivity
import com.example.aptitudefitnesstracker.presentation.activities.DatabaseTestActivity
import com.example.aptitudefitnesstracker.presentation.activities.RoutineListActivity
import com.example.aptitudefitnesstracker.presentation.activities.SignupActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

//singleton class to manage GUI stuff and link to application data
class Presenter : Application() {
    val session: Session by lazy { Session(this) }
    val routineList: LiveData<List<Routine>> by lazy { session.repository.localRoutines }

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
        val intent = Intent(
            this,
            DatabaseTestActivity::class.java
        ) //TODO replace "DatabaseTestActivity" with appropriate class later
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun accountSettingButton() { //temp for testing
        val intent = Intent(
            this,
            AccountActivity::class.java
        ) //TODO replace "DatabaseTestActivity" with appropriate class later
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /*
    LoginActivity
     */
    fun loginButtonPressed(inputEmail: EditText, inputPassword: EditText) {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

        val listener: ((Task<AuthResult>) -> Unit) =
            { task -> //todo may want the activity to come up with its own callback?
                if (task.isSuccessful) {
                    val intent = Intent(this, RoutineListActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

        session.authenticateLogin(email, password, listener)

//                // If sign in fails, display a message to the user. If sign in succeeds
//                // the auth state listener will be notified and logic to handle the
//                // signed in user can be handled in the listener.
//                progressBar!!.visibility = View.GONE
//                if (!task.isSuccessful) {
//                    // there was an error
//                    if (password.length < 6) {
//                        inputPassword!!.error = getString(R.string.minimum_password)
//                    } else {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            getString(R.string.auth_failed),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                } else {
//                    val intent = Intent(this@LoginActivity, DatabaseTestActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
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