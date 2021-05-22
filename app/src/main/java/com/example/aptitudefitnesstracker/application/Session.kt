package com.example.aptitudefitnesstracker.application

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.persistence.LocalRoomDatabase
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session(context: Context) {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed rather than when the application starts
    val database by lazy { LocalRoomDatabase.getDatabase(context, applicationScope) }
    val repository by lazy { Repository(database.routineDao()) }
    val routineList: LiveData<List<RoutineEntity>> by lazy { repository.allRoutines.asLiveData() }


    fun insert(routine: Routine) =
        applicationScope.launch { //insertion runs in REPLACE mode so this functions as update as well
            TODO("Need to implement translator to replace RoutineEntity method.")
        }

    fun insert(routine: RoutineEntity) = applicationScope.launch {
        repository.insert(routine)
    }

    fun delete(routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun deleteAllRoutines() = applicationScope.launch {
        repository.deleteAllRoutines()
    }

    fun insert(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun delete(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun share(routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun share(exercise: Exercise) = applicationScope.launch {
        TODO()
    }

    fun downloadRoutineDatabase(scope: CoroutineScope) = scope.launch {
        TODO("Firebase hookup")
    }

    var loggedInUser : User? = null
    var loggedInUser: User? = null
    fun userIsLoggedIn(): Boolean {
        return loggedInUser != null
    }


    /*
    authenticateLogin()  still needs to be refactored into presenter and session classes.
     */

    @AddTrace(name = "authenticateLogin")
    fun authenticateLogin(inputEmail: EditText, inputPassword: EditText): Boolean {
        val email = inputEmail!!.text.toString()
        val password = inputPassword!!.text.toString()

        return true
//
//        //authenticate user
//        auth!!.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(
//                this@LoginActivity
//            ) { task ->
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
//            }
    }


}