package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.domain.CallsProductsServerUseCase
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowServerViewModel @Inject constructor(private val UseCaseServer: CallsProductsServerUseCase) :
    AndroidViewModel(
        Application()
    ) {
    val ProductsAllPreview: MutableLiveData<ProductsPreviewQuery.Data?> = MutableLiveData();

    fun GetProductsAllPreview() {
        viewModelScope.launch {
            try {
                var resource = UseCaseServer.PreviewCallUser()
                ProductsAllPreview.postValue(resource)
            }catch (ex:ApolloException){
                ex.printStackTrace()
                Log.e("SHOWSERVERVIEWMODDL","No hay conexion: "+ex.toString())
            }
        }
    }





}