package com.example.fromdeskhelper.data.Network

import com.apollographql.apollo3.api.ApolloResponse
import com.example.fromdeskhelper.CategoriasQuery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApiClient {
    @POST("/Login/")
    suspend fun getComprobate():Response<String>

}



interface LoginApiClientGraphql {
    suspend fun getComprobateAcount():ApolloResponse<CategoriasQuery.Data>
}