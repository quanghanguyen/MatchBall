package com.example.matchball.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.R
import com.example.matchball.signin.password.SignInActivity
import com.example.matchball.databinding.ActivitySignUpBinding
import com.example.matchball.model.validateLengthPassword
import com.example.matchball.model.validateLowerCasePassword
import com.example.matchball.model.validateSpecialPassword
import com.example.matchball.model.validateUpperCasePassword

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private val signUpViewModel : SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        initObserve()
        signUpCheck()
        emailFocusListener()
        passwordFocusListener()
        passwordConfirmFocusListener()

    }

    private fun initObserve() {
        signUpViewModel.signUpResult.observe(this, { result ->
            signUpBinding.signUpSwipe.isRefreshing = false
            when (result) {
                is SignUpViewModel.SignUpResult.LoginOk -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignUpViewModel.SignUpResult.LoginError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is SignUpViewModel.SignUpResult.Loading -> {
                    signUpBinding.signUpSwipe.isRefreshing = true
                }
            }
        })
    }

    private fun signUpCheck() {
        signUpBinding.btnSignUp.setOnClickListener{
            val email: String = signUpBinding.emailEt.text.toString()
            val password: String = signUpBinding.passET.text.toString()
            val validEmail = signUpBinding.tvEmailSignUp.helperText == null
            val validPass = signUpBinding.tvPasswordSignUp.helperText == null
            val validConfirmPass = signUpBinding.tvConfirmPasswordSignUp.helperText == null

            if (validEmail && validPass && validConfirmPass)
                signUpViewModel.handleSignUp(email, password)
            else
                Toast.makeText(this, R.string.sign_up_fail, Toast.LENGTH_SHORT).show()
        }
    }

    private fun emailFocusListener() {
        signUpBinding.emailEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                signUpBinding.tvEmailSignUp.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = signUpBinding.emailEt.text.toString()

        if (emailText.isEmpty()) {
            return "Email cannot empty"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email"
        }

        return null
    }

    // Password

    private fun passwordFocusListener() {
        signUpBinding.passET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                signUpBinding.tvPasswordSignUp.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = signUpBinding.passET.text.toString()

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

    private fun passwordConfirmFocusListener() {
        signUpBinding.passConfirmET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                signUpBinding.tvConfirmPasswordSignUp.helperText = validConfirmPassword()
            }
        }
    }

    private fun validConfirmPassword(): String? {
        val confirmPass = signUpBinding.passConfirmET.text.toString()
        val passWord = signUpBinding.passET.text.toString()

        if (confirmPass.isEmpty()) {
            return "Confirm password cannot empty"
        }

        if (confirmPass != passWord) {
            return "Confirm password not match"
        }
        return null
    }


}