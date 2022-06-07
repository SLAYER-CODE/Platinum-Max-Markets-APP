package com.example.fromdeskhelper.data.Repositorys

import com.example.fromdeskhelper.data.Network.LoginService
import com.example.fromdeskhelper.data.model.LoginIntProvider
import javax.inject.Inject

class ClientRepository @Inject constructor(private val api: LoginService, private val userLogin: LoginIntProvider){

}