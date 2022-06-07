package com.example.fromdeskhelper.data.model.Controller

import Data.ClientList
import Data.ClientListGet
import Data.InventarioProducts
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

    fun GetProductsAll(): LiveData<List<ClientListGet>> {
        return DaoProduct.getClientList()
    }

}