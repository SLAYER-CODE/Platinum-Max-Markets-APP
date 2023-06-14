package com.example.fromdeskhelper.domain

import android.util.Log
import com.example.fromdeskhelper.GetDataAsociateQuery
import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.data.Network.ServicesGraph
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Repositorys.ComprobateLoginRepository
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.data.model.LoginIntProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPermisionsUseCase @Inject constructor(private val loginComprobate:ComprobateLoginRepository, private val cliente: ServicesGraph?) {
    suspend operator fun invoke(token:String):Privilegies{
        return loginComprobate.comprobate(token)
    }

    suspend fun getRoles():GetRolesAdminQuery.Data?{
        return  withContext(Dispatchers.IO) {
            cliente?.GetRoles()?.data
        }
    }

    suspend fun getDataUser(): GetDataAsociateQuery.Data?{
        return  withContext(Dispatchers.IO) {
            return@withContext cliente?.GetDataUser()?.data
        }
    }
}