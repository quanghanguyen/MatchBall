package com.example.matchball.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchRequest (
    @SerializedName("teamName")
    val teamName : String? = "",
    @SerializedName("time")
    val time : String? = "",
    @SerializedName("pitch")
    val pitch : String? = "",
    @SerializedName("pitchLatitude")
    val pitchLatitude : String? = "",
    @SerializedName("pitchLongitude")
    val pitchLongitude : String? = "",
    @SerializedName("people")
    val people : String? = "",
    @SerializedName("note")
    val note : String? = "",
    @SerializedName("phone")
    val phone : String? = "",
        ) : Parcelable