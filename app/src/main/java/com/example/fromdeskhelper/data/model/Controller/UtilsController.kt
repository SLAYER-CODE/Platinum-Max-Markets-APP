package com.example.fromdeskhelper.data.model.Controller

import Data.ImagenesProduct
import androidx.lifecycle.LiveData
import com.example.fromdeskhelper.data.model.base.ProductoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UtilsController @Inject constructor(
    private val DaoProduct:ProductoData){
    fun GetCountItems():LiveData<Int>{
        return DaoProduct.getCount()
    }

    suspend fun saveImage(Imager: ImagenesProduct){
        val resultKeys= withContext(Dispatchers.Main){
            DaoProduct.insertAllImages(Imager)
        }
    }
}
