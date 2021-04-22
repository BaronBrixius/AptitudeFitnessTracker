package com.example.aptitudefitnesstracker.application

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Exercise {
    var id:Int? = null
    var name: String? = null
    var tags:List<String>? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    constructor() {}
    constructor(name: String?) {
        this.name = name
    }
}