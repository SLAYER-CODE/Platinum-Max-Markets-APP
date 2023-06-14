package com.example.fromdeskhelper.domain.Local

import android.util.Log
import com.example.fromdeskhelper.core.socket.ServerConnection
import com.example.fromdeskhelper.data.Network.Local.LocalService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.WebSocket
import javax.inject.Inject

class SocketConnectUseCase  @Inject constructor(private val service:LocalService){
    var SERVERSOCKETSWEB = "SOCKETCONNECTUSECASE"
    suspend operator fun invoke(listener:ServerConnection.ServerListener): WebSocket? {
        return withContext(Dispatchers.IO) {
            try {
                service.ConnectListener(listener)
            }catch (ex:Exception){
                Log.e(SERVERSOCKETSWEB,"Error en el socket",ex)
                return@withContext null
            }
        }
    }
}