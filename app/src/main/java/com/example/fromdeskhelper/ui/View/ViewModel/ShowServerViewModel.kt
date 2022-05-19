package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.domain.CallsProductsServerUseCase
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
            var resource = UseCaseServer.PreviewCallUser()
            ProductsAllPreview.postValue(resource)
        }
    }
}