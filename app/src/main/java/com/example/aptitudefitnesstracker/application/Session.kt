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
        repository.addExerciseToRoutine(exercise, routine)
    }

    fun removeExerciseFromRoutine(exercise: Exercise, routine: Routine) {
        repository.addExerciseToRoutine(exercise, routine)
    }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
        repository.insertRoutine(routine)
    }

    fun delete(routine: Routine) = applicationScope.launch {
        repository.deleteRoutine(routine)
    }

    fun deleteAllRoutines() = applicationScope.launch {
        repository.deleteAllRoutines()
    }

    fun insertExercise(exercise: Exercise) = applicationScope.launch {
        repository.insertExercise(exercise)
    }

    fun deleteExercise(exercise: Exercise) = applicationScope.launch {
        repository.deleteExercise(exercise)
    }

    fun deleteAllExercises() = applicationScope.launch {
//        repository.deleteAllRoutines()
    }

    //Removed scope for Boolean return, had to add suspend to call repository.shareRoutine(routine)
    suspend fun share(routine: Routine): Boolean {
        return if (userIsLoggedIn()) {
            repository.shareRoutine(routine)
            true
        } else {
            false
        }
    }

    suspend fun share(exercise: Exercise): Boolean {
        return if (userIsLoggedIn()) {
            repository.shareExercise(exercise)
            true
        } else {
            false
        }
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