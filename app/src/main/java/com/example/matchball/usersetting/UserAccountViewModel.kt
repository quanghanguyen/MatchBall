package com.example.matchball.usersetting

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.StorageConnection
import java.io.File

class UserAccountViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val loadData = MutableLiveData<UserData>()

    sealed class UserData {
        class LoadAvatarSuccess(val image: Bitmap) : UserData()
        class LoadEmailSuccess(val email : String) : UserData()
        object LoadDataFail : UserData()
    }

    fun handleLoadAvatar() {
        val email = AuthConnection.authUser?.email
        loadData.postValue(email?.let
        {UserData.LoadEmailSuccess(it)})

        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.handleAvatar(uid = uid, localFile =  localFile, onSuccess = {
            loadData.postValue(UserData.LoadAvatarSuccess(it))
        }, onFail = {
            loadData.postValue(UserData.LoadDataFail)
        })
    }

}