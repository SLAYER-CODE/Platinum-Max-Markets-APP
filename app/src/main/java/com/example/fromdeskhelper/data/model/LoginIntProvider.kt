package com.example.fromdeskhelper.data.model

import com.example.fromdeskhelper.data.Privilegies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginIntProvider @Inject constructor(){
    var LoginUserComprobate:Privilegies = Privilegies.NULL
    var token:String?=null
}