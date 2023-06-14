package com.example.fromdeskhelper.data.LocalNetwork

import com.example.fromdeskhelper.data.model.objects.mongod.ProductsLocalImput
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsModelAdapter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface QuoteApiLocalServer {
    @GET("products")
    suspend fun getAllProducts():Response<List<ProductsModelAdapter>>

//    @POST("ping")
//    suspend fun comprobate():Boolean

    @POST("client/addproduct/")
    suspend fun addproduct(@Body Body: ProductsLocalImput):Response<Boolean>

    @PUT("client/addproduct/{id}")
    suspend fun pingAdd(@Path("id") id:String):Response<ResponseBody>


    @POST("ping")
    suspend fun pingLocal():Response<ResponseBody>
}