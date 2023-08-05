package com.example.fromdeskhelper.ui.View.ViewModel.Users

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ProductsPreviewOffertsQuery
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.SloganImagesGetQuery
import com.example.fromdeskhelper.domain.CallsAddProductStorageUseCase
import com.example.fromdeskhelper.domain.CallsAddProductsUseCase
import com.example.fromdeskhelper.domain.Client.CallClientServerUseCase
import com.example.fromdeskhelper.domain.Root.LocalConnectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel  @Inject constructor(
    private val UseCaseClient: CallClientServerUseCase,
    private val UseCaseServer: CallsAddProductsUseCase,

    ) :
    AndroidViewModel(
        Application()
    ) {
    val SloganAllPreview: MutableLiveData<SloganImagesGetQuery.Data?> = MutableLiveData();
    val ListCategories: MutableLiveData<CategoriasQuery.Data> = MutableLiveData();
    val OffertsProduct: MutableLiveData<ProductsPreviewOffertsQuery.Data> = MutableLiveData();
    val ListBrands: MutableLiveData<BrandsQuery.Data> = MutableLiveData();

    fun getSloganItems(){
        viewModelScope.launch {
            try {
                Log.e("SELLAMO SLOGAN","No hay conexion:")
                SloganAllPreview.postValue(UseCaseClient.PreviewSloganUser())
            }catch (ex: ApolloException){
                ex.printStackTrace()
                Log.e("SLOGANFAILED","No hay conexion: "+ex.toString())
            }
        }
    }
    fun getCategoryes(){
        viewModelScope.launch {
            ListCategories.postValue(UseCaseServer.ReturnCategories())
        }
    }

    fun getOfferts(){
        viewModelScope.launch {
            try {
                OffertsProduct.postValue(UseCaseClient.OffertsProduct())
            }catch (ex: ApolloException){
                ex.printStackTrace()
                Log.e("OFFERTS","ERROR: "+ex.toString())
            }
        }
    }

    fun getbrands(){
        viewModelScope.launch {
            try {
                ListBrands.postValue(UseCaseClient.BrandsProduct())
            }catch (ex:ApolloException){
                ex.printStackTrace()
                Log.e("OFFERTS","ERROR: "+ex.toString())
            }
        }
    }
}