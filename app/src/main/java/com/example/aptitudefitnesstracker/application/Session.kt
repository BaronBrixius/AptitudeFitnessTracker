package com.example.aptitudefitnesstracker.application

class Session {
    //User loggedInUser? = null
    var routineList:List<Routine>? = null
    var settings:Settings? = null

    fun isLoggedIn():Boolean {
        return false
    }
}