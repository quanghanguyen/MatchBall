package com.example.matchball.usersetting

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.StorageConnection
import java.io.File

class UserAccountViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val loadAvatar = MutableLiveData<UserAvatar>()

    sealed class UserAvatar {
        class LoadAvatarSuccess(val image: Bitmap) : UserAvatar()
        object LoadAvatarFail : UserAvatar()
    }

    fun handleLoadAvatar() {
        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.handleAvatar(uid = uid, localFile =  localFile, onSuccess = {
            loadAvatar.postValue(UserAvatar.LoadAvatarSuccess(it))
        }, onFail = {
            loadAvatar.postValue(UserAvatar.LoadAvatarFail)
        })
    }

}