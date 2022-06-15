package com.example.matchball.signin.google

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
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