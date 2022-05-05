package com.example.fromdeskhelper.domain

import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Repositorys.ComprobateLoginRepository
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.data.model.LoginIntProvider
import javax.inject.Inject

class GetPermisionsUseCase @Inject constructor(private val loginComprobate:ComprobateLoginRepository) {
    suspend operator fun invoke(token:String):Privilegies{
        return loginComprobate.comprobate(token)
    }
}