package com.example.matchball.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("teamName")
    var teamName : String,
    @SerializedName("teamBio")
    var teamBio : String,
    @SerializedName("email")
    var birthday : String,
    @SerializedName("phone")
    var phone : String
        )

