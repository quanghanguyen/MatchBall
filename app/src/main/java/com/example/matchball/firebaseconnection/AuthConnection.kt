package com.example.matchball.firebaseconnection

import com.google.firebase.auth.FirebaseAuth

object AuthConnection {

    val auth = FirebaseAuth.getInstance()
    val authUser = FirebaseAuth.getInstance().currentUser

}