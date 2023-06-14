package com.example.fromdeskhelper.data.Repositorys

import com.example.fromdeskhelper.data.model.objects.Function
import com.example.fromdeskhelper.data.model.objects.User
import javax.inject.Inject

class AnonimeModulesRepository @Inject constructor(){
    suspend fun anonimeModules():MutableList<Int>{
        return mutableListOf(
                Function.Scan,
                Function.Purchase,
                Function.Products,
                Function.Stores,
                Function.Filters,
                Function.Search,
                Function.Information
            )
    }
}