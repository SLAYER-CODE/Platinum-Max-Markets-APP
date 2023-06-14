package com.example.fromdeskhelper.data.Network.Local

import Data.Producto
import android.util.Log
import com.example.fromdeskhelper.core.socket.ServerConnection
import com.example.fromdeskhelper.data.LocalNetwork.QuoteApiLocalServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.WebSocket
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

var SERVER_CONNECTION_LOCAL_SERVICE="CONNECTIONLOCALSERVICE"

class LocalService @Inject constructor(
    private val socketConnection: ServerConnection?){

//    var connection:WebSocket?=null
    suspend fun NewConfig(port:String){
        socketConnection?.ChangePort(port)
    }




    //Sockets

    suspend fun sendMessage(string:String,socket:WebSocket?=null):Boolean{
        return withContext(Dispatchers.IO) {
            try {
                return@withContext socketConnection!!.sendMessage(string)
            }catch (ex:Exception){
                return@withContext false
            }
        }
    }
    suspend fun ConnectListener(listener:ServerConnection.ServerListener): WebSocket? {
        return withContext(Dispatchers.IO) {
//            connection = socketConnection.connect(listener)
//            Log.i(SERVER_CONNECTION_LOCAL_SERVICE,connection.toString())
            try {
                return@withContext socketConnection?.connect(listener)
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }
    suspend fun StatusListener():Boolean{
        return socketConnection?.mStatusHandler != null
    }

}