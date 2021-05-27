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
import com.example.aptitudefitnesstracker.databinding.ActivitySignupBinding
import com.example.aptitudefitnesstracker.presentation.DialogUtils
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.perf.metrics.AddTrace

class SignupActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var auth: FirebaseAuth? = null
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_signup)

        binding = ActivitySignupBinding.inflate(layoutInflater)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }

        binding.signInButton.setOnClickListener {
            finish()
        }

        binding.signUpButton.setOnClickListener {
            SignUp()
        }
    }

    /*
    SignUp() needs to be refactored into separate methods in Presenter and Session classes
     */
    @AddTrace(name = "SignUp")
    private fun SignUp() {
        val email = binding.email.text.toString().trim { it <= ' ' }
        val password = binding.password.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(
                applicationContext,
                "Password too short, enter minimum 6 characters!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        //create user
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@SignupActivity,
                OnCompleteListener<AuthResult?> { task ->
                    Toast.makeText(
                        this@SignupActivity,
                        "createUserWithEmail:onComplete:" + task.isSuccessful,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity, "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity(Intent(this@SignupActivity, RoutineListActivity::class.java))
                        finish()
                    }
                })
    }

    private fun resetPassword() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@SignupActivity,
                R.string.send_password_reset_email
            )
        val materialDialog: MaterialDialog =
            builder.customView(R.layout.dialog_reset_password, true)
                .onPositive { dialog, _ ->
                    val email = findViewById<EditText>(R.id.email_input).text.toString()
                    session.sendPasswordResetEmail(email) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@SignupActivity,
                                "Failed to send password reset email.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                .build()
        materialDialog.show()
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
    }
}
