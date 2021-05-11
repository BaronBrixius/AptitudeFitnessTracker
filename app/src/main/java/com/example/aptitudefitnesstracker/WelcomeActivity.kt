package com.example.aptitudefitnesstracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aptitudefitnesstracker.presentation.Presenter
import com.example.aptitudefitnesstracker.presentation.activities.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onStart(){
        super.onStart()
        Presenter.displayLoginPage(this)
    }


}