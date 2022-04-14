package com.example.finalproductos.data.LocalNetwork

import com.example.finalproductos.data.model.WifiModel
import retrofit2.Response
import retrofit2.http.GET

interface WifiApiClient {
    @GET("/.json")
    suspend fun getAllQuotes(): Response<List<WifiModel>>
}