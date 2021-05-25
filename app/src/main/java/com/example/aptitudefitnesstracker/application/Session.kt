package com.example.aptitudefitnesstracker.application

import android.content.Context
import com.example.aptitudefitnesstracker.persistence.firebase.RemoteFirebaseDatabase
import com.example.aptitudefitnesstracker.persistence.local.LocalRoomDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session(context: Context) {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Using by lazy so the database/repository are only created when they're needed rather than when the application starts
    val repository by lazy {
        Repository(
            LocalRoomDatabase.getDatabase(context, applicationScope).iDao(),
            RemoteFirebaseDatabase()
        )
    }

    fun addExerciseToRoutine(exercise: Exercise, routine: Routine) {
        //todo
    }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
        repository.insertRoutine(routine)
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

    var loggedInUser: User? = null
    fun userIsLoggedIn(): Boolean {
        return loggedInUser != null
    }


    /*
    authenticateLogin()  still needs to be refactored into presenter and session classes.
     */

    @AddTrace(name = "authenticateLogin")
    fun authenticateLogin(email: String, password: String, onCompleteListener: (Task<AuthResult>) -> Unit) {
        //authenticate user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener)
    }
}

