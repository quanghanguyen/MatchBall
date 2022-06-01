package com.example.matchball.usersetting

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.firebaseconnection.StorageConnection
import java.io.File

class UserFragmentViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val readUserImage = MutableLiveData<UserImage>()
    val readUserInfo = MutableLiveData<UserInfo>()

    sealed class UserImage() {
        class ReadSuccess(val image : Bitmap) : UserImage()
        class ReadError() : UserImage()
    }

    sealed class UserInfo() {
        class ReadSuccess(val teamName: String, val teamBio : String, val email : String, val phone : String) : UserInfo()
        class ReadError(val message : String) : UserInfo()
    }

    fun handleReadUserInfo() {
        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val teamName = it.child("teamName").value.toString()
                val teamBio = it.child("teamBio").value.toString()
                val email = it.child("email").value.toString()
                val phone = it.child("phone").value.toString()

                readUserInfo.postValue(UserInfo.ReadSuccess(teamName, teamBio, email, phone))

            } else {
                readUserInfo.postValue(UserInfo.ReadError("Please Create Your Team Information"))
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    fun handleReadUserImage() {
        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.storageReference.getReference("Users").child(uid).getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            readUserImage.postValue(UserImage.ReadSuccess(bitmap))
        }.addOnFailureListener {
            readUserImage.postValue(UserImage.ReadError())
        }
    }
}