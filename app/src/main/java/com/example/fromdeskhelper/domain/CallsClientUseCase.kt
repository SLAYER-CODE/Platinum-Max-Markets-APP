package com.example.fromdeskhelper.domain

import Data.ClientAndProducts
import Data.ClientList
import Data.ClientListGet
import Data.ClientToProduct
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

    suspend fun productsGetClient(GetClinet:Int):ClientToProduct{
        return ProductController.productsGetClient(GetClinet)
    }



    suspend fun productsRemoveClient(GetClinet:Int,Product:Int){
        return ProductController.productsRemoveClient(GetClinet,Product)
    }

    suspend fun setRelationClient(idClient:Int,idProdct:Int){
        var relation= ClientAndProducts(idClient, idProdct)
        ProductController.setRelationProduct(relation)
    }


}
