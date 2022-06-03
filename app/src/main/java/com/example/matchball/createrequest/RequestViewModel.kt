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

    private val uid = AuthConnection.auth.currentUser!!.uid
    val sendRequest = MutableLiveData<SendRequestResult>()

    sealed class SendRequestResult {
        class GetResultOk(val teamName : String, val teamPhone : String) : SendRequestResult()
        class GetResultError(val errorMessage : String) : SendRequestResult()
        class SendResultOk(val successMessage : String) : SendRequestResult()
        class SendResultError(val errorMessage: String) : SendRequestResult()
    }

    fun handleSendRequest(teamNameReceived : String, matchTime : String, locationReceived: String?,
                          latitudeReceived: String?, longitudeReceived : String?, matchPeople: String?,
                          matchNote : String, teamPhoneReceived : String) {

        val matchRequest = MatchRequest(teamNameReceived, matchTime, locationReceived, latitudeReceived, longitudeReceived,
            matchPeople, matchNote, teamPhoneReceived)

        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            val teamName =  it.child("teamName").value.toString()
            val teamPhone = it.child("phone").value.toString()
            sendRequest.postValue(SendRequestResult.GetResultOk(teamName, teamPhone))
        }.addOnFailureListener {
            sendRequest.postValue(SendRequestResult.GetResultError("Failed to get Name and Phone"))
        }

        DatabaseConnection.databaseReference.getReference("MatchRequest").push().setValue(matchRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                sendRequest.postValue(SendRequestResult.SendResultOk("Send Request Success"))
            } else {
                sendRequest.postValue(SendRequestResult.SendResultError("Send Request Fail"))
            }
        }
    }

}