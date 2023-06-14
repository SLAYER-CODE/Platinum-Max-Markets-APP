package com.example.fromdeskhelper.data.model.objects

import androidx.camera.core.impl.Identifier
import kotlinx.serialization.Serializable

object Socket {
    const val Message = 1
    const val Alert = 2
    const val Get = 3
    const val Post = 4
    const val Ping= 5
    const val Disconnect = 6
}

object SocktFuctionEvent {
    const val REGISTER="event_register"
    const val SINCRONIZE="event_sincronize"
    const val MESSAGE="event_message"
}


@Serializable
data class Indetifier(
    val id: Int,
    val phone: String,
    val uid:String,
    val time:String,
    val qrUser:String,
    val name:String,
    val email:String,
    val photo:String,
    val maker:String,
    val model:String
    )


@Serializable
data class SendMessage (val event:String , val data: Indetifier)
