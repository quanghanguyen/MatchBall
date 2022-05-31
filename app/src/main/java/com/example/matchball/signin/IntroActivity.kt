package com.example.matchball.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matchball.home.MainActivity
import com.example.matchball.R
import com.example.matchball.databinding.ActivityIntroBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class IntroActivity : AppCompatActivity() {

    private lateinit var introBinding: ActivityIntroBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    val googleSignInViewModel : IntroViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(introBinding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnEmailClick()
        tvSignInClick()

        introBinding.btnContinueGoogle.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = AuthConnection.auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    Toast.makeText(this, "Sign-in Success", Toast.LENGTH_SHORT).show()
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    Toast.makeText(this, "Sign-in Fail", Toast.LENGTH_SHORT).show()
                }
        }

//        googleSignInViewModel.googleSignIn.observe(this, Observer { signInResult ->
//            when (signInResult) {
//                is IntroViewModel.GoogleSignInResult.SignInOk -> {
//
//                    Toast.makeText(this, signInResult.message, Toast.LENGTH_SHORT).show()
//                    firebaseAuthWithGoogle(account.idToken!!)
//                }
//
//                is IntroViewModel.GoogleSignInResult.SignInError -> {
//                    Toast.makeText(this, signInResult.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//
//        googleSignInViewModel.handleGoogleSuccess(requestCode, resultCode, data)

    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        googleSignInViewModel.googleSignIn.observe(this, Observer { result ->
            when (result) {
                is IntroViewModel.GoogleSignInResult.SignInOk -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is IntroViewModel.GoogleSignInResult.SignInError -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        googleSignInViewModel.handleGoogleLoading(idToken)
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

    private fun tvSignInClick() {
        introBinding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnEmailClick() {
        introBinding.btnAnotherContinue.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}