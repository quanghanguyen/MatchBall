package com.example.matchball.firebasedatabase

import com.google.firebase.database.FirebaseDatabase

object DatabaseConnection {
    val databaseReference = FirebaseDatabase.getInstance().getReference("MatchRequest")
    //
    //
}

