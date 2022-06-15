package com.example.matchball.yourmatchrequest.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.model.MatchRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class YourRequestViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val yourRequestListResult = MutableLiveData<YourRequestListResult>()

    sealed class YourRequestListResult {
        class ResultOk(val matchList : ArrayList<MatchRequest>) : YourRequestListResult()
        class ResultError(val errorMessage : String) : YourRequestListResult()
    }

    fun handleYourRequestMatch() {
        DatabaseConnection.databaseReference.getReference("User_MatchRequest").child(uid).addValueEventListener(object :
        ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<MatchRequest>()
                    for (requestSnapshot in snapshot.children) {
                        requestSnapshot.getValue(MatchRequest::class.java)?.let {
                            list.add(0, it)
                        }
                    }
                    yourRequestListResult.postValue(YourRequestListResult.ResultOk(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                yourRequestListResult.postValue(YourRequestListResult.ResultError("Error"))
            }

        })
    }

}