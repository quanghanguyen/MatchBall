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
    val readUser = MutableLiveData<UserData>()

    sealed class UserData {
        object Loading : UserData()
        class ReadAvatarSuccess(val image : Bitmap) : UserData()
        class ReadAvatarError : UserData()
        class ReadInfoSuccess(val teamName: String, val teamBio : String, val email : String, val phone : String) : UserData()
        class ReadInfoError(val message : String) : UserData()

    }

    fun handleReadUser() {
        readUser.postValue(UserData.Loading)
        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val teamName = it.child("teamName").value.toString()
                val teamBio = it.child("teamBio").value.toString()
                val email = it.child("email").value.toString()
                val phone = it.child("phone").value.toString()

                readUser.postValue(UserData.ReadInfoSuccess(teamName, teamBio, email, phone))

            } else {
                readUser.postValue(UserData.ReadInfoError("Please Create Your Team Information"))
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }

        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.storageReference.getReference("Users").child(uid).getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            readUser.postValue(UserData.ReadAvatarSuccess(bitmap))
        }.addOnFailureListener {
            readUser.postValue(UserData.ReadAvatarError())
        }
    }

}