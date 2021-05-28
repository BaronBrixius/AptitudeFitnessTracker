package com.example.aptitudefitnesstracker.presentation

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.aptitudefitnesstracker.R

object DialogUtils {
    /**
     * Creates a material dialog with given title
     * @param activity - activity instance
     * @param title - dialog title resource id
     * @return - material dialog builder
     */
    fun createCustomDialogWithoutContent(
        activity: Activity?,
        title: Int
    ): MaterialDialog.Builder {
        return MaterialDialog.Builder(activity!!)
            .title(title)
            .positiveText(R.string.ok)
            .negativeText(R.string.cancel)
    }
}