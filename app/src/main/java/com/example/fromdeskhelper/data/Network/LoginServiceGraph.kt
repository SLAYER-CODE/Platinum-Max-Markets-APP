package com.example.fromdeskhelper.data.Network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.example.fromdeskhelper.CategoriasQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginServiceGraph @Inject constructor(
    private val services:ServicesGraph) {
    suspend fun ComprobateLogin():ApolloResponse<CategoriasQuery.Data> {
        return withContext(Dispatchers.IO) {
            services.CategoriesGetService()
        }
    }
}