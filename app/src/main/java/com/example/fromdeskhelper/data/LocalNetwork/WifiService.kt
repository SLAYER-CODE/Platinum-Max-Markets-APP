package com.example.fromdeskhelper.data.LocalNetwork

import com.example.fromdeskhelper.data.model.WifiModel
import javax.inject.Inject

class WifiService @Inject constructor(private val api:WifiApiClient) {
    suspend fun getP2p():List<WifiModel>{
        val response = api.getAllQuotes()
        //val response = retrofit.create(WifiApiClient::class.java).getAllQuotes()
        return response.body()?: emptyList();
    }
}