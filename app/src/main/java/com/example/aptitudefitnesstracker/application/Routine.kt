package com.example.aptitudefitnesstracker.application

class Routine { //not sure this will end up being needed
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