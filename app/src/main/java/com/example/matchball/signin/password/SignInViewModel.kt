package com.example.matchball.signin.password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection

class SignInViewModel : ViewModel() {

    val signInResult = MutableLiveData<SignInResult>()

    sealed class SignInResult {
        object Loading : SignInResult()
        class SignInOk(val message : String) : SignInResult()
        class SignInError(val message: String) : SignInResult()
    }

    fun handleSignIn(email : String, password : String) {
        signInResult.postValue(SignInResult.Loading)
        AuthConnection.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInResult.postValue(SignInResult.SignInOk("Sign In Success"))
            } else {
                signInResult.postValue(SignInResult.SignInError("Sign In Fail"))
            }
        }
    }

}