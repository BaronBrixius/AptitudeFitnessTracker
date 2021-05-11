package com.example.aptitudefitnesstracker.presentation

import android.os.Build
import android.os.Bundle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import com.example.aptitudefitnesstracker.WelcomeActivity
import com.example.aptitudefitnesstracker.application.Session
import com.example.aptitudefitnesstracker.presentation.activities.LoginActivity
import com.example.aptitudefitnesstracker.presentation.activities.RoutineListActivity
import com.example.aptitudefitnesstracker.presentation.activities.context


//singleton class to manage GUI stuff and link to application data




    object Presenter {
        var session: Session? = null
        var settings: Settings? = null
        var currentActivity: Intent? = null


        init {

        }

        fun displayLoginPage(context: Context) {
            currentActivity = Intent(context, LoginActivity::class.java)
            context.startActivity(currentActivity)
        }


        fun LoginButtonPressed(inputEmail: EditText, inputPassword: EditText) {
        if (session.logIn(inputEmail, inputPassword)) {
            currentActivity = Intent(RoutineListActivity., RoutineListActivity::class.java)

        } else {
            loginActivity.loginFailed()
        }
        }

    }

    fun LoginButtonPressed(inputEmail: EditText, inputPassword: EditText) {
//        if (session.logIn(inputEmail, inputPassword)) {
//            val intent = Intent(loginActivity, RoutineListActivity::class.java)
//            loginActivity.startActivity(intent)
//            loginActivity.finish()
//        } else {
//            loginActivity.loginFailed()
//        }
    }

//    private void finishFunction() {
//        Activity activity = (Activity)getContext();
//        activity.finish();
//    }

