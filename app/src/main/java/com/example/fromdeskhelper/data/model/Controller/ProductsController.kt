package com.example.fromdeskhelper.data.model.Controller

import Data.InventarioProducts
import Data.Producto
import Data.listInventarioProductos
import androidx.lifecycle.LiveData
import com.example.fromdeskhelper.data.model.base.ProductoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProductsController @Inject constructor(
    private val DaoProduct: ProductoData
) {
    fun GetProductsAllOffset(offset:Int,limit:Int):LiveData<List<listInventarioProductos>>{
        return DaoProduct.getByInventarioProductos(offset,limit)
    }
    fun GetProductsAll():LiveData<List<listInventarioProductos>>{
        return DaoProduct.getByInventarioProductosAll()
    }
    fun GetProductsAll(ID:Int):LiveData<InventarioProducts>{
        return DaoProduct.getInventarioId(ID)
    }

    suspend fun DeleteProduct(product:Producto){
        withContext(Dispatchers.IO){
            DaoProduct.delete(product)
        }
    }

    suspend fun UpdateProduct(product: Producto){
        withContext(Dispatchers.IO){
            DaoProduct.update(product)
        }
    }

}
