package com.example.matchball.createrequest

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.home.MainActivity
import com.example.matchball.model.MatchRequest

class RequestViewModel : ViewModel() {

    val uid = AuthConnection.auth.currentUser!!.uid

    val getNameAndPhone = MutableLiveData<GetNameAndPhoneResult>()
    val sendRequest = MutableLiveData<SendRequestResult>()

    sealed class GetNameAndPhoneResult {
        class ResultOk(val teamName : String, val teamPhone : String) : GetNameAndPhoneResult()
        class ResultError(val errorMessage : String) : GetNameAndPhoneResult()
    }

    sealed class SendRequestResult {
        class SendResultOk(val successMessage : String) : SendRequestResult()
        class SendResultError(val errorMessage: String) : SendRequestResult()
    }

    fun handleNameAndPhone() {
        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            val teamNameReceived =  it.child("teamName").value.toString()
            val teamPhoneReceived = it.child("phone").value.toString()
            getNameAndPhone.postValue(GetNameAndPhoneResult.ResultOk(teamNameReceived, teamPhoneReceived))
        }.addOnFailureListener {
            getNameAndPhone.postValue(GetNameAndPhoneResult.ResultError("Failed to get Name and Phone"))
        }
    }

    fun handleSendRequest(teamNameReceived : String, matchTime : String, locationReceived: String?,
                          latitudeReceived: String?, longitudeReceived : String?, matchPeople: String,
                          matchNote : String, teamPhoneReceived : String) {

        val matchRequest = MatchRequest(teamNameReceived, matchTime, locationReceived, latitudeReceived, longitudeReceived,
            matchPeople, matchNote, teamPhoneReceived)

        if (uid != null) {
            DatabaseConnection.databaseReference.getReference("MatchRequest").push().setValue(matchRequest).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendRequest.postValue(SendRequestResult.SendResultOk("Send Request Success"))
                } else {
                    sendRequest.postValue(SendRequestResult.SendResultError("Send Request Fail"))
                }
            }
        }
    }

}