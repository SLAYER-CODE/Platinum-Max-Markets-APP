package com.example.fromdeskhelper.ui.View.ViewModel.SendItems

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.ui.View.adapter.LocalAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendProductsLocalViewModel @Inject constructor() :


    AndroidViewModel(
        Application()
    ) {
    val Items: MutableLiveData<LocalAdapter> = MutableLiveData();
//    private var adapterLocal :LocalAdapter=LocalAdapter(listOf(),1)
    fun sendAdapterItems(items: LocalAdapter){
        viewModelScope.launch {
            Items.postValue(items)
//            adapterLocal=items
        }
    }

}
