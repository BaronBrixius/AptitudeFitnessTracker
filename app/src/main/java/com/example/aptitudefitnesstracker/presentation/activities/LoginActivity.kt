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
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.databinding.ActivityLoginBinding
import com.example.aptitudefitnesstracker.presentation.DialogUtils
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class LoginActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this) // for set theme
        ThemeUtils.setAppFont(this) // for set font size
        ThemeUtils.setAppFontFamily(this) // for set font family

        binding = ActivityLoginBinding.inflate(layoutInflater)

        // set the view now
        setContentView(R.layout.activity_login)
//        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)

        binding.btnSignup.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (checkLoginInputs(email, password)) {
                val listener: ((Task<AuthResult>) -> Unit) =
                    { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, RoutineListActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                session.authenticateLogin(email, password, listener)
            }
        }
    }

    fun incorrectEmailPopUp() {
        Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
            .show()
    }

    fun incorrectPasswordPopUp() {
        Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()

    }

    fun displayLoadingCircle() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun checkLoginInputs(email: String, password: String): Boolean {
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
                    val email = findViewById<EditText>(R.id.email_input).text.toString()
                    session.sendPasswordResetEmail(email) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Failed to send password reset email.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                .build()
        materialDialog.show()
    }
}