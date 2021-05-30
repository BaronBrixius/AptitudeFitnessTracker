package com.example.aptitudefitnesstracker.presentation.routines

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
import com.example.aptitudefitnesstracker.application.data.Routine
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.authentication.LoginActivity
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils

class EditRoutineActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var txtDetails: TextView? = null
    private var inputName: EditText? = null
    private var btnSave: Button? = null
    private var userId: String? = null
    private var btnDelete: Button? = null
    private var btnShare: Button? = null
    private var btnDownload: Button? = null



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
        btnShare = findViewById<View>(R.id.btn_share) as Button
        btnDownload = findViewById<View>(R.id.btn_download) as Button
        var routine = session.activeRoutine
        inputName!!.setText(routine!!.name)


        println(session.firebaseMode)

        if(session.firebaseMode){
            btnDelete!!.visibility = View.GONE
            btnShare!!.visibility = View.GONE
        } else {
            btnDownload!!.visibility = View.GONE

        }




        // Save / update the user
        btnSave!!.setOnClickListener {
            val name = inputName!!.text.toString()

            // Check for already existed userId
            if (TextUtils.isEmpty(userId)) {
                routine.name = name
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

        btnShare!!.setOnClickListener{
            if(session.userIsLoggedIn()){
            session.share(routine)
            }
            else{
                val loginRequiredDialog = AlertDialog.Builder(this)
                loginRequiredDialog.setTitle("Share Routine")
                loginRequiredDialog.setMessage("To share a routine you must be logged in. Would you like to login now?")

                loginRequiredDialog.setPositiveButton("Login") { dialog, which ->
                    if (routine != null) {
                        session.deleteRoutine(routine)
                    }
                    finish()
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                }
                loginRequiredDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                    finish()
                }
                loginRequiredDialog.show()
            }

        }

        btnDelete!!.setOnClickListener{
            session.saveRemoteRoutineLocally(routine)
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