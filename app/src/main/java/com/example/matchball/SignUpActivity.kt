package com.example.matchball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.graphics.toColor
import com.example.matchball.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        emailFocusListener()
        passwordFocusListener()
        passwordConfirmFocusListener()

        signUpBinding.btnSignUp.setOnClickListener {
            signUpCheck()
        }
    }

    private fun signUpCheck() {
        val validEmail = signUpBinding.tvEmailSignUp.helperText == null
        val validPass = signUpBinding.tvPasswordSignUp.helperText == null
        val validConfirmPass = signUpBinding.tvConfirmPasswordSignUp.helperText == null

        if (validEmail && validPass && validConfirmPass)
            signUpSuccess()
        else
            signUpFail()
    }

    private fun signUpFail() {
        Toast.makeText(this, R.string.sign_up_fail, Toast.LENGTH_SHORT).show()
    }

    private fun signUpSuccess() {
        val email: String = signUpBinding.emailEt.text.toString()
        val password: String = signUpBinding.passET.text.toString()
        val confirmPass: String = signUpBinding.passConfirmET.text.toString()

//        if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
//            if (password.equals(confirmPass)) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, R.string.sign_up_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.sign_up_fail, Toast.LENGTH_SHORT).show()
            }
        }

//            } else {
//                Toast.makeText(this, R.string.pass_not_match, Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
//        }
//    }

        //------------------------------------------------

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