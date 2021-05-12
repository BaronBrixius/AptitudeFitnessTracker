package com.example.aptitudefitnesstracker.presentation.activities

import android.app.Application
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
import com.example.aptitudefitnesstracker.presentation.Presenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.metrics.AddTrace

class LoginActivity : AppCompatActivity() {
    private val presenter: Presenter by lazy { application as Presenter }
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            presenter.signupButtonPressed()
        }
        btnReset!!.setOnClickListener {
            presenter.resetButtonPressed() //Takes you to AccountActivity

        }
        btnLogin!!.setOnClickListener(View.OnClickListener {
            if(checkLoginInputs(inputEmail!!,inputPassword!!)){
                    presenter.loginButtonPressed(inputEmail!!, inputPassword!!)
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
    fun checkLoginInputs(inputEmail: EditText, inputPassword: EditText): Boolean {
        val email = inputEmail!!.text.toString()
        val password = inputPassword!!.text.toString()

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