package com.example.fromdeskhelper.ui.View.ViewModel.Client

import Data.listInventarioProductos
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val LOGVIEWMODEL="ProductsP2PViewMode"
@HiltViewModel
class ShowMainClientViewModel @Inject constructor():
    AndroidViewModel(
        Application()
    ) {
    val ItemsRecivedTransmited: MutableLiveData<MutableList<MutableList<FactureProduct>>> = MutableLiveData();
    val Ritems=mutableListOf<MutableList<FactureProduct>>()
    fun AddFacture(Facture: String){
        val Items:MutableList<FactureProduct> = mutableListOf()
        viewModelScope.launch {
            var item = Facture.split(";")
            for (y in item){
                if(y!="") {
                    var z = y.split(":")
                    Items.add(FactureProduct(z[0], z[1].toDouble()))
                }
            }
        }
        Ritems.add(Items)
        ItemsRecivedTransmited.postValue(Ritems)
    }
}


data class FactureProduct(
    val nombre: String,
    val price:Double,
)
