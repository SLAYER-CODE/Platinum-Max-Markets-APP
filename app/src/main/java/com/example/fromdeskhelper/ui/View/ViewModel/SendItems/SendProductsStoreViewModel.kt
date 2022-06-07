package com.example.fromdeskhelper.ui.View.ViewModel.SendItems

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SendProductsStoreViewModel @Inject constructor() :
    AndroidViewModel(
        Application()
    ) {
    val Items: MutableLiveData<ProductoAdapter> = MutableLiveData();
//    private var adapterProduct: ProductoAdapter = ProductoAdapter(listOf(), null,1);

//    private val _ItemsFlow = MutableSharedFlow<ProductoAdapter>(replay = 0)
//    val ItemsFlow = _ItemsFlow.asSharedFlow()

    fun sendAdapterItems(items: ProductoAdapter){

        viewModelScope.launch {
            Items.postValue(items)
//            adapterProduct=items
//            _ItemsFlow.emit(adapterProduct)
        }
    }

}
