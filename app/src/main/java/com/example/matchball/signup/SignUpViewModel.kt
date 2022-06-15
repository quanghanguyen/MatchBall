package com.example.matchball.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection

class SignUpViewModel : ViewModel() {

    val signUpResult = MutableLiveData<SignUpResult>()
//    private val _loginOk by lazy {MutableLiveData<LoginResult>()}
//    val signUpResult : LiveData<LoginResult> = _loginOk

    sealed class SignUpResult {
        object Loading : SignUpResult()
        class LoginOk(val message: String) : SignUpResult()
        class LoginError(val message: String) : SignUpResult()
    }

    fun handleSignUp(email: String, password: String) {
        signUpResult.postValue(SignUpResult.Loading)
        AuthConnection.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpResult.postValue(SignUpResult.LoginOk("Sign Up Success"))
            } else {
                signUpResult.postValue(SignUpResult.LoginError("Sign Up Fail"))
            }
        }
    }
}