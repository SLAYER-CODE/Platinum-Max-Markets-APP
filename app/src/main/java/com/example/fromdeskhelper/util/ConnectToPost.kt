package com.example.fromdeskhelper.util

import android.util.Log
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders

import okhttp3.OkHttpClient
import okhttp3.Request



object ConnectToPost {
    suspend fun ConnectAndGet(QRcode: String):Map<Int,String>{
        val Segundo:Map<Int,String> = mutableMapOf()
        var mURL = ("https://www.google.com/search?q=$QRcode&tbm=isch")
        mURL = ("https://www.google.com/search?q=7750075042896&tbm=isch")
        val client = OkHttpClient() // Crear objeto OkHttpClient
        val her= mapOf<String,String>(Pair("origin", "www.google.com"),
            Pair("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36"),
        Pair("host","www.google.com"))
        val header:Headers= her.toHeaders()
        val request: Request = Request.Builder().headers(header)
            .url(mURL).get() // Solicitar enlace
            .build() // Crear objeto de solicitud
        val response =  client.newCall(request).execute() // Obtener la respuesta de nuestra soliciutd
        if(response.isSuccessful) {
            println("Respuesta ${response.code}")
            var respuesta = response.body?.string().toString()
            val regex = "Apache-2.0 \\w{20}".toRegex()
            val match = regex.find(respuesta )
            if(match != null) {
                println("No")
                println(match.value)
                println(match.groupValues)
                println(match.range)
            }
            Log.i("ResCLACLA", "Respuesta $respuesta}")
        }
        return  Segundo;
    }
}