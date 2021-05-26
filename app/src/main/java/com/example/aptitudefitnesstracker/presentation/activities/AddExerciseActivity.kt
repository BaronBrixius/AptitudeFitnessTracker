package com.example.aptitudefitnesstracker.presentation.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Exercise
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.example.aptitudefitnesstracker.presentation.Presenter
import kotlinx.android.synthetic.main.activity_add_exercise.*

class AddExerciseActivity : AppCompatActivity() {
    private var inputDetails: EditText? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null
    private var userId: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_add_exercise)
        // Displaying toolbar icon
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        inputName = findViewById<View>(R.id.name) as EditText
        inputDetails = findViewById<View>(R.id.Detail) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        toolbar.title = "Add Exercise"

        // Save / update the user
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()

            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                createExercise(name)
            } else {
//                updateExercise(name)
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
    private fun createExercise(name: String) {
        val presenter = application as Presenter

        if (name.isNotEmpty()) {
            presenter.createExercise(Exercise(name))
            Toast.makeText(presenter, "Exercise added", Toast.LENGTH_LONG).show()
        } else {
            presenter.deleteAllRoutines() //todo let's not leave this in for final release eh?
            Toast.makeText(presenter, "Deleted all routines", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = AddExerciseActivity::class.java.simpleName
    }
}