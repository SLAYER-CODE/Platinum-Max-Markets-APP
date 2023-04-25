package com.example.fromdeskhelper.data.Network

import android.util.Log
import com.example.fromdeskhelper.core.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject

class LoginService @Inject constructor(private val retrofit: LoginApiClient?) {
    suspend fun comprobateLogin():String?{
        return  withContext(Dispatchers.IO) {
            try {
                val response = retrofit?.getComprobate()
                response?.body() ?: ""
            }catch (x:Exception){
                return@withContext null
            }
        }
    }
}