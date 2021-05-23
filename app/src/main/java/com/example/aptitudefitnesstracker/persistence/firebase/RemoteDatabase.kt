package com.example.aptitudefitnesstracker.persistence.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.RoutineDao
import com.google.firebase.database.*

class RemoteDatabase : RoutineDao {
    private lateinit var mFirebaseDatabase: DatabaseReference
    val remoteRoutines: MutableLiveData<List<Routine>> = MutableLiveData()

    override fun getAllRoutines(): LiveData<List<Routine>> {
        mFirebaseDatabase =
            FirebaseDatabase.getInstance("https://aptitude-fitness-tracker-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("routines")
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
            child.getValue(Routine::class.java)?.let { routineList.add(it) }
        }
        return routineList
    }

    override suspend fun insert(routine: Routine) {
//     TODO
//     In real apps this userId should be fetched by implementing firebase auth
        mFirebaseDatabase =
            FirebaseDatabase.getInstance("https://aptitude-fitness-tracker-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("routines")
        val firebaseID: String? = mFirebaseDatabase.push().key
        mFirebaseDatabase.child(firebaseID!!).setValue(routine)
    }

    override suspend fun deleteAllRoutines() {
        TODO("Not yet implemented")
    }
}