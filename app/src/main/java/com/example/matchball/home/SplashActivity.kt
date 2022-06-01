package com.example.matchball.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.matchball.R
import com.example.matchball.signin.GoogleSignInActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        Handler().postDelayed({
            if (user != null) {
                val signUpIntent = Intent(this, MainActivity::class.java)
                startActivity(signUpIntent)
                finish()
            } else {
                val mainUpIntent = Intent(this, GoogleSignInActivity::class.java)
                startActivity(mainUpIntent)
                finish()
            }
        }, 3000)
    }
}