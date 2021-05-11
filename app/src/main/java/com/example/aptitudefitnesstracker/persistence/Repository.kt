package com.example.aptitudefitnesstracker.persistence

import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.aptitudefitnesstracker.application.RoutineDao
import com.example.aptitudefitnesstracker.persistence.local.RoutineEntity
import com.example.aptitudefitnesstracker.presentation.activities.DatabaseTestActivity
import com.google.firebase.database.*
import kotlinx.coroutines.flow.Flow

class Repository(private val routineDao: RoutineDao) {
    val allRoutines: Flow<List<RoutineEntity>> = routineDao.getAllRoutines()

    @WorkerThread
    suspend fun insert(routine: RoutineEntity) {
        routineDao.insert(routine)
    }

    @WorkerThread
    suspend fun deleteAllRoutines() {
        routineDao.deleteAllRoutines()
    }

//    private var mFirebaseDatabase: DatabaseReference? = null
//    private var mFirebaseInstance: FirebaseDatabase? = null

//    mFirebaseInstance = FirebaseDatabase.getInstance()
//
//    // get reference to 'users' node
//    mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
//
//    // store app title to 'app_title' node
//    mFirebaseInstance!!.getReference("app_title").setValue("Realtime Database")
//
//    // app_title change listener
//    mFirebaseInstance!!.getReference("app_title")
//    .addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            Log.e(DatabaseTestActivity.TAG, "App title updated")
//            val appTitle = dataSnapshot.getValue(String::class.java)
//
//            // update toolbar title
////                    supportActionBar!!.setTitle(appTitle)
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            // Failed to read value
//            Log.e(DatabaseTestActivity.TAG, "Failed to read app title value.", error.toException())
//        }
//    })


//    private fun updateExercise(name: String) {
//        // updating the user via child nodes
//        if (!TextUtils.isEmpty(name))
//            mFirebaseDatabase!!.child(userId!!).child("name").setValue(name)
//    }

//    private fun createExercise(name: String) {
////     TODO
////     In real apps this userId should be fetched
////     by implementing firebase auth
//
//        if (TextUtils.isEmpty(userId)) {
//            userId = mFirebaseDatabase!!.push().key
//        }
//        val user = RoutineEntity(name)
//        mFirebaseDatabase!!.child(userId!!).setValue(user)
//        addUserChangeListener()
//    }

//    /**
//     * User data change listener
//     */
//    private fun addUserChangeListener() {
//        // User data change listener
//        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val exercise = dataSnapshot.getValue(RoutineEntity::class.java)
//
//                // Check for null
//                if (exercise == null) {
//                    Log.e(DatabaseTestActivity.TAG, "Exercise data is null!")
//                    return
//                }
//                Log.e(DatabaseTestActivity.TAG, "Exercise data is changed!" + exercise.name)
//
//                // Display newly updated name and email
//                txtDetails!!.text = exercise.name
//
//                // clear edit text
//                inputName!!.setText("")
//                toggleButton()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.e(DatabaseTestActivity.TAG, "Failed to read exercise", error.toException())
//            }
//        })
//    }

//    companion object {
//        private val TAG = DatabaseTestActivity::class.java.simpleName
//    }
}