package com.example.matchball.signin

import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.R
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.home.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class IntroViewModel : ViewModel() {

    val googleSignIn = MutableLiveData<GoogleSignInResult>()

    sealed class GoogleSignInResult {
        object Loading : GoogleSignInResult()
        object Success : GoogleSignInResult()
        class SignInOk(val message : String) : GoogleSignInResult()
        class SignInError(val message: String) : GoogleSignInResult()
    }

    fun handleGoogleLoading(idToken: String) {
        googleSignIn.value = GoogleSignInResult.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        AuthConnection.auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    googleSignIn.postValue(GoogleSignInResult.SignInOk("Sign In Success"))
                } else {
                    // If sign in fails, display a message to the user.
                    googleSignIn.postValue(GoogleSignInResult.SignInError("Sign In Fail"))
                }
            }
    }

//    fun handleGoogleSuccess(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        googleSignIn.value = GoogleSignInResult.Success
//
//        if (requestCode == IntroActivity.RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                googleSignIn.postValue(GoogleSignInResult.SignInOk("Sign In Success"))
//                IntroActivity.firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                googleSignIn.postValue(GoogleSignInResult.SignInError("Sign In Error"))
//            }
//        }
//
//    }

}