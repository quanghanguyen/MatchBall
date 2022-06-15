package com.example.matchball.signin.google

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.home.MainActivity
import com.example.matchball.R
import com.example.matchball.databinding.ActivityIntroBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.signin.password.SignInActivity
import com.example.matchball.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import java.io.File

class GoogleSignInActivity : AppCompatActivity() {

    private lateinit var googleSignInBinding: ActivityIntroBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    private val googleSignInViewModel : GoogleSignInViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(googleSignInBinding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        initObserve()
        initEvent()
    }

    private fun initObserve() {
        googleSignInViewModel.googleSignIn.observe(this, { result ->
            when (result) {
                is GoogleSignInViewModel.GoogleSignInResult.SignInOk -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is GoogleSignInViewModel.GoogleSignInResult.SignInError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is GoogleSignInViewModel.GoogleSignInResult.Loading -> {
                }
            }
        })
    }

    private fun initEvent() {
        goSignUp()
        goSignIn()
        googleSignIn()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = AuthConnection.auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    Toast.makeText(this, "Sign-in Success", Toast.LENGTH_SHORT).show()
                    googleSignInViewModel.handleGoogleLoading(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                    Toast.makeText(this, "Sign-in Fail", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    companion object {
        const val TAG = "GoogleActivity"
        const val RC_SIGN_IN = 100
    }

    private fun googleSignIn() {
        googleSignInBinding.btnContinueGoogle.setOnClickListener {
            signIn()
        }
        val localFile = File.createTempFile("tempImage", "jpg")

    }

    private fun goSignIn() {
        googleSignInBinding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goSignUp() {
        googleSignInBinding.btnAnotherContinue.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}