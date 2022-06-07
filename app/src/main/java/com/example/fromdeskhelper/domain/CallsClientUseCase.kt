package com.example.fromdeskhelper.domain

import Data.ClientList
import Data.ClientListGet
import androidx.lifecycle.LiveData
import com.example.fromdeskhelper.data.model.Controller.ClientsController
import com.example.fromdeskhelper.data.model.Controller.SavedProductController
import javax.inject.Inject


class CallsClientUseCase @Inject constructor(private val ProductController: ClientsController) {
    suspend fun addClientItem(clientList: ClientList){
        ProductController.saveClient(clientList)

    }
    fun getClientItem():LiveData<List<ClientListGet>> {
        return ProductController.GetProductsAll()
    }
}
