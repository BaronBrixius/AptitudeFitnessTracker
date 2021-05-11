package com.example.aptitudefitnesstracker.application

import android.widget.EditText

class Session {
    //User loggedInUser? = null
    var routineList:List<Routine>? = null

    fun isLoggedIn():Boolean {
        return false
    }

    fun logIn(inputEmail: EditText, inputPassword: EditText):Boolean{
        //TODO Implement login function
        return true
    }

}