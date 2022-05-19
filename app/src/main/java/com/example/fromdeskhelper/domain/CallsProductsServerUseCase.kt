package com.example.fromdeskhelper.domain

import com.apollographql.apollo3.api.ApolloResponse
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.data.Network.ServicesGraph
import javax.inject.Inject

class CallsProductsServerUseCase @Inject constructor(
    private val cliente: ServicesGraph
){
    suspend fun PreviewCallUser():ProductsPreviewQuery.Data?{
        return cliente.GetProductsPreviewAll().data
    }
}
