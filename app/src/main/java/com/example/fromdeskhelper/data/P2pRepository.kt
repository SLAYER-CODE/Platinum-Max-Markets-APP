package com.example.fromdeskhelper.data

import com.example.fromdeskhelper.data.LocalNetwork.WifiService
import com.example.fromdeskhelper.data.model.WifiModel
import com.example.fromdeskhelper.data.model.WifiProvider
import javax.inject.Inject

class P2pRepository @Inject constructor(private val api:WifiService,private val wifiProvider: WifiProvider) {

    suspend fun getAllP2p():List<WifiModel>{
        val response:List<WifiModel> = api.getP2p()
        wifiProvider.wifiModel = response;
        return response;
    }
}