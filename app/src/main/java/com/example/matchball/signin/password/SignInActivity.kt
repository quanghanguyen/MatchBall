package com.example.matchball.signin.password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matchball.R
import com.example.matchball.databinding.ActivitySignInBinding
import com.example.matchball.home.MainActivity
import com.example.matchball.model.*
import com.example.matchball.signin.google.GoogleSignInActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var signInBinding: ActivitySignInBinding
    private val signInViewModel : SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        initObserve()
        initEvent()
        passwordFocusListener()
        emailFocusListener()

    }

    private fun initObserve() {
        signInViewModel.signInResult.observe(this, Observer {result ->
            when (result) {
                is SignInViewModel.SignInResult.SignInOk -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignInViewModel.SignInResult.SignInError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is SignInViewModel.SignInResult.Loading -> {
                }
            }
        })
    }

    private fun initEvent() {
        goSignUpActivity()
        signInCheck()
    }

    private fun goSignUpActivity() {
        signInBinding.tvSignup.setOnClickListener {
            val intent = Intent(this, GoogleSignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInCheck() {
        signInBinding.btnSignIn.setOnClickListener {
            val email : String = signInBinding.emailEt.text.toString()
            val password : String = signInBinding.passET.text.toString()
            val validEmail = signInBinding.emailLayout.helperText == null
            val validPass = signInBinding.passwordLayout.helperText == null

            if (validEmail && validPass) {
                signInViewModel.handleSignIn(email, password)
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

        if (passwordText.validateLengthPassword())

        {
            return "Minimum 8 Character Password"
        }

        if (!passwordText.validateUpperCasePassword())

        {
            return "Must contain 1 Upper-case Character"
        }

        if (!passwordText.validateLowerCasePassword())

        {
            return "Must contain 1 Lower-case Character"
        }

        if (!passwordText.validateSpecialPassword())

        {
            return "Must contain 1 Special Character"
        }
        return null
    }
}