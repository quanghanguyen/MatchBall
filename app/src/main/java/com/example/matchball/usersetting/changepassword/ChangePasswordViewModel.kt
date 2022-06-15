package com.example.matchball.usersetting.changepassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.signin.google.GoogleSignInActivity
import com.google.firebase.auth.EmailAuthProvider

class ChangePasswordViewModel : ViewModel() {

    val changePasswordResult = MutableLiveData<ChangePassWordResult>()

    sealed class ChangePassWordResult {
        class ResultOk(val successMessage : String) : ChangePassWordResult()
        class ResultError(val errorError : String) : ChangePassWordResult()
    }

    fun handleChangePassWord(email : String, oldPassword : String, newPassword : String) {
        val credential = EmailAuthProvider
            .getCredential(email, oldPassword)
        AuthConnection.authUser?.reauthenticate(credential)
            ?.addOnCompleteListener{
                if (it.isSuccessful) {
                    AuthConnection.authUser.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(GoogleSignInActivity.TAG, "User password updated.")
                            }
                        }
                    changePasswordResult.postValue(ChangePassWordResult.ResultOk("Change Password Success"))
                    AuthConnection.auth.signOut()
                } else {
                    changePasswordResult.postValue(ChangePassWordResult.ResultError("Change Password Failed"))
                }
            }
    }

}