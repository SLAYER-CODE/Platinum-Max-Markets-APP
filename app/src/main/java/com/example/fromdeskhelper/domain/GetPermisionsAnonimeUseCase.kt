package com.example.fromdeskhelper.domain

import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.data.Network.ServicesGraph
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Repositorys.AnonimeModulesRepository
import com.example.fromdeskhelper.data.Repositorys.ComprobateLoginRepository
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.data.model.LoginIntProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPermisionsAnonimeUseCase @Inject constructor(private val default:AnonimeModulesRepository) {
    suspend fun getRoles(): MutableList<GetRolesAdminQuery.Fuction>{
        return  withContext(Dispatchers.IO) {
            var faste_or_anonime = mutableListOf<GetRolesAdminQuery.Fuction>()
            for (x in default.anonimeModules()){
                var a:GetRolesAdminQuery.Fuction=GetRolesAdminQuery.Fuction(x.toString(),"",false,true)
                faste_or_anonime.add(a)
            }
            faste_or_anonime
        }
    }
}