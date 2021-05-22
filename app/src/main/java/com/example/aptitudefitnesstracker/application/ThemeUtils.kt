package com.example.aptitudefitnesstracker.application

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.aptitudefitnesstracker.R

object ThemeUtils {
    /**
     * Set selected theme to current context
     *
     * @param context - current context
     */
    fun setThemeApp(context: Context) {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val themeName = mSharedPreferences.getString(
            Constants.DEFAULT_THEME_TEXT,
            Constants.DEFAULT_THEME
        ) ?: return
        when (themeName) {
            Constants.THEME_WHITE -> context.setTheme(R.style.AppThemeWhite)
            Constants.THEME_BLACK -> context.setTheme(R.style.AppThemeBlack)
            Constants.THEME_DARK -> context.setTheme(R.style.ActivityThemeDark)
        }
    }

    /**
     * get position of selected theme
     *
     * @param context - current context
     * @return - position
     */
    fun getSelectedThemePosition(context: Context?): Int {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val themeName = mSharedPreferences.getString(
            Constants.DEFAULT_THEME_TEXT,
            Constants.DEFAULT_THEME
        )
        when (themeName) {
            Constants.THEME_BLACK -> return 0
            Constants.THEME_DARK -> return 1
            Constants.THEME_WHITE -> return 2
        }
        return 0
    }

    /**
     * Save given theme to shared prefs
     *
     * @param context   - current context
     * @param themeName - name of theme to save
     */
    fun saveTheme(context: Context?, themeName: String) {
        Log.e("SAVE_THEME", themeName)
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = mSharedPreferences.edit()
        editor.putString(Constants.DEFAULT_THEME_TEXT, themeName)
        editor.apply()
    }

    /**
     * Set selected font to current context
     *
     * @param context - current context
     */
    fun setAppFont(context: Context) {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val fontName = mSharedPreferences.getString(
            Constants.DEFAULT_APP_FONT,
            Constants.DEFAULT_FONT
        ) ?: return
        when (fontName) {
            Constants.VERY_SMALL_FONT -> context.setTheme(R.style.FontSizeVerySmall)
            Constants.SMALL_FONT -> context.setTheme(R.style.FontSizeSmall)
            Constants.NORMAL_FONT -> context.setTheme(R.style.FontSizeNormal)
            Constants.BIG_FONT -> context.setTheme(R.style.FontSizeBig)
            Constants.VERY_BIG_FONT -> context.setTheme(R.style.FontSizeVeryBig)
        }
    }

    /**
     * get position of selected theme
     *
     * @param context - current context
     * @return - position
     */
    fun getSelectedFontPosition(context: Context?): Int {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val fontName = mSharedPreferences.getString(
            Constants.DEFAULT_APP_FONT,
            Constants.DEFAULT_FONT
        )
        when (fontName) {
            Constants.VERY_SMALL_FONT -> return 0
            Constants.SMALL_FONT -> return 1
            Constants.NORMAL_FONT -> return 2
            Constants.BIG_FONT -> return 3
            Constants.VERY_BIG_FONT -> return 4
        }
        return 0
    }

    /**
     * Save given font to shared prefs
     *
     * @param context  - current context
     * @param fontName - name of font to save
     */
    fun saveFontSize(context: Context?, fontName: String) {
        Log.e("SAVE_FONT", fontName)
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = mSharedPreferences.edit()
        editor.putString(Constants.DEFAULT_APP_FONT, fontName)
        editor.apply()
    }

    /**
     * Set selected font to current context
     *
     * @param context - current context
     */
    fun setAppFontFamily(context: Context) {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val fontFamilyName = mSharedPreferences.getString(
            Constants.DEFAULT_APP_FONT_FAMILY,
            Constants.DEFAULT_FONT_FAMILY
        ) ?: return
        when (fontFamilyName) {
            Constants.ROBOTO_FONT -> context.setTheme(R.style.TextViewCustomFontDefault)
            Constants.CALIBRI_FONT -> context.setTheme(R.style.TextViewCustomFontOne)
            Constants.COMIC_FONT -> context.setTheme(R.style.TextViewCustomFontTwo)
            Constants.HELVETICA_FONT -> context.setTheme(R.style.TextViewCustomFontThree)
        }
    }

    /**
     * get position of selected theme
     *
     * @param context - current context
     * @return - position
     */
    fun getSelectedFontFamilyPosition(context: Context?): Int {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val fontName = mSharedPreferences.getString(
            Constants.DEFAULT_APP_FONT_FAMILY,
            Constants.DEFAULT_FONT_FAMILY
        )
        when (fontName) {
            Constants.ROBOTO_FONT -> return 0
            Constants.CALIBRI_FONT -> return 1
            Constants.COMIC_FONT -> return 2
            Constants.HELVETICA_FONT -> return 3
        }
        return 0
    }

    /**
     * Save given font to shared prefs
     *
     * @param context    - current context
     * @param fontFamily - name of font to save
     */
    fun saveFontFamily(context: Context?, fontFamily: String) {
        Log.e("SAVE_FONT", fontFamily)
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = mSharedPreferences.edit()
        editor.putString(Constants.DEFAULT_APP_FONT_FAMILY, fontFamily)
        editor.apply()
    }
}