package com.example.matchball.firebaseconnection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object StorageConnection {

    val storageReference = FirebaseStorage.getInstance()

    fun handleAvatar(blala : String? = null , uid:String, localFile:File, onSuccess:(Bitmap) -> Unit, onFail:(Exception) -> Unit)
    {

        storageReference.getReference("Users").child(uid).getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                onSuccess(bitmap)
            }.addOnFailureListener {
                onFail(it)
            }
    }
}