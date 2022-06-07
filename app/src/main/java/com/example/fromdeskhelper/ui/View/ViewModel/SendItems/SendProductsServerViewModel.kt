package com.example.fromdeskhelper.ui.View.ViewModel.SendItems

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.ui.View.adapter.LocalAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SendProductsServerViewModel @Inject constructor() :
    AndroidViewModel(
    Application()
) {
    val Items: MutableLiveData<ServerAdapter> = MutableLiveData();
//    private var adapterServer :ServerAdapter=ServerAdapter(listOf(),1)
    fun sendAdapterItems(items: ServerAdapter){
        viewModelScope.launch {
            Items.postValue(items)
//            adapterServer=items
        }
    }

}
