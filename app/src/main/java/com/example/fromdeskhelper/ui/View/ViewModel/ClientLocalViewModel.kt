package com.example.fromdeskhelper.ui.View.ViewModel

import Data.ClientListGet
import Data.ClientToProduct
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.domain.CallsClientUseCase
import com.example.fromdeskhelper.domain.GetPermisionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val LOGVIEWMODEL="ViewModelInit"
@HiltViewModel
class ClientLocalViewModel @Inject constructor(
    private val UseCaseLocal: CallsClientUseCase,
    ) : AndroidViewModel(Application()) {
    //):ViewModel(){
    val itemClientItem = MutableLiveData<ClientListGet>()
    val itemClientProducts = MutableLiveData<ClientToProduct>()
    val closeView = MutableLiveData<Boolean>()

    fun setRelationSelect (idClient: Int,idProduct:Int){
        viewModelScope.launch {
            UseCaseLocal.setRelationClient(idClient,idProduct)
        }
    }

    fun GetRelation(idClient: Int){
        viewModelScope.launch {
            itemClientProducts.postValue(UseCaseLocal.productsGetClient(idClient))
        }
    }
    fun DeleteRelaton(GetClinet:Int,Product:Int){
        viewModelScope.launch {
            UseCaseLocal.productsRemoveClient(GetClinet,Product)
        }
    }

    fun SendselectItem(client:ClientListGet){
        itemClientItem.postValue(client)
    }

    fun closeViewInterface() {
        closeView.postValue(true)
    }
}
//    val UserComprobateLogin : LiveData<Boolean> = _registerResult;
