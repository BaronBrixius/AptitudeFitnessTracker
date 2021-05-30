package com.example.aptitudefitnesstracker.presentation.routines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.authentication.LoginActivity
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils

class EditRoutineActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var txtDetails: TextView? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null
    private var btnDelete: Button? = null
    private var btnShare: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_edit_routine)

        txtDetails = findViewById<View>(R.id.txt_user) as TextView
        inputName = findViewById<View>(R.id.name) as EditText
        btnSave = findViewById<View>(R.id.btn_save) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button
        btnShare = findViewById<View>(R.id.btn_share) as Button

        val routine = session.activeRoutine
        inputName!!.setText(routine!!.name)

        // Save / update the user
        btnSave!!.setOnClickListener {
            routine.name = inputName!!.text.toString()
            session.updateRoutine(routine)
        }

        btnDelete!!.setOnClickListener {
            val saveDialog = AlertDialog.Builder(this)
            saveDialog.setTitle("Delete Routine")
            saveDialog.setMessage("Are you sure you want to delete Routine?")

            saveDialog.setPositiveButton("Delete") { _, _ ->
                session.deleteRoutine(routine)
                finish()
                intent = Intent(this, RoutineListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            saveDialog.setNegativeButton("No") { _, _ ->
            }
            saveDialog.show()
        }

        btnShare!!.setOnClickListener {
            if (session.userIsLoggedIn()) {
                session.share(routine)
            } else {
                val loginRequiredDialog = AlertDialog.Builder(this)
                loginRequiredDialog.setTitle("Share Routine")
                loginRequiredDialog.setMessage("To share a routine you must be logged in. Would you like to login now?")

                loginRequiredDialog.setPositiveButton("Login") { _, _ ->
                    session.share(routine)
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                loginRequiredDialog.setNegativeButton(android.R.string.no) { _, _ ->
                }
                loginRequiredDialog.show()
            }

        }
    }
}