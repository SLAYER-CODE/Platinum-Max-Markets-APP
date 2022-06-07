package com.example.fromdeskhelper.data.Network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.CountProductsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UtilsServicesGraph @Inject constructor(private val grapql: ApolloClient) {
    suspend fun GetCount(): ApolloResponse<CountProductsQuery.Data>? {
        return withContext(Dispatchers.IO){
            try {
                return@withContext grapql.query<CountProductsQuery.Data>(CountProductsQuery()).execute()
            }catch (ex:ApolloException){
                return@withContext null
            }
        }
    }
}