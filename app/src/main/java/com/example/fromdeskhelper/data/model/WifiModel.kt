package com.example.fromdeskhelper.data.model

import com.google.gson.annotations.SerializedName

data class WifiModel(
    @SerializedName("objWifi") val objWifi:String,
    @SerializedName("author") val author:String
    )

