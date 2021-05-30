package com.example.aptitudefitnesstracker.persistence.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aptitudefitnesstracker.application.data.Exercise
import com.example.aptitudefitnesstracker.application.dao.IRemoteDao
import com.example.aptitudefitnesstracker.application.data.Routine
import com.google.firebase.database.*

class RemoteFirebaseDatabase : IRemoteDao {
    private val databaseURL: String = "https://aptitude-fitness-tracker-default-rtdb.europe-west1.firebasedatabase.app"

    override fun getAllRoutines(): LiveData<List<Routine>> {
        val remoteRoutines: MutableLiveData<List<Routine>> = MutableLiveData()
        val mFirebaseDatabase = FirebaseDatabase.getInstance(databaseURL).getReference("routines")
        mFirebaseDatabase.addValueEventListener(object :
            ValueEventListener {  //use addListenerForSingleValueEvent instead if realtime updates are not needed
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    remoteRoutines.postValue(toRoutines(dataSnapshot))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("failed update")    //todo
            }
        })

        return remoteRoutines
    }

    private fun toRoutines(dataSnapshot: DataSnapshot): List<Routine> {
        val routineList: ArrayList<Routine> = ArrayList()
        dataSnapshot.children.forEach { child ->
            child.getValue(Routine::class.java)?.let {
                val exerciseList = MutableLiveData<List<Exercise>>()    //exercise LiveData has to be reconstructed from Firebase format
                exerciseList.value = toExercises(child.child("exercises"))
                it.exercises = exerciseList
                routineList.add(it)
            }
        }
        return routineList
    }

    override suspend fun insertRoutine(routine: Routine) {
        var mFirebaseDatabase = FirebaseDatabase.getInstance(databaseURL).getReference("routines")
        val firebaseID: String? = mFirebaseDatabase.push().key
        mFirebaseDatabase.child(firebaseID!!).setValue(routine)

        mFirebaseDatabase = mFirebaseDatabase.child(firebaseID).child("exercises")
        routine.exercises.value?.forEach { exercise ->
            val firebaseExerciseID: String? = mFirebaseDatabase.push().key
            mFirebaseDatabase.child(firebaseExerciseID!!).setValue(exercise)
        }
    }

    override fun getAllExercises(): LiveData<List<Exercise>> {
        val remoteExercises: MutableLiveData<List<Exercise>> = MutableLiveData()
        val mFirebaseDatabase = FirebaseDatabase.getInstance(databaseURL).getReference("exercises")
        mFirebaseDatabase.addValueEventListener(object :
            ValueEventListener {  //use addListenerForSingleValueEvent instead if realtime updates are not needed
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    remoteExercises.postValue(toExercises(dataSnapshot))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("failed update")    //todo
            }
        })
        return remoteExercises
    }

    private fun toExercises(dataSnapshot: DataSnapshot): List<Exercise> {
        val exerciseList: ArrayList<Exercise> = ArrayList()
        dataSnapshot.children.forEach { child ->
            child.getValue(Exercise::class.java)?.let { exerciseList.add(it) }
        }
        return exerciseList
    }

    override suspend fun insertExercise(exercise: Exercise) {
        val mFirebaseDatabase = FirebaseDatabase.getInstance(databaseURL).getReference("exercises")
        val firebaseID: String? = mFirebaseDatabase.push().key
        mFirebaseDatabase.child(firebaseID!!).setValue(exercise)
    }
}