package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import android.widget.Adapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.domain.Root.LocalConnectUseCase
import com.example.fromdeskhelper.domain.Root.ProductsModelAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val LOG_VIEWMODELLOCAL="LOGVIEWMODELLOCAL"
@HiltViewModel
class ShowLocalViewModel @Inject constructor(
    private val UseCaseLocal:LocalConnectUseCase) :
    AndroidViewModel(
    Application()
) {
    val ConnectLocalCorrect: MutableLiveData<Boolean> = MutableLiveData();
    val AdapterSend: MutableLiveData<List<ProductsModelAdapter>> = MutableLiveData();
    fun ComprobateConnect(){
        viewModelScope.launch {
            var result = UseCaseLocal.ConnectComprobate()
//            UseCaseLocal.ComprobateTest()
            ConnectLocalCorrect.postValue(result)
        }
    }
    fun GetAllProducts(){
        viewModelScope.launch {
            var result = UseCaseLocal.GetProductsAll()
            Log.i(LOG_VIEWMODELLOCAL,result.toString())
            AdapterSend.postValue(result)
        }
    }
}