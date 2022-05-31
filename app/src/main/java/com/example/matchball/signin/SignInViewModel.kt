package com.example.matchball.signin

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.R
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.home.MainActivity

class SignInViewModel : ViewModel() {

    val signInResult = MutableLiveData<SignInResult>()

    sealed class SignInResult {
        object Loading : SignInResult()
        class SignInOk(val message : String) : SignInResult()
        class SignInError(val message: String) : SignInResult()
    }

    fun handleSignIn(email : String, password : String) {
        signInResult.value = SignInResult.Loading
        AuthConnection.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInResult.postValue(SignInResult.SignInOk("Sign In Success"))
            } else {
                signInResult.postValue(SignInResult.SignInError("Sign In Fail"))
            }
        }
    }

}