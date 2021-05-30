package com.example.aptitudefitnesstracker.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Routine
import com.example.aptitudefitnesstracker.application.Session

class EditRoutineActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var txtDetails: TextView? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null
    private var userId: String? = null
    private var btnDelete: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_edit_routine)
        // Displaying toolbar icon
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        txtDetails = findViewById<View>(R.id.txt_user) as TextView
        inputName = findViewById<View>(R.id.name) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button
        var routine = session.activeRoutine

        inputName!!.hint = routine!!.name

        // Save / update the user
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()

            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                routine.name = inputName.toString()
                session.updateRoutine(routine)
            } else {
//                updateExercise(name)
            }
        }

        btnDelete!!.setOnClickListener{

            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Delete Routine")
            saveDialog.setMessage("Are you sure you want to delete Routine?")

            saveDialog.setPositiveButton("Delete") { dialog, which ->
                if (routine != null) {
                    session.deleteRoutine(routine)
                }
                finish()
                intent = Intent(this, RoutineListActivity::class.java)
                startActivity(intent)

            }
            saveDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                finish()
            }
            saveDialog.show()
            btnDelete!!.visibility = View.INVISIBLE

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
    private fun createRoutine(name: String) {
        if (name.isNotEmpty()) {
            session.insertRoutine(Routine(name))
            Toast.makeText(this, "Routine added", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = AddRoutineActivity::class.java.simpleName
    }
}