package com.example.aptitudefitnesstracker.presentation.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.settings.DialogUtils
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils

class LoginActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        btnSignup = findViewById<View>(R.id.btn_signup) as Button
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button

        btnSignup!!.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btnReset!!.setOnClickListener { resetPassword() }

        btnLogin!!.setOnClickListener { loginButtonPressed() }
    }

    private fun loginButtonPressed() {
        val email = inputEmail!!.text.toString()
        val password = inputPassword!!.text.toString()

        if (checkLoginInputs(email, password)) {
            session.authenticateLogin(email, password) { task ->
                if (task.isSuccessful) {
                    finish()
                }
            }
        }
    }

    private fun incorrectEmailPopUp() {
        Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
    }

    private fun incorrectPasswordPopUp() {
        Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
    }

    private fun displayLoadingCircle() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun checkLoginInputs(email: String, password: String): Boolean {
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

    private fun resetPassword() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@LoginActivity,
                R.string.send_password_reset_email
            )
        val materialDialog: MaterialDialog =
            builder.customView(R.layout.dialog_reset_password, true)
                .onPositive { dialog, _ ->
                    val emailInput = dialog.findViewById(R.id.email_input) as EditText
                    val email = emailInput.text.toString()

                    session.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                            displayPopup("Password reset email sent.")
                        else
                            displayPopup("Failed to send password reset email.")
                    }
                }.build()

        materialDialog.show()
    }

    private fun displayPopup(text: String) {
        Toast.makeText(this@LoginActivity, text, Toast.LENGTH_LONG).show()
    }

}