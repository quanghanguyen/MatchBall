package com.example.matchball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        signUpBinding.btnSignUp.setOnClickListener {
            SignUp()
        }
    }

    private fun SignUp() {
        val email : String = signUpBinding.emailEt.text.toString()
        val password : String = signUpBinding.passET.text.toString()
        val confirmPass : String = signUpBinding.passConfirmET.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (password.equals(confirmPass)) {

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this, R.string.sign_up_success, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, R.string.sign_up_fail, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, R.string.pass_not_match, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show()
        }
    }
}