package com.example.aptitudefitnesstracker.presentation.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.persistence.RoutineEntity
import com.example.aptitudefitnesstracker.presentation.Presenter
import com.google.firebase.database.*
import com.google.firebase.perf.metrics.AddTrace

//todo this was set up as a test for the Firebase Database, currently the Add feature is commented out and
// replaced with local database Addition stuff, probably should split it
class DatabaseTestActivity : AppCompatActivity() {
    private var txtDetails: TextView? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_databasetest)

        // Displaying toolbar icon
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        txtDetails = findViewById<View>(R.id.txt_user) as TextView
        inputName = findViewById<View>(R.id.name) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        // store app title to 'app_title' node
        mFirebaseInstance!!.getReference("app_title").setValue("Realtime Database")

        // app_title change listener
        mFirebaseInstance!!.getReference("app_title")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.e(TAG, "App title updated")
                    val appTitle = dataSnapshot.getValue(String::class.java)

                    // update toolbar title
//                    supportActionBar!!.setTitle(appTitle)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read app title value.", error.toException())
                }
            })

        // Save / update the user
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()

            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                createExercise(name)
            } else {
                updateExercise(name)
            }
        }
        toggleButton()
    }

    // Changing button text
    private fun toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave!!.text = "Save"
        } else {
            btnSave!!.text = "Update"
        }
    }

    /**
     * Creating new user node under 'users'
     */
//    @AddTrace(name = "CreateExercise")
    private fun createExercise(name: String) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth

//        if (TextUtils.isEmpty(userId)) {
//            userId = mFirebaseDatabase!!.push().key
//        }
//        val user = RoutineEntity(name)
//        mFirebaseDatabase!!.child(userId!!).setValue(user)
//        addUserChangeListener()
        val presenter = application as Presenter

        if (!name.isEmpty()) {
            presenter.insert(RoutineEntity(name))
            Toast.makeText(presenter, "Routine added", Toast.LENGTH_LONG).show()
        } else {
            presenter.deleteAllRoutines() //todo let's not leave this in for final release eh?
            Toast.makeText(presenter, "Deleted all routines", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * User data change listener
     */
    private fun addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exercise = dataSnapshot.getValue(RoutineEntity::class.java)

                // Check for null
                if (exercise == null) {
                    Log.e(TAG, "Exercise data is null!")
                    return
                }
                Log.e(TAG, "Exercise data is changed!" + exercise.name)

                // Display newly updated name and email
                txtDetails!!.text = exercise.name

                // clear edit text
                inputName!!.setText("")
                toggleButton()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "Failed to read exercise", error.toException())
            }
        })
    }

    @AddTrace(name = "UpdateUser")
    private fun updateExercise(name: String) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase!!.child(userId!!).child("name").setValue(name)
    }

    companion object {
        private val TAG = DatabaseTestActivity::class.java.simpleName
    }
}