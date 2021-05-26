package com.example.aptitudefitnesstracker.application

import android.app.Application
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

class Session : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val localDao by lazy { LocalRoomDatabase.getDatabase(this, applicationScope).iDao() }
    private val remoteDao by lazy { RemoteFirebaseDatabase() }
    var loggedInUser: User? = null
    var firebaseMode: Boolean = false
    var activeRoutine: Routine? = null
    var activeExercise: Exercise? = null

    fun getLocalRoutines(): LiveData<List<Routine>> {
        return localDao.getAllRoutines().map {
            it.map { routine ->
                addExercisesToRoutine(routine)
            }
        }
    }

    private fun addExercisesToRoutine(routine: Routine): Routine {
        routine.exercises = localDao.getExercisesInRoutine(routine.id)
        return routine
    }

    fun updateRoutine(routine: Routine) = applicationScope.launch {
        localDao.update(routine)
    }

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

    fun delete(exercise: Exercise) = applicationScope.launch {
        TODO()
        //localDao.delete(exercise)
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
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener)
    }
}