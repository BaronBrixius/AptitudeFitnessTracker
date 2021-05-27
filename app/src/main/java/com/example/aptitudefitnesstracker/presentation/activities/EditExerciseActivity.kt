package com.example.aptitudefitnesstracker.presentation.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.databinding.ActivityAddExerciseBinding
import com.example.aptitudefitnesstracker.presentation.ThemeUtils

class EditExerciseActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private lateinit var binding: ActivityAddExerciseBinding
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_add_exercise)

        binding = ActivityAddExerciseBinding.inflate(layoutInflater)

        // Displaying toolbar icon
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)

        binding.toolbar.title = "Add Exercise"

        var exercise = session.activeExercise

        if (exercise != null) {
            binding.name.hint = exercise.name
        }


        // Save / update the user
        binding.btnSave.setOnClickListener {
            val name = binding.name.text.toString()
            val details = binding.Detail.text.toString()
            val notes = binding.Notes.text.toString()


            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                if (exercise != null) {
                    exercise.name = name
                }
            } else {
//                updateExercise(name)
            }
        }
        toggleButton()
    }

    // Changing button text
    private fun toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            binding.btnSave.text = "Save"
        } else {
            binding.btnSave.text = "Update"
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private fun createExercise(name: String) {
        if (name.isNotEmpty()) {
            session.insertExercise(Exercise(name))
            Toast.makeText(this, "Exercise added", Toast.LENGTH_LONG).show()
        } else {
            session.deleteAllRoutines() //todo let's not leave this in for final release eh?
            Toast.makeText(this, "Deleted all routines", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = EditExerciseActivity::class.java.simpleName
    }
}