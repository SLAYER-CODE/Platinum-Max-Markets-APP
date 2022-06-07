package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.Network.UtilLocalServiceMG
import com.example.fromdeskhelper.data.Network.UtilsServicesGraph
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UtilsShowMainViewModels @Inject constructor(
    private val UtilsStorage: UtilsController,
    private val UtilsServer:UtilsServicesGraph,
    private val UtilsLocal:UtilLocalServiceMG) :
    AndroidViewModel(
    Application()
) {


    val StorageCountObserver: MutableLiveData<Int?>  = MutableLiveData();
    val ServerCountObserver: MutableLiveData<Int?> = MutableLiveData();
    val LocalCountObserver: MutableLiveData<Int?> = MutableLiveData();

    fun GetStorageCount(int:Int?=null){
        viewModelScope.launch {
            if(int==null) {
                UtilsStorage.GetCountItems().observeForever {
                    StorageCountObserver.postValue(it)
                }
            }else{
                StorageCountObserver.postValue(int)
            }
        }
    }


    fun GetServerCount(int:Int?=null){
        viewModelScope.launch {
            if(int==null) {
                ServerCountObserver.postValue(UtilsServer.GetCount()?.data?.countproduct)
            }else{
                ServerCountObserver.postValue(int)
            }
        }
    }
    fun GetLocalCount(int:Int?=null){
        viewModelScope.launch {
            if(int==null) {
                LocalCountObserver.postValue(UtilsLocal.GetPProductCount())
            }else{
                LocalCountObserver.postValue(int)
            }
        }
    }

    fun GetCountItemsServer(){
        GetServerCount()
        GetLocalCount()
        GetStorageCount()
    }


}