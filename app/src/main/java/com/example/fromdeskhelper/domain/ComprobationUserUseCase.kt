package com.example.fromdeskhelper.domain

import android.util.Log
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ComprobateUserQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

var LOG_INFO="USECASECOMPROBATE";
class ComprobationUserUseCase @Inject constructor(
    private val cliente: ApolloClient
){
    suspend operator fun invoke(token:String):Boolean{
//        var clientes =ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
//            .okHttpClient(okHttpClient)
//            .build()
        return  withContext(Dispatchers.IO) {
//            var caller=ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
//            .okHttpClient(okHttpClient)
//                .build()

            var primero = cliente.query(ComprobateUserQuery(token)).execute()
            var segundo = primero.data.toString()
            Log.i(LOG_INFO,segundo)
            return@withContext primero.data!!.comprabationUser
//            "Caa"
        }
    }


}