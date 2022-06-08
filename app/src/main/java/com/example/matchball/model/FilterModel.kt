package com.example.matchball.model

import com.google.gson.annotations.SerializedName

data class FilterModel (
    @SerializedName("attribute")
    val attribute : String
        )