package com.example.fromdeskhelper.data.Network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.DefaultUpload
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.AgregateProductMutation
import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ComprobationAdminQuery
import com.example.fromdeskhelper.ComprobationConnectionQuery
import com.example.fromdeskhelper.ComprobationEmployedQuery
import com.example.fromdeskhelper.ComprobationQuery
import com.example.fromdeskhelper.ComprobationRootQuery
import com.example.fromdeskhelper.ComprobationWorkedQuery
import com.example.fromdeskhelper.GetDataAsociateQuery
import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.type.ProductsInput
import com.example.fromdeskhelper.type.Upload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ServicesGraph @Inject constructor(private val grapql: ApolloClient?) {
    suspend fun ComprobateConect():ApolloResponse<ComprobationConnectionQuery.Data>?{
        return withContext(Dispatchers.IO) {
            grapql?.query(ComprobationConnectionQuery())?.execute();
        }
    }

    suspend fun GetRoles():ApolloResponse<GetRolesAdminQuery.Data>?{
        return withContext(Dispatchers.IO) {
            try {
                grapql?.query(GetRolesAdminQuery())?.execute();
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun GetDataUser():ApolloResponse<GetDataAsociateQuery.Data>?{
        return withContext(Dispatchers.IO) {
            try {
                grapql?.query(GetDataAsociateQuery())?.execute();
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }
    suspend fun CategoriesGetService(): ApolloResponse<CategoriasQuery.Data>? {
        return withContext(Dispatchers.IO) {
            try {
                grapql?.query<CategoriasQuery.Data>(CategoriasQuery())?.execute();
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }
    suspend fun MarcasGetService():ApolloResponse<BrandsQuery.Data>?{
        return withContext(Dispatchers.IO){
            try{
                grapql?.query<BrandsQuery.Data>(BrandsQuery())?.execute()
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun GetProductsPreviewAll():ApolloResponse<ProductsPreviewQuery.Data>?{
        return withContext(Dispatchers.IO){
            try{
                var res = grapql?.query<ProductsPreviewQuery.Data>(ProductsPreviewQuery())?.execute()
                res
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun ComprobationWorked():ApolloResponse<ComprobationWorkedQuery.Data>?{
        return withContext(Dispatchers.IO){
            try {
                var res = grapql?.query(ComprobationWorkedQuery())?.execute()
                res
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun ComprobationAdmin():ApolloResponse<ComprobationAdminQuery.Data>?{
        return withContext(Dispatchers.IO){
            try {
                var res = grapql?.query(ComprobationAdminQuery())?.execute()
                res
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun ComprobationRoot():ApolloResponse<ComprobationRootQuery.Data>?{
        return withContext(Dispatchers.IO){
            try {
                var res = grapql?.query(ComprobationRootQuery())?.execute()
                res
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }
    suspend fun ComprobateEmployed():ApolloResponse<ComprobationEmployedQuery.Data>?{
        return withContext(Dispatchers.IO){
            try {
                var res = grapql?.query<ComprobationEmployedQuery.Data>(ComprobationEmployedQuery())
                    ?.execute()
                return@withContext res
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }
    suspend fun Comprobate(token: String):ApolloResponse<ComprobationQuery.Data>?{
        return withContext(Dispatchers.IO){
            var res = grapql?.query<ComprobationQuery.Data>( ComprobationQuery() )?.execute()
            return@withContext res
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