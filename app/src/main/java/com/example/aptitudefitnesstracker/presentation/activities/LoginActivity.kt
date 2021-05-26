package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session

import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this) // for set theme
        ThemeUtils.setAppFont(this) // for set font size
        ThemeUtils.setAppFontFamily(this) // for set font family
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
//        if (auth!!.currentUser != null) {
//            startActivity(Intent(this@LoginActivity, AccountActivity::class.java))
//            finish()
//        }

        // set the view now
        setContentView(R.layout.activity_login)
//        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        btnSignup = findViewById<View>(R.id.btn_signup) as Button
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()


        btnSignup!!.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        btnReset!!.setOnClickListener {
            // presenter.resetButtonPressed() //Takes you to AccountActivity
            var intent = Intent(this, SignupActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        btnLogin!!.setOnClickListener(View.OnClickListener {

            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()

            if (checkLoginInputs(email, password)) {
                val listener: ((Task<AuthResult>) -> Unit) =
                    { task -> //todo may want the activity to come up with its own callback?
                        if (task.isSuccessful) {
                            val intent = Intent(this, RoutineListActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }

                session.authenticateLogin(email, password, listener)
            }
        })
    }

    fun incorrectEmailPopUp() {
        Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
            .show()
    }

    fun incorrectPasswordPopUp() {
        Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()

    }

    fun displayLoadingCircle() {
        progressBar!!.visibility = View.VISIBLE
    }

    //TODO Should this method be moved to the presenter class?
    fun checkLoginInputs(email: String, password: String): Boolean {
        //val email = inputEmail!!.text.toString()
        //val password = inputPassword!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            incorrectEmailPopUp()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            incorrectPasswordPopUp()
            return false
        }
        displayLoadingCircle()
        return true
    }

}