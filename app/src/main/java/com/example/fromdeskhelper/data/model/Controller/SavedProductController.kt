package com.example.fromdeskhelper.data.model.Controller

import Data.ImagenesProduct
import com.example.fromdeskhelper.data.model.base.ProductoData
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


       suspend fun saveProductALl(Producter:Data.Producto):Int{
              return withContext(Dispatchers.IO) {
                     return@withContext DaoProduct.insertProductAndImages(Producter)
              }
       }


       suspend fun saveImage(Imager: ImagenesProduct){
              val resultKeys= withContext(Dispatchers.IO){
                     DaoProduct.insertAllImages(Imager)
              }
       }
}
