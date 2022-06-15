package com.example.fromdeskhelper.data.Network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.DefaultUpload
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.AgregateProductMutation
import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.type.ProductsInput
import com.example.fromdeskhelper.type.Upload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ServicesGraph @Inject constructor(private val grapql: ApolloClient?) {
    suspend fun CategoriesGetService(): ApolloResponse<CategoriasQuery.Data>? {
        return withContext(Dispatchers.IO) {
            grapql?.query<CategoriasQuery.Data>(CategoriasQuery())?.execute();
        }
    }
    suspend fun MarcasGetService():ApolloResponse<BrandsQuery.Data>?{
        return withContext(Dispatchers.IO){
            grapql?.query<BrandsQuery.Data>(BrandsQuery())?.execute()
        }
    }


    suspend fun GetProductsPreviewAll():ApolloResponse<ProductsPreviewQuery.Data>?{
        return withContext(Dispatchers.IO){
            var res = grapql?.query<ProductsPreviewQuery.Data>(ProductsPreviewQuery())?.execute()
            res
        }
    }

    suspend fun SendProduct(
        product:ProductsInput,list:MutableList<com.apollographql.apollo3.api.Upload>
    ):ApolloResponse<AgregateProductMutation.Data>?{
        return withContext(Dispatchers.IO) {
//            var testing=ProductsInput(
//                mutableListOf(),
//                mutableListOf(),
//                Optional.presentIfNotNull("Se cargoo"),
//                12.2,
//                12.9,
//                Optional.presentIfNotNull<Double>(1.2),
//                "Carlos",
//                Optional.presentIfNotNull<String>("121212"),
//                Optional.presentIfNotNull<Double>(0.1),
//                Optional.presentIfNotNull<Double>(0.1),
//                Optional.presentIfNotNull(Date().toString())
//            )


            grapql?.mutation(AgregateProductMutation(myval = product, list ))?.execute()
//            grapql.mutation(AgregateProductMutation(myval = testing)).execute()
        }
    }

}