package com.example.matchball.yourmatchrequest.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection

class YourRequestDetailsViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val loadData = MutableLiveData<LoadData>()

    sealed class LoadData {
        class ResultOk(
            val time : String,
            val location: String,
            val people : String,
            val note : String
        ) : LoadData()
        class ResultError(val errorMessage : String) : LoadData()
    }

//    fun handleLoadData() {
//        DatabaseConnection.databaseReference.getReference("User_MatchRequest").child(uid).get()
//            .addOnSuccessListener {
//                if (it.exists())
//                {
//                    val time = it.child("time").value.toString()
//                    val location = it.child("pitch").value.toString()
//                    val people = it.child("people").value.toString()
//                    val note = it.child("note").value.toString()
//
//                    loadData.postValue(LoadData.ResultOk(time, location, people, note))
//                } else {
//                    loadData.postValue(LoadData.ResultError("Error"))
//                }
//            }.addOnFailureListener {
//                it.printStackTrace()
//            }
//        }
    }