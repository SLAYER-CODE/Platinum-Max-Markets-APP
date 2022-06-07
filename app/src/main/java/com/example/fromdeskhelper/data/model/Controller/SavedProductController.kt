package com.example.fromdeskhelper.data.model.Controller

import Data.ImagenesNew
import Data.Producto
import com.apollographql.apollo3.ApolloClient
import com.example.fromdeskhelper.data.model.base.ProductoData
import com.example.fromdeskhelper.type.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SavedProductController @Inject constructor(
       private val DaoProduct:ProductoData
) {
       suspend fun saveProduct(Producter:Data.Producto):Int{
              return withContext(Dispatchers.IO) {
                     return@withContext DaoProduct.insertAll(Producter).toInt()
              }
       }



       suspend fun saveImage(Imager: ImagenesNew){
              val resultKeys= withContext(Dispatchers.IO){
                     DaoProduct.insertAllImages(Imager)
              }
       }
}
