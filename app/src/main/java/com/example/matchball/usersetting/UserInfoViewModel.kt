package com.example.matchball.usersetting

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserInfoViewModel : ViewModel() {

    val loadUserAvatar = MutableLiveData<LoadUserAvatar>()
    val saveUserData = MutableLiveData<SaveUserData>()

    sealed class LoadUserAvatar() {
        class LoadOk(val avatar : Bitmap) : LoadUserAvatar()
        class LoadFail(val message : String) : LoadUserAvatar()
    }

    sealed class SaveUserData() {

    }

    fun handleLoadUserData() {

    }

}