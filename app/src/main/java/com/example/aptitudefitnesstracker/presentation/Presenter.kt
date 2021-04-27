package com.example.aptitudefitnesstracker.presentation

import com.example.aptitudefitnesstracker.application.Session

//singleton class to manage GUI stuff and link to application data
object Presenter {
    var session: Session? = null
    var settings: Settings? = null

    init {

    }

}