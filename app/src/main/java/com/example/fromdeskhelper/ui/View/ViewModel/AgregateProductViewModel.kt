package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.data.model.objects.ResponseAdd
import com.example.fromdeskhelper.data.model.objects.Upload
import com.example.fromdeskhelper.domain.CallsAddProductStorageUseCase
import com.example.fromdeskhelper.domain.CallsAddProductsUseCase
import com.example.fromdeskhelper.domain.Root.LocalConnectUseCase
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


var LOG_VIEWMODE = "VIEWMODELADDPRODUCT"

@HiltViewModel
class AgregateProductViewModel @Inject constructor(
    private val UseCaseServer: CallsAddProductsUseCase,
    private val UseCaseLocal: CallsAddProductStorageUseCase,
    private val UseCaseLL: LocalConnectUseCase,
) :
    AndroidViewModel(
        Application()
    ) {

    val QRBindig: MutableLiveData<String> = MutableLiveData();
    val ClearFragment: MutableLiveData<Boolean> = MutableLiveData()
    val RecivImageItem: MutableLiveData<String> = MutableLiveData();
    val ButtonAgregateState: MutableLiveData<Boolean> = MutableLiveData();
    val AgregateState: MutableLiveData<ResponseAdd> = MutableLiveData();

    val data: LiveData<MutableList<Uri>>
        get() = _data

    private val _data = MutableLiveData<MutableList<Uri>>(mutableListOf())
    private val list = mutableListOf<Uri>()

    private val _sharedFlow = MutableSharedFlow<Uri?>(replay = 0)
    val sharedFlow = _sharedFlow.asSharedFlow()


    //[AGREGATEPRODUCTS]
    val ListCategories: MutableLiveData<CategoriasQuery.Data> = MutableLiveData();

    fun sendImageCapture(toUri: Uri?) {
        Log.i(LOG_VIEWMODE, "SE LLAMO EN VIEWMODEL" + toUri.toString())
        viewModelScope.launch {
            _sharedFlow.emit(toUri)
        }
    }

    private fun loadData() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        Log.i("VIEWMODEL SELLAMO", "Se envio el primer cargo")
        viewModelScope.launch {
            _data.postValue(list)
        }
    }



    fun AgregateQR(String: String) {
        QRBindig.postValue(String)
    }

    fun AgregateItemStorage(
        name: String,
        precio: String,
        precioU: String,
        marca: String,
        detalles: String,
        categoria: String,
        stockcantidad: String,
        stockunidad: String,
        qr: String,
        mutableList: MutableList<Uri>, baseActivity: EmployedMainActivity
    ) {
        viewModelScope.launch {
            var user = UseCaseLocal.SaveProductLocal(
                name, precio, precioU, marca, detalles, categoria, stockcantidad, stockunidad, qr,

                )
            if (user != -1) {
                UseCaseLocal.SaveImageProduct(
                    mutableList,
                    user, baseActivity
                )
                AgregateState.postValue(ResponseAdd(Upload.STORAGE,true))
            }
        }
    }

    fun AgregateServer(
        ListCategoria: MutableList<CategoriesInput>,
        LittMarca: MutableList<BrandsInput>,
        name: String,
        precio: String,
        precioU: String,
        marca: String,
        detalles: String,
        categoria: String,
        stockcantidad: String,
        stockunidad: String,
        qr: String,
        mutableList: MutableList<Uri>, baseActivity: EmployedMainActivity
    ) {
        viewModelScope.launch {
            var retorned = UseCaseServer.AgregateProduct(
                ListCategoria.toList(),
                LittMarca.toList(),
                name,
                precio,
                precioU,
                marca,
                detalles,
                categoria,
                stockcantidad,
                stockunidad,
                qr,
                mutableList, baseActivity
            )
            if(retorned!=null){
                if(retorned.createproduct.product_name == name){
                    AgregateState.postValue(ResponseAdd(Upload.SERVER,true))
                }else{
                    AgregateState.postValue(ResponseAdd(Upload.SERVER,false))
                }
            }else{
                AgregateState.postValue(ResponseAdd(Upload.SERVER,false))
            }
        }
    }

    fun AgregateProductLocal(
        ListCategoria: MutableList<String>,
        LittMarca: MutableList<String>,
        name: String,
        precio: String,
        precioU: String,
        marca: String,
        detalles: String,
        categoria: String,
        stockcantidad: String,
        stockunidad: String,
        qr: String,
        mutableList: MutableList<Uri>, baseActivity: EmployedMainActivity
    ){
        viewModelScope.launch {
            //Dos formas!! SERVIDOR GENERAL
            var item = UseCaseLL.AddProductOne(
                ListCategoria,
                LittMarca,
                name,
                precio,
                precioU,
                marca,
                detalles,
                categoria,
                stockcantidad,
                stockunidad,
                qr,
                mutableList, baseActivity,Upload.SERVER_LOCAL
            )
            if(item){
                AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL,true))
            }else{
                AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL,false))
                var itemResolver = UseCaseLL.AddProductOne(
                    ListCategoria,
                    LittMarca,
                    name,
                    precio,
                    precioU,
                    marca,
                    detalles,
                    categoria,
                    stockcantidad,
                    stockunidad,
                    qr,
                    mutableList, baseActivity,Upload.SERVER_LOCAL_DATABASE
                )
                if(itemResolver){
                    AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL_DATABASE,true))
                }else{
                    AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL_DATABASE,false))
                }
            }
        }
    }


    fun CategoriesGetApp() {
        viewModelScope.launch {
            ListCategories.postValue(UseCaseServer.ReturnCategories())
        }
    }

    override fun onCleared() {
        Log.i("VIEWMODEL SELLAMO", "se destruyo la vista")
        ClearFragment.postValue(true)
        super.onCleared()
    }
}
