package com.example.matchball.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.matchball.R
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.signin.google.GoogleSignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (authUser != null) {
                val signUpIntent = Intent(this, MainActivity::class.java)
                startActivity(signUpIntent)
                finish()
            } else {
                val mainUpIntent = Intent(this, GoogleSignInActivity::class.java)
                startActivity(mainUpIntent)
                finish()
            }
        }, 2000)
    }
}