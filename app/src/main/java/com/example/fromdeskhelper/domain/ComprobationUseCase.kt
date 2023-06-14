package com.example.fromdeskhelper.domain

import com.example.fromdeskhelper.data.Network.ServicesGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

var LOG_INFO="USECASECOMPROBATE";
class ComprobationUseCase @Inject constructor(
    private val cliente: ServicesGraph?
){

    suspend fun connected():Boolean?{
        return withContext(Dispatchers.IO){
            try {
                return@withContext cliente?.ComprobateConect()?.data?.comprobationConnection
            }catch (err:Exception){
                return@withContext null
            }
        }
    }
    suspend operator fun invoke(): Boolean?{
//        var clientes =ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
//            .okHttpClient(okHttpClient)
//            .build()
        return  withContext(Dispatchers.IO) {

//            var caller=ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
//            .okHttpClient(okHttpClient)
//                .build()

//            var primero = cliente?.query(ComprobateUserQuery(token))?.execute()
//            if(primero==null){
//                return@withContext false
//            }
            return@withContext cliente?.ComprobationWorked ()?.data?.comprobationWorked;
//            return@withContext cliente?.Comprobate(token)?.data?.comprobation //[OBSOLETE] Comprueba mediaten el token
        }
    }

    suspend fun Cemployed():Boolean?{
        return withContext(Dispatchers.IO){
            return@withContext cliente?.ComprobateEmployed ()?.data?.comprobationEmployed
        }
    }

    suspend fun Cadmin():Boolean?{
        return withContext(Dispatchers.IO){
            return@withContext cliente?.ComprobationAdmin()?.data?.comprobationAdmin
        }
    }

    suspend fun Croot():Boolean?{
        return withContext(Dispatchers.IO){
            return@withContext cliente?.ComprobationRoot()?.data?.comprobationRoot
        }
    }
}