package com.example.fromdeskhelper.ui.View.ViewModel.Client

import Data.listInventarioProductos
import Data.listInventarioProductosP2P
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private val LOGVIEWMODEL="ProductsP2PViewMode"
@HiltViewModel
class ProductsP2PViewMode @Inject constructor():
    AndroidViewModel(
        Application()
    ) {
    val ItemsRecivedTransmited: MutableLiveData<MutableList<listInventarioProductosP2P>> = MutableLiveData();
    public val Items:MutableList<listInventarioProductosP2P> = mutableListOf()

    fun ComprobateList(Products: listInventarioProductosP2P):Boolean{
        for (x in Items){
            if(x.uid==Products.uid){
                return true
            }
        }
        return false
    }

    fun AddRecived(Products: String){
        viewModelScope.launch {
            var ItemZerialice=Products.split(";")
            var imagenBity:ByteArray?=null
            var qr:String?="Sin Codigo"

            if(ItemZerialice[5]!="null"){
                imagenBity=java.util.Base64.getDecoder().decode(ItemZerialice[4])
            }
            if(ItemZerialice[4]!="null"){
                qr=ItemZerialice[4]
            }
            var NewProduct = listInventarioProductosP2P(
                ItemZerialice[0].toInt(),
                nombre = ItemZerialice[1],
                precioC = ItemZerialice[3].toDouble(),
                precioNeto = ItemZerialice[2].toDouble(),
                imageBit = imagenBity,
                qr=qr
            )
            Log.i("RECIVIENDO ARCHIVO", NewProduct.toString())
            if (!ComprobateList(NewProduct)) {
                Items.add(NewProduct)
                RequestItem()
            }
        }
    }
    fun RequestItem(){
        viewModelScope.launch {
            Log.i("RECIVIENDO","Se ACTUALIZO")
            ItemsRecivedTransmited.postValue(Items)
        }
    }
    fun RequestItemFunc():MutableList<listInventarioProductosP2P>{
        return Items
    }

}