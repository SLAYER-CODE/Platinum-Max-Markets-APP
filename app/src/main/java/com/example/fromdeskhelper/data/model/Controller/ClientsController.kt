package com.example.fromdeskhelper.data.model.Controller

import Data.*
import androidx.lifecycle.LiveData
import com.example.fromdeskhelper.data.model.base.ProductoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject



class ClientsController @Inject constructor(
    private val DaoProduct: ProductoData
) {

    suspend fun saveClient(Client:Data.ClientList):Int{
        return withContext(Dispatchers.IO) {
            return@withContext DaoProduct.insertClient(Client).toInt()
        }
    }

    suspend fun setRelationProduct(relation:ClientAndProducts){
        return withContext(Dispatchers.IO) {
            return@withContext DaoProduct.insertProductUser(relation)
        }
    }

    suspend fun productsGetClient(GetClinet:Int):ClientToProduct{
        return withContext(Dispatchers.IO){
            return@withContext DaoProduct.getByInventarioUser(GetClinet)
        }
    }


    suspend fun productsRemoveClient(GetClinet:Int,Product:Int){
        return withContext(Dispatchers.IO){
            return@withContext DaoProduct.removeByInventarioUser(GetClinet,Product)
        }
    }




    fun GetProductsAll(): LiveData<List<ClientListGet>> {
        return DaoProduct.getClientList()
    }

}