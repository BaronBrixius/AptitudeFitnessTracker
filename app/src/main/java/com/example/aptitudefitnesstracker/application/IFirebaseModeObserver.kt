package com.example.aptitudefitnesstracker.application

interface IFirebaseModeObserver {
    fun notify(mode: Boolean)   //True if FirebaseMode turns on, False if it turns off
}