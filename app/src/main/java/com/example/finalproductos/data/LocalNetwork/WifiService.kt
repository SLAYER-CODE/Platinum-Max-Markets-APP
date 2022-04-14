package com.example.finalproductos.data.LocalNetwork

import retrofit2.Retrofit
import com.example.finalproductos.core.RetrofitHelper
import com.example.finalproductos.data.model.WifiModel
import javax.inject.Inject

class WifiService @Inject constructor(private val api:WifiApiClient) {
    suspend fun getP2p():List<WifiModel>{
        val response = api.getAllQuotes()
        //val response = retrofit.create(WifiApiClient::class.java).getAllQuotes()
        return response.body()?: emptyList();
    }
}