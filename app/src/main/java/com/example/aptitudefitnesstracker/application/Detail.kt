package com.example.aptitudefitnesstracker.application

class Detail() {

    var name = ""
    var value = ""

    fun updateName(newName: String) {
        name = newName
    }

    fun updateValue(newValue: String) {
        value = newValue
    }

    fun updateValue(newValue: Int) {
        value = newValue.toString()
    }

    constructor(name: String, value: Int) : this() {
        this.name = name
        this.value = value.toString()
    }

    constructor(name: String, value: String) : this() {
        this.name = name
        this.value = value
    }
}