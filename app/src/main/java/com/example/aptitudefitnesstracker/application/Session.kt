package com.example.aptitudefitnesstracker.application

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
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
    private val localDao by lazy { LocalRoomDatabase.getDatabase(context, applicationScope).iDao() }
    val remoteDao by lazy { RemoteFirebaseDatabase() }
    var loggedInUser: User? = null

    fun addExerciseToRoutine(exercise: Exercise, routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun removeExerciseFromRoutine(exercise: Exercise, routine: Routine) = applicationScope.launch {
        TODO()
    }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
        localDao.insertRoutine(routine)
    }

    fun delete(routine: Routine) = applicationScope.launch {
        localDao.delete(routine)
    }

    fun deleteAllRoutines() = applicationScope.launch {
        localDao.deleteAllRoutines()
    }

    fun insertExercise(exercise: Exercise) = applicationScope.launch {
        TODO()
        //localDao.insertExercise(exercise)
    }

    fun deleteExercise(exercise: Exercise) = applicationScope.launch {
        TODO()
        //localDao.deleteExercise(exercise)
    }

    fun deleteAllExercises() = applicationScope.launch {
        TODO()
        //localDao.deleteAllExercises()
    }

    fun downloadRemoteRoutines(): LiveData<List<Routine>> {
        return remoteDao.getAllRoutines()
    }

    fun downloadRemoteExercises(): LiveData<List<Exercise>> {
        return remoteDao.getAllExercises()
    }

    //Removed scope for Boolean return, had to add suspend to call repository.shareRoutine(routine)
    suspend fun share(routine: Routine): Boolean {
        return if (userIsLoggedIn()) {
            remoteDao.insertRoutine(routine)
            true
        } else {
            false
        }
    }

    suspend fun share(exercise: Exercise): Boolean {
        return if (userIsLoggedIn()) {
            remoteDao.insertExercise(exercise)
            true
        } else {
            false
        }
    }

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