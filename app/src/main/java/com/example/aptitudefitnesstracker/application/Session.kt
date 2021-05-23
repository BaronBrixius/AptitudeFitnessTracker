package com.example.aptitudefitnesstracker.application

import android.content.Context
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.aptitudefitnesstracker.persistence.local.LocalRoomDatabase
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session(context: Context) {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed rather than when the application starts
    val database by lazy { LocalRoomDatabase.getDatabase(context, applicationScope) }
    val repository by lazy { Repository(database.routineDao()) }
    val routineList: LiveData<List<Routine>> by lazy { repository.allRoutines.asLiveData() }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
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

    var loggedInUser: User? = null
    fun userIsLoggedIn(): Boolean {
        return loggedInUser != null
    }


    /*
    authenticateLogin()  still needs to be refactored into presenter and session classes.
     */

    @AddTrace(name = "authenticateLogin")
    fun authenticateLogin(inputEmail: EditText, inputPassword: EditText): Boolean {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

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