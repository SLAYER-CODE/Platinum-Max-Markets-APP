package com.example.fromdeskhelper.domain.Local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings.Secure
import android.util.Log
import com.example.fromdeskhelper.data.Network.Local.LocalService
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.objects.Indetifier
import com.example.fromdeskhelper.data.model.objects.SendMessage
import com.example.fromdeskhelper.data.model.objects.SocktFuctionEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import javax.inject.Inject


var USE_CASE_SOCKET_FUCTION = "SOCKETFUCTIONUSECASE"
class SocketFuctionUseCase  @Inject constructor(
    private val service: LocalService,
    private val context:Context,
    private val loginPreferences: PreferencesManager,
){
    suspend fun sendString(string: String){
//        if(statusConnect()) {
//        var item = service.connection
//        if(item!=null){
        if(service.sendMessage(string)){
            Log.i(USE_CASE_SOCKET_FUCTION ,"Se envio el mensaje")
        }else{
            Log.i(USE_CASE_SOCKET_FUCTION ,"Ubo un error al enviar")
        }
//        }
//        }
    }

    suspend fun registerFunction(number:String){
        var user= "Undefinend"
        var email= "Undefined"
        var photo:String="null"
        loginPreferences.preferencesUserFlow.collect { it ->
            if(!it.loginInit){
                Log.i(USE_CASE_SOCKET_FUCTION ,"Recolectando datos")
                user = FirebaseAuth.getInstance().currentUser?.displayName?:"Not Locate"
                email = FirebaseAuth.getInstance().currentUser?.email?:"Not Locate"
                photo = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
            }

        val md = MessageDigest.getInstance("SHA-512")
        var uuid= getNumber()
        var qrUser:String = md.digest(uuid.toByteArray()).fold("") { str, it -> str + "%02x".format(it) }
//        Log.i(USE_CASE_SOCKET_FUCTION ,uuid)
//        Log.i(USE_CASE_SOCKET_FUCTION ,qrUser)
        var time = System.currentTimeMillis().toString()
        var serialize:String= Json.encodeToString(
            SendMessage(
                SocktFuctionEvent.REGISTER,
                Indetifier(
                    1,
                    number,
                    uuid,
                    time,
                    qrUser,
                    user,
                    email,
                    photo,
                    Build.MANUFACTURER,
                    Build.MODEL)
            )
        )
        Log.i(USE_CASE_SOCKET_FUCTION ,serialize)

        if(service.sendMessage(serialize)){
            Log.i(USE_CASE_SOCKET_FUCTION ,"Se envio el mensaje")
        }else{
            Log.i(USE_CASE_SOCKET_FUCTION ,"Ubo un error al enviar")
        }
        }
    }
    suspend fun changeConnect(string:String){
        service.NewConfig(string)
    }

    suspend fun statusConnect():Boolean{
        return service.StatusListener()
    }


    @SuppressLint("HardwareIds")
    private fun getNumber():String{
        val id = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        return id
    }
}