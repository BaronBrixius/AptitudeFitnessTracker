package com.example.aptitudefitnesstracker.presentation.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R
import com.example.aptitudefitnesstracker.presentation.DialogUtils
import com.example.aptitudefitnesstracker.presentation.ThemeUtils
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    /*Set context of the current class*/
    private var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setThemeApp(this)
        ThemeUtils.setAppFont(this)
        ThemeUtils.setAppFontFamily(this)
        setContentView(R.layout.activity_settings)
        mActivity = this as Activity

        setSupportActionBar(toolbar)
        toolbar.title = title

        /* Control click listner which is used for make action */
        linearChangeTheme.setOnClickListener {
            setTheme()
        }
        linearFontSize.setOnClickListener {
            setAppFont()
        }
        linearChangeFontFamily.setOnClickListener {
            setFontFamily()
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
                mActivity!!.recreate()
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
                mActivity!!.recreate()
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
                mActivity!!.recreate()
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SettingsActivity, RoutineListActivity::class.java))
        finish()
    }
}