package com.example.matchball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
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
            goSignUp()
        }

        signInBinding.btnSignIn.setOnClickListener {
            signInCheck()
        }

        passwordFocusListener()
        emailFocusListener()

    }

    private fun signInCheck() {
        val validEmail = signInBinding.emailLayout.helperText == null
        val validPass = signInBinding.passwordLayout.helperText == null

        if (validEmail && validPass) {
            signInSuccess()
        } else {
            signInFail()
        }


    }

    private fun signInFail() {
        Toast.makeText(this, R.string.sign_in_fail, Toast.LENGTH_SHORT).show()
    }

    private fun signInSuccess() {
        val email : String = signInBinding.emailEt.text.toString()
        val password : String = signInBinding.passET.text.toString()

            fireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.sign_in_fail, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun emailFocusListener() {
        signInBinding.emailEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                signInBinding.emailLayout.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = signInBinding.emailEt.text.toString()

        if (emailText.isEmpty()) {
            return "Email cannot empty"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email"
        }

        return null
    }

    private fun passwordFocusListener() {
        signInBinding.passET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                signInBinding.passwordLayout.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = signInBinding.passET.text.toString()

        if (passwordText.isEmpty()) {
            return "Password cannot empty"
        }

        if (passwordText.length < 8)

        {
            return "Minimum 8 Character Password"
        }

        if (!passwordText.matches(".*[A-Z].*".toRegex()))

        {
            return "Must contain 1 Upper-case Character"
        }

        if (!passwordText.matches(".*[a-z].*".toRegex()))

        {
            return "Must contain 1 Lower-case Character"
        }

        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))

        {
            return "Must contain 1 Special Character"
        }
        return null
    }


    private fun goSignUp() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
    }
}