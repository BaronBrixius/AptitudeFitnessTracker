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
                routine.exercises = localDao.getExercisesInRoutine(routine.id)
                routine
            }
        }
    }

    fun downloadRemoteRoutines(): LiveData<List<Routine>> {
        return remoteDao.getAllRoutines()
    }

    fun downloadRemoteExercises(): LiveData<List<Exercise>> {
        return remoteDao.getAllExercises()
    }

    fun updateRoutine(routine: Routine) = applicationScope.launch {
        localDao.updateRoutine(routine)
    }

    fun copyExerciseToRoutine(exercise: Exercise, routine: Routine) = applicationScope.launch {
        val copyExercise = exercise.copy(routineId = routine.id)
        insertExercise(copyExercise)
    }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
        localDao.insertRoutine(routine)
    }

    fun insertExercise(exercise: Exercise) = applicationScope.launch {
        localDao.insertExercise(exercise)
    }

    fun deleteRoutine(routine: Routine) = applicationScope.launch {
        localDao.deleteRoutine(routine)
    }

    fun deleteExercise(exercise: Exercise) = applicationScope.launch {
        localDao.deleteExercise(exercise)
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
    fun authenticateLogin(
        email: String,
        password: String,
        onCompleteListener: (Task<AuthResult>) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener)
    }
}