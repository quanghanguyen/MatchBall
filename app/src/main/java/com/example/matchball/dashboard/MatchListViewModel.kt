package com.example.matchball.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.model.MatchRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MatchListViewModel : ViewModel() {

    val matchListResult = MutableLiveData<MatchListResult>()

    sealed class MatchListResult {
        object Loading : MatchListResult()
        class ResultOk(val matchList : ArrayList<MatchRequest>) : MatchListResult()
        class ResultError(val errorMessage : String) : MatchListResult()
    }

    fun handleMatchList() {
        matchListResult.postValue(MatchListResult.Loading)
        DatabaseConnection.databaseReference.getReference("MatchRequest").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listMatch=ArrayList<MatchRequest>()
                    for (requestSnapshot in snapshot.children) {
                        requestSnapshot.getValue(MatchRequest::class.java)?.let {
                            listMatch.add(it)
                        }
                    }
                    matchListResult.postValue(MatchListResult.ResultOk(listMatch))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                matchListResult.postValue(MatchListResult.ResultError("Failed to load Data"))
            }
        })
    }
}