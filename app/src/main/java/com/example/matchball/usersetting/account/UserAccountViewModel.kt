package com.example.matchball.usersetting.account

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.firebaseconnection.StorageConnection
import java.io.File

class UserAccountViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val loadData = MutableLiveData<UserData>()
    val verifyEmail = MutableLiveData<VerifyEmail>()

    sealed class UserData {
        class LoadAvatarSuccess(val image: Bitmap) : UserData()
        class LoadEmailSuccess(val email : String) : UserData()
        object LoadDataFail : UserData()
    }

    sealed class VerifyEmail {
        class EmailVerifySuccess(val successMessage : String) : VerifyEmail()
        class EmailVerifyFail(val errorMessage : String) : VerifyEmail()
    }

    fun handleLoadAvatar() {
        val email = authUser?.email
        loadData.postValue(email?.let
        { UserData.LoadEmailSuccess(it) })

        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.handleAvatar(uid = uid, localFile =  localFile, onSuccess = {
            loadData.postValue(UserData.LoadAvatarSuccess(it))
        }, onFail = {
            loadData.postValue(UserData.LoadDataFail)
        })
    }

    fun handleVerifyEmail() {
        authUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                verifyEmail.postValue(VerifyEmail.EmailVerifySuccess("Send Verify Email Success"))
            } else {
                verifyEmail.postValue(VerifyEmail.EmailVerifyFail("Send Verify Email Fail"))
            }
        }
    }

}