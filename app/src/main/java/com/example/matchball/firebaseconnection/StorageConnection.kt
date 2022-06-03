package com.example.matchball.firebaseconnection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.matchball.usersetting.UserAccountViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object StorageConnection {

    val storageReference = FirebaseStorage.getInstance()

    fun handleAvatar(blala:String?=null,uid:String, localFile:File,onSuccess:(Bitmap)->Unit, onFail:(Exception)->Unit)
    {

        storageReference.getReference("Users").child(uid).getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                onSuccess(bitmap)
            }.addOnFailureListener {
                onFail(it)
            }

//        StorageConnection.storageReference.getReference("Users").child(uid).getFile(uri).addOnSuccessListener {
//            val bitmap = BitmapFactory.decodeFile(absolutePath)
//            onSuccess(bitmap)
//            //loadAvatar.postValue(UserAccountViewModel.UserAvatar.LoadAvatarSuccess(bitmap))
//        }.addOnFailureListener{
//            onFail(it)
//            //loadAvatar.postValue(UserAccountViewModel.UserAvatar.LoadAvatarFail)
//        }
    }
}