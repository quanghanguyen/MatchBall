package com.example.matchball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matchball.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var signInBinding: ActivitySignInBinding
    private lateinit var fireBaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        signInBinding.tvSignup.setOnClickListener {
            GoSignUp()
        }

        signInBinding.btnSignIn.setOnClickListener {
            SignIn()
        }

    }

    private fun SignIn() {
        val email : String = signInBinding.emailEt.text.toString()
        val password : String = signInBinding.passET.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            fireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.sign_in_fail, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
        }
    }

    private fun GoSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}