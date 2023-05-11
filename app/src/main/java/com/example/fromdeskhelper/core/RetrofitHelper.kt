package com.example.fromdeskhelper.core

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getRtrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.17:2016/").
                addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}
