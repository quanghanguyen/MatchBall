package com.example.matchball.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection

class MainActivityViewModel : ViewModel() {

    val userInfo = MutableLiveData<UserData>()
    private val uid = AuthConnection.authUser?.uid

    sealed class UserData {
        class LoadDataOk(val name : String, val phone : String) : UserData()
        object LoadDataError : UserData()
    }

    fun handleReadUserData() {
        uid?.let {
            DatabaseConnection.databaseReference.getReference("Users").child(it).get().addOnSuccessListener {
                if (it.exists()) {
                    val name = it.child("teamName").value.toString()
                    val phone = it.child("phone").value.toString()

                    userInfo.postValue(UserData.LoadDataOk(name, phone))
                } else {
                    userInfo.postValue(UserData.LoadDataError)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }
}