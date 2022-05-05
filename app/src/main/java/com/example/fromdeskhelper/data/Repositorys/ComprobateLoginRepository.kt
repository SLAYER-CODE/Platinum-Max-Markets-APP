package com.example.fromdeskhelper.data.Repositorys

import com.example.fromdeskhelper.data.Network.LoginService
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.model.LoginIntProvider
import javax.inject.Inject

class ComprobateLoginRepository @Inject constructor(private val api:LoginService,private val userLogin:LoginIntProvider){
    suspend fun comprobate(string: String):Privilegies{
        val response = api.comprobateLogin()
        if(response=="Hola") return Privilegies.CLIENTE
        userLogin.LoginUserComprobate=Privilegies.CLIENTE
        return  Privilegies.NULL
    }
}