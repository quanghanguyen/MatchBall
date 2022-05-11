package com.example.matchball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        Handler().postDelayed({
            if (user == null) {
                val signUpIntent = Intent(this, MainActivity::class.java)
                startActivity(signUpIntent)
            } else {
                val mainUpIntent = Intent(this, IntroActivity::class.java)
                startActivity(mainUpIntent)
            }
        }, 3000)

    }
}