package com.example.aptitudefitnesstracker.presentation.routines

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.application.data.Routine
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils

class AddRoutineActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var txtDetails: TextView? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_databasetest)
        txtDetails = findViewById<View>(R.id.txt_user) as TextView
        inputName = findViewById<View>(R.id.name) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button

        btnSave!!.text = getString(R.string.action_save)

        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()
            createRoutine(name)
            finish()
        }
    }

    private fun createRoutine(name: String) {
        if (name.isNotEmpty()) {
            session.insertRoutine(Routine(name))
            Toast.makeText(this, "Routine added", Toast.LENGTH_LONG).show()
            finish()
        } else {
            val loginRequiredDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            loginRequiredDialog.setTitle("Invalid Name")
            loginRequiredDialog.setMessage("Routine name cannot be blank, please input a name.")
            loginRequiredDialog.setPositiveButton(android.R.string.ok) { _, _ ->
            }
            loginRequiredDialog.show()
        }
    }
}