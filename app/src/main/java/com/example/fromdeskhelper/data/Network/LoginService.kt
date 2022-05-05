package com.example.fromdeskhelper.data.Network

import android.util.Log
import com.example.fromdeskhelper.core.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create
import javax.inject.Inject

class LoginService @Inject constructor(private val retrofit: LoginApiClient) {
    suspend fun comprobateLogin():String{
        return  withContext(Dispatchers.IO) {
            val response = retrofit.getComprobate()
            response.body() ?: ""
        }
    }
}