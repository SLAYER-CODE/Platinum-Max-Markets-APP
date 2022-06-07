package com.example.fromdeskhelper.ui.View.ViewModel

import Data.InventarioProducts
import Data.Producto
import Data.listInventarioProductos
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.domain.CallsAddProductStorageUseCase
import com.example.fromdeskhelper.type.Products
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ShowMainViewModel @Inject constructor(private val Utils: UtilsController,private val Products:ProductsController) :AndroidViewModel(
Application()
) {
    val ItemsRouterTransmited: MutableLiveData<MutableList<listInventarioProductos>> = MutableLiveData();
    public val Items:MutableList<listInventarioProductos> = mutableListOf()
    val ImageReturn: MutableLiveData<String?> = MutableLiveData();

    fun ComprobateList(Products: listInventarioProductos):Boolean{
        for (x in Items){
            if(x.uid==Products.uid){
                return true
            }
        }
        return false
    }
    fun AddResponse(Products: listInventarioProductos){
        Log.i("SUBIENDO AL SERVIDOR",Products.nombre)
       if(!ComprobateList(Products)){
           Items.add(Products)
           RequestItem()
       }
    }
    fun RequestItem(){
        viewModelScope.launch {
            Log.i("SUBIENDO","Se ACTUALIZO")
            ItemsRouterTransmited.postValue(Items)
        }
    }
    fun RequestItemFunc():MutableList<listInventarioProductos>{
            return Items
    }

    fun GetImageResource(){
        viewModelScope.launch {
            ImageReturn.postValue(FirebaseAuth.getInstance().currentUser?.photoUrl?.toString())
        }
    }


    fun GetCount():LiveData<Int>{
        return Utils.GetCountItems()
    }


    fun AllProducts():LiveData<List<listInventarioProductos>>{
        return Products.GetProductsAll()
    }


    fun AllProductsLimits(max:Int,limit:Int):LiveData<List<listInventarioProductos>>{
        return Products.GetProductsAllOffset(max,limit)
    }
    fun getProductId(ID:Int):LiveData<InventarioProducts>{
        return Products.GetProductsAll(ID)
    }

    fun deteProduct(product:Producto){
        viewModelScope.launch {
            Products.DeleteProduct(product)
        }
    }

    fun updateProduct(product: Producto){
        viewModelScope.launch {
            Products.UpdateProduct(product)
        }
    }



}