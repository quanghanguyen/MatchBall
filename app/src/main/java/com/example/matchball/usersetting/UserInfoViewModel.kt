package com.example.matchball.usersetting

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.firebaseconnection.StorageConnection
import com.example.matchball.model.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UserInfoViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    private lateinit var imgUri : Uri

    val saveUserData = MutableLiveData<SaveUserData>()
    val loadAvatar = MutableLiveData<LoadAvatar>()

    sealed class LoadAvatar {
        class LoadAvatarOk(val avatar : Bitmap) : LoadAvatar()
        class LoadAvatarFail(val message : String) : LoadAvatar()
    }

    sealed class SaveUserData {
        class SaveOk(val message: String) : SaveUserData()
        class SaveFail(val message: String) : SaveUserData()
    }

    fun handleLoadAvatar() {
        val firebaseStorage = FirebaseStorage.getInstance().getReference("Users").child(uid)
        val localFile = File.createTempFile("tempImage", "jpg")
        firebaseStorage.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            loadAvatar.postValue(LoadAvatar.LoadAvatarOk(bitmap))
        }.addOnFailureListener {
            loadAvatar.postValue((LoadAvatar.LoadAvatarFail("Failed to Load your Avatar")))
        }
    }

    fun handleSaveUserData(teamName : String, teamBio : String, teamEmail : String, teamPhone : String) {
        val user = User(teamName, teamBio, teamEmail, teamPhone)
        DatabaseConnection.databaseReference.getReference("Users").child(uid).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                StorageConnection.storageReference.getReference("Users/" + AuthConnection.auth.currentUser?.uid).putFile(imgUri).addOnSuccessListener {
                    saveUserData.postValue(SaveUserData.SaveOk("Save Profile Success"))
                }.addOnFailureListener{
                    saveUserData.postValue(SaveUserData.SaveFail("Failed to Save Profile"))
                }
            }
        }
    }
}