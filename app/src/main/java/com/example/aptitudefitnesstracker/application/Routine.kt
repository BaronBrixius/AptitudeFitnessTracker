package com.example.aptitudefitnesstracker.application

class Routine { //not sure this will end up being needed
    var id:Int = 0
    var name: String = ""
    var tags:List<String>? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    constructor() {}
    constructor(name: String?) {
        if (name != null) {
            this.name = name
        }
    }

    constructor(id: Int, name: String)
    constructor(id: Int, name: String, tags: List<String>)
}