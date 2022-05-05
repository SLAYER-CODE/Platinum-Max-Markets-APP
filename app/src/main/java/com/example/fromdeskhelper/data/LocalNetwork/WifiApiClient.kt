package com.example.fromdeskhelper.data.LocalNetwork

import com.example.fromdeskhelper.data.model.WifiModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response
import retrofit2.http.GET

interface WifiApiClient {
    @GET("/.json")
    suspend fun getAllQuotes(): Response<List<WifiModel>>

}
