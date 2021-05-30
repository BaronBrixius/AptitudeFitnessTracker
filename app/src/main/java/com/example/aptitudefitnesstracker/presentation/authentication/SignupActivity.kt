package com.example.aptitudefitnesstracker.presentation.authentication

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
import com.example.aptitudefitnesstracker.presentation.settings.DialogUtils
import com.example.aptitudefitnesstracker.presentation.settings.ThemeUtils
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var progressBar: ProgressBar? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_signup)

        btnSignIn = findViewById(R.id.sign_in_button)
        btnSignUp = findViewById(R.id.sign_up_button)
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        btnResetPassword = findViewById(R.id.btn_reset_password)

        btnResetPassword!!.setOnClickListener { resetPassword() }
        btnSignIn!!.setOnClickListener { finish() }
        btnSignUp!!.setOnClickListener {
            val auth = FirebaseAuth.getInstance()

            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
            finish()

        }
    }



    private fun checkInputs(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            displayPopup("Enter email address!")
            return true
        }
        if (TextUtils.isEmpty(password)) {
            displayPopup("Enter password!")
            return true
        }
        if (password.length < 6) {
            displayPopup("Password too short, enter minimum 6 characters!")
            return true
        }
        return false
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
        Toast.makeText(this@SignupActivity, text, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}