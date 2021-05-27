package com.example.aptitudefitnesstracker.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.DialogUtils
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private val session: Session by lazy { application as Session }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        toolbar.title = title

        /* Control click listeners */
        linearChangeTheme.setOnClickListener {
            setTheme()
        }
        linearFontSize.setOnClickListener {
            setAppFont()
        }
        linearChangeFontFamily.setOnClickListener {
            setFontFamily()
        }

        linearResetPass.setOnClickListener {
            resetPassword()
        }
        linearSignOut.setOnClickListener {
            signOut()
        }
    }

    /**
     * Modify theme
     */
    private fun setTheme() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@SettingsActivity,
                R.string.theme_edit
            )
        val materialDialog: MaterialDialog = builder.customView(R.layout.dialog_theme_default, true)
            .onPositive { dialog, _ ->
                val view: View? = dialog.customView
                val radioGroup = view!!.findViewById<RadioGroup>(R.id.radio_group_themes)
                val selectedId = radioGroup.checkedRadioButtonId
                val radioButton = view.findViewById<RadioButton>(selectedId)
                val themeName = radioButton.text.toString()
                ThemeUtils.saveTheme(this@SettingsActivity, themeName)
                recreate()
            }
            .build()
        val radioGroup: RadioGroup =
            materialDialog.customView!!.findViewById(R.id.radio_group_themes)
        val rb = radioGroup
            .getChildAt(
                ThemeUtils.getSelectedThemePosition(this@SettingsActivity)
            ) as RadioButton
        rb.isChecked = true
        materialDialog.show()
    }

    /**
     * Change font
     */
    private fun setAppFont() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@SettingsActivity,
                R.string.font_edit
            )
        val materialDialog: MaterialDialog = builder.customView(R.layout.dialog_font_size, true)
            .onPositive { dialog, _ ->
                val view: View? = dialog.customView
                val radioGroupFont = view!!.findViewById<RadioGroup>(R.id.radio_group_font)
                val selectedFontId = radioGroupFont.checkedRadioButtonId
                val radioFontButton = view.findViewById<RadioButton>(selectedFontId)
                val fontName = radioFontButton.text.toString()
                Log.e("PLPLP", fontName)
                ThemeUtils.saveFontSize(this@SettingsActivity, fontName)
                recreate()
            }
            .build()
        val radioGroupFont: RadioGroup =
            materialDialog.customView!!.findViewById(R.id.radio_group_font)
        val rbutton = radioGroupFont
            .getChildAt(
                ThemeUtils.getSelectedFontPosition(this@SettingsActivity)
            ) as RadioButton
        rbutton.isChecked = true
        materialDialog.show()
    }

    /**
     * Change font family
     */
    private fun setFontFamily() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@SettingsActivity,
                R.string.edit_font_family
            )
        val materialDialog: MaterialDialog = builder.customView(R.layout.dialog_font_family, true)
            .onPositive { dialog, _ ->
                val view: View? = dialog.customView
                val radioGroupFont = view!!.findViewById<RadioGroup>(R.id.radio_group_fontFamily)
                val selectedFontId = radioGroupFont.checkedRadioButtonId
                val radioFontButton = view.findViewById<RadioButton>(selectedFontId)
                val fontFamilyName = radioFontButton.text.toString()
                Log.e("PLPLP", fontFamilyName)
                ThemeUtils.saveFontFamily(this@SettingsActivity, fontFamilyName)
                recreate()
            }
            .build()
        val radioGroupFont: RadioGroup =
            materialDialog.customView!!.findViewById(R.id.radio_group_fontFamily)
        val rbutton = radioGroupFont
            .getChildAt(
                ThemeUtils.getSelectedFontFamilyPosition(this@SettingsActivity)
            ) as RadioButton
        rbutton.isChecked = true
        materialDialog.show()
    }

    private fun resetPassword() {
        val builder: MaterialDialog.Builder =
            DialogUtils.createCustomDialogWithoutContent(
                this@SettingsActivity,
                R.string.send_password_reset_email
            )
        val materialDialog: MaterialDialog =
            builder.customView(R.layout.dialog_reset_password, true)
                .onPositive { dialog, _ ->
                    val email = dialog.email_input.text.toString()

                    session.sendPasswordResetEmail(email) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@SettingsActivity,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@SettingsActivity,
                                "Failed to send password reset email.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                .build()
        materialDialog.show()
    }

    private fun signOut() {
        val materialDialog: MaterialDialog =
            DialogUtils.createCustomDialogWithoutContent(
                this@SettingsActivity,
                R.string.sign_out_confirmation
            ).onPositive { _, _ ->
                session.signOut()
                finish()
            }.build()
        materialDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SettingsActivity, RoutineListActivity::class.java))
        finish()
    }
}