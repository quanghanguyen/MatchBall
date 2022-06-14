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
    val getNameAndPhone = MutableLiveData<GetNameAndPhone>()

    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay: Int = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    sealed class SendRequestResult {
        class SendResultOk(val successMessage : String) : SendRequestResult()
        class SendResultError(val errorMessage: String) : SendRequestResult()
    }

    sealed class GetNameAndPhone {
        class GetResultOk(val name : String, val phone : String) : GetNameAndPhone()
        class GetResultError(val errorMessage : String) : GetNameAndPhone()
    }

    fun handleNameAndPhone() {
        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            val teamName =  it.child("teamName").value.toString()
            val teamPhone = it.child("phone").value.toString()
            getNameAndPhone.postValue(GetNameAndPhone.GetResultOk(teamName, teamPhone))
        }.addOnFailureListener {
            getNameAndPhone.postValue(GetNameAndPhone.GetResultError("Failed to get Name and Phone"))
        }
    }

    fun handleSendRequest(teamNameReceived : String, matchTime : String, locationReceived: String?,
                          latitudeReceived: String?, longitudeReceived : String?, matchPeople: String?,
                          matchNote : String, teamPhoneReceived : String) {

        val matchRequest = MatchRequest(teamNameReceived, matchTime, locationReceived, latitudeReceived, longitudeReceived,
            matchPeople, matchNote, teamPhoneReceived)

        DatabaseConnection.databaseReference.getReference("MatchRequest").push().setValue(matchRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                sendRequest.postValue(SendRequestResult.SendResultOk("Send Request Success"))
            } else {
                sendRequest.postValue(SendRequestResult.SendResultError("Send Request Fail"))
            }
        }

        DatabaseConnection.databaseReference.getReference("User_MatchRequest").child(uid).push().setValue(matchRequest)
    }
}