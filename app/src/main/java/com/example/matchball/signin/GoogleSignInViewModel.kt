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

class GoogleSignInViewModel : ViewModel() {

    val googleSignIn = MutableLiveData<GoogleSignInResult>()

    sealed class GoogleSignInResult {
        object Loading : GoogleSignInResult()
        class SignInOk(val message : String) : GoogleSignInResult()
        class SignInError(val message: String) : GoogleSignInResult()
    }

    fun handleGoogleLoading(idToken: String) {
        googleSignIn.postValue(GoogleSignInResult.Loading)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        AuthConnection.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    googleSignIn.postValue(GoogleSignInResult.SignInOk("Sign In Success"))
                } else {
                    googleSignIn.postValue(GoogleSignInResult.SignInError("Sign In Fail"))
                }
            }
    }

}