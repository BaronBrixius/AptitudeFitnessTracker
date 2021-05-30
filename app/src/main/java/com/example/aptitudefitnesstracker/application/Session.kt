package com.example.aptitudefitnesstracker.application

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.data.Routine
import com.example.aptitudefitnesstracker.persistence.firebase.RemoteFirebaseDatabase
import com.example.aptitudefitnesstracker.persistence.local.LocalRoomDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Session : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val auth by lazy {
        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            loggedInUser = auth.currentUser
            if (!userIsLoggedIn())
                turnFirebaseModeOff()
        }
        auth
    }
    private val localDao by lazy { LocalRoomDatabase.getDatabase(this, applicationScope).iDao() }
    private val remoteDao by lazy { RemoteFirebaseDatabase() }
    var loggedInUser: FirebaseUser? = null
    var firebaseMode: Boolean = false
    var activeRoutine: Routine? = null
    var activeExercise: Exercise? = null
    private var observers: MutableList<IFirebaseModeObserver> = mutableListOf()

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

    fun updateRoutines(routineList: List<Routine>) = applicationScope.launch {
        localDao.updateRoutines(routineList)
    }

    fun updateExercise(exercise: Exercise) = applicationScope.launch {
        localDao.updateExercise(exercise)
    }

    fun updateExercises(exercises: List<Exercise>) = applicationScope.launch {
        localDao.updateExercises(exercises)
    }

    fun createExerciseInRoutine(exercise: Exercise, routine: Routine) = applicationScope.launch {
        exercise.routineId = routine.id
        exercise.position = routine.exercises.value!!.size

        val insertId = localDao.insertExercise(exercise)
//        activeRoutine?.exercises?.value?.forEach { e ->
//            if (insertId.toInt() == e.id) {
//                activeExercise = e
//                return@forEach
//            }
//        }
    }

    fun insertRoutine(routine: Routine) = applicationScope.launch {
        localDao.insertRoutine(routine)
    }

    fun deleteRoutine(routine: Routine) = applicationScope.launch {
        localDao.deleteRoutine(routine)
    }

    fun deleteExercise(exercise: Exercise) = applicationScope.launch {
        localDao.deleteExercise(exercise)
    }

     fun share(routine: Routine) = applicationScope.launch {
        if (userIsLoggedIn()) {
            remoteDao.insertRoutine(routine)
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

    fun saveRemoteRoutineLocally(routine: Routine) = applicationScope.launch {
        val newRoutineId: Long = localDao.insertRoutine(routine)
        val exerciseList = routine.exercises.value
        exerciseList!!.forEach {
            it.routineId = newRoutineId.toInt()
            localDao.insertExercise(it)
        }
    }

    fun userIsLoggedIn(): Boolean {
        return loggedInUser != null
    }

    fun turnFirebaseModeOn(): Boolean {
        return if (userIsLoggedIn()) {
            setFirebaseModeAndNotify(true)
        } else {
            false
        }
    }

    fun turnFirebaseModeOff(): Boolean {
        return setFirebaseModeAndNotify(false)
    }

    fun toggleAndGetFirebaseMode(): Boolean {
        return if (!firebaseMode) {
            turnFirebaseModeOn()
        } else {
            turnFirebaseModeOff()
        }
    }

    private fun setFirebaseModeAndNotify(turnOn: Boolean): Boolean {
        firebaseMode = turnOn
        for (item in observers)
            item.notify(firebaseMode)

        return firebaseMode
    }

    @AddTrace(name = "authenticateLogin")
    fun authenticateLogin(
        email: String,
        password: String,
        onCompleteListener: (Task<AuthResult>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener)
    }

    fun signOut() {
        auth.signOut()
    }

    fun addObserver(newObserver: IFirebaseModeObserver) {
        observers.add(newObserver)
    }

    //think of a better name than "proper" routines
    fun getProperRoutines(): LiveData<List<Routine>> {
        return if (firebaseMode)
            downloadRemoteRoutines()
        else
            getLocalRoutines()
    }

    fun getProperExercises(): LiveData<List<Exercise>>? {
        return if (firebaseMode)
            downloadRemoteExercises()
        else
            activeRoutine?.exercises
    }

    fun sendPasswordResetEmail(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }

    fun createUser(email: String, password: String, onCompleteListener: (Task<AuthResult>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener)
    }
}