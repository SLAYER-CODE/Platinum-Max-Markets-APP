package com.example.fromdeskhelper.core.di

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.core.SockInterceptor
import com.example.fromdeskhelper.core.socket.ServerConnection
import com.example.fromdeskhelper.core.static.AppConst
import com.example.fromdeskhelper.core.static.Collections
import com.example.fromdeskhelper.data.LocalNetwork.QuoteApiLocalServer
import com.google.gson.Gson
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
//import com.mongodb.client.MongoClient
//import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.annotation.Nullable
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    var PROVIDER_INTERNAL_INTERCEPTOR = "LOCAL_SUBINTERNAL_INTERCEPTOR"
    var PROVIDER_INTERNAL = "LOCAL_INTERNAL"
    @Provides
    @Singleton
    fun MongoClientRetorner():MongoClient{
        val mongoclient =  MongoClientURI(AppConst.BASE_DATABASE_URL)
        return MongoClient(mongoclient)
    }
    @Provides
    @Singleton
    fun MongoDadaBaseRetorned(Client:MongoClient):MongoDatabase{
        val mongoClient = Client.getDatabase(Collections.BASE_DATABASE_LOCAL)
        return mongoClient
    }
    @Singleton
    @Provides
    @Nullable
    @Named("HttpLocal")
    fun provederOkHttpClientLocal ():OkHttpClient?{
        try {
            return OkHttpClient.Builder()
                .readTimeout(500,TimeUnit.SECONDS)    .connectTimeout(500, TimeUnit.SECONDS)

                .retryOnConnectionFailure(true)

//                .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
//                .protocols(listOf(Protocol.HTTP_1_1))

//                .addInterceptor(authorizationInterceptor)
                .build()
        }catch (ex:Exception){
            Log.e(PROVIDER_INTERNAL,"LocalModuleError",ex)
            return null
        }
    }
    //SERVER LOCAL 2023
    @Singleton
    @Provides
    @Nullable
    @Named("ServerLocal")
    fun providerRetrofitLocal( @Named("HttpLocal") http : OkHttpClient?): Retrofit?{
        var rester =
            OkHttpClient().
        newBuilder().readTimeout(500, TimeUnit.SECONDS).
        connectTimeout(500, TimeUnit.SECONDS).
        retryOnConnectionFailure(true).addInterceptor(object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                try{
                    Log.i(PROVIDER_INTERNAL_INTERCEPTOR,"SERVIDOR ENCONTRADO REGRESANDO LISTENER")
                    return chain.proceed(chain.request());
                }catch(e:Exception){
                    Log.i(PROVIDER_INTERNAL_INTERCEPTOR,e.toString())
                    return Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(999)
                        .message("Sin respuesta")
                        .body(ResponseBody.create(null, "{${e}}")).build()
                }
            }
        }).build()
        try {
            Log.i(PROVIDER_INTERNAL_INTERCEPTOR,"INICIANDO CONEXION LOCAL")
            return http?.let {
                Retrofit.Builder().client(http)
                    .baseUrl(AppConst.BASE_URL_SERVER_LOCAL).addConverterFactory(GsonConverterFactory.create(Gson())).build()
            }
        }catch (ex:Exception){
            Log.e(PROVIDER_INTERNAL_INTERCEPTOR,"UBO UN ERROR DE CONEXION",ex)
            return null
        }
    }


    @Singleton
    @Provides
    @Nullable
    fun providerApiLocal(@Named("ServerLocal") retrofit: Retrofit?):QuoteApiLocalServer?{
        try {
            return retrofit?.create(QuoteApiLocalServer::class.java)
        }catch (ex:Exception){
            Log.e(PROVIDER_INTERNAL,"UBO UN ERRO DE PETICIONES",ex)
            return null
        }
    }

    //Esta es la conexion interecptada si es necesario si no hace falta entonces continua


    @Singleton
    @Provides
    fun provideSocketConnection(@Named("HttpLocal") Http:OkHttpClient?) : ServerConnection?{
//        val token = runBlocking (Dispatchers.IO) {  }
//        val tok = token!!.token
//        val refreshToken = token.refresh_token
        try {
            return ServerConnection(AppConst.BASE_URL_SERVER_SOCKET_LOCAL, http = Http)
        }catch (ex:Exception){
            return null
        }
    }

}