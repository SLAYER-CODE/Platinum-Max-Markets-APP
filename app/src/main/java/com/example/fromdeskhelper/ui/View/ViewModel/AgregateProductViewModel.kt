package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bolts.Bolts
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.data.model.objects.Form
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
import kotlinx.serialization.descriptors.PrimitiveKind
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID
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
    val EditingState: MutableLiveData<Boolean> = MutableLiveData();
    val State: MutableLiveData<Form> = MutableLiveData();

    val data: LiveData<MutableList<Uri>>
        get() = _data

    private val _data = MutableLiveData<MutableList<Uri>>(mutableListOf())
    private val list = mutableListOf<Uri>()

    private val _sharedFlow = MutableSharedFlow<Uri?>(replay = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()


    //[AGREGATEPRODUCTS]
    val ListCategories: MutableLiveData<CategoriasQuery.Data> = MutableLiveData();

    fun sendImageCapture(toUri: Uri?) {
        Log.i(LOG_VIEWMODE, "SENDIMAGECAPTURE" + toUri.toString())
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


    fun editing(){
        if(EditingState.value == true) {
            EditingState.postValue(false)
        }else{
            EditingState.postValue(true)
        }
    }

    fun AgregateQR(stringer: String) {
        Log.i("SERVICIO XD VIEWMODEL", stringer)
        QRBindig.postValue(stringer)
    }

    fun SaveCamp(Form:Form) {
        viewModelScope.launch {
            State.postValue(Form)
            if (Form.Storage) {
                Log.i(LOG_VIEWMODE, "Agregando storage")
                AgregateItemStorage(
                    Form.name,
                    Form.precio,
                    Form.precioNeto,
                    Form.disconut,
                    Form.stockcantidad,
                    Form.stockC_attr,
                    Form.stockU,
                    Form.stockU_attr,
                    Form.peso,
                    Form.peso_attr,
                    Form.stock_attr,
                    Form.detalles,
                    Form.qr,
                    Form.available_shipment,
                    Form.available_now,
                    Form.available_date,
                    Form.uuid,
                    Form.categoria,
                    Form.marca,
                    Form.mutableList,
                )
            }

            if (Form.SServer) {
                Log.i(LOG_VIEWMODE, "Agregando Server")

                AgregateServer(
                    Form.name,
                    Form.precio,
                    Form.precioNeto,
                    Form.disconut,
                    Form.stockcantidad,
                    Form.stockC_attr,
                    Form.stockU,
                    Form.stockU_attr,
                    Form.peso,
                    Form.peso_attr,
                    Form.stock_attr,
                    Form.detalles,
                    Form.qr,
                    Form.available_shipment,
                    Form.available_now,
                    Form.available_date,
                    Form.uuid,
                    Form.categoria,
                    Form.marca,
                    Form.mutableList,
                )


            }

            if (Form.SLocal) {
                Log.i(LOG_VIEWMODE, "Agregando Local")
//                var caTegorys = mutableListOf<String>()
//                var marCas = mutableListOf<String>()

//                binding.chipGroup2.children.toList().forEach {
//                    caTegorys.add((it as Chip).text.toString())
//                }
//                binding.ChipGroupMarca.children.toList().forEach {
//                    marCas.add((it as Chip).text.toString())
//                }
                AgregateProductLocal(
                    Form.name,
                    Form.precio,
                    Form.precioNeto,
                    Form.disconut,
                    Form.stockcantidad,
                    Form.stockC_attr,
                    Form.stockU,
                    Form.stockU_attr,
                    Form.peso,
                    Form.peso_attr,
                    Form.stock_attr,
                    Form.detalles,
                    Form.qr,
                    Form.available_shipment,
                    Form.available_now,
                    Form.available_date,
                    Form.uuid,
                    Form.categoria,
                    Form.marca,
                    Form.mutableList,
                )
            }

        }
    }

    fun AgregateItemStorage(
        name: String,
        precio: String,
        precioNeto: String,
        disconut: String,
        stockcantidad: String,
        stockC_attr: Int,


        stockU: String,
        stockU_attr: Int,

        peso: String,
        peso_attr: Int,

        stock_attr: Int,
        detalles: String,

        qr: String,

        available_shipment: Boolean,
        available_now: Boolean,
        available_date: LocalDateTime?,

        uuid: String,

        categoria: MutableList<CategoriesInput>,
        marca: MutableList<BrandsInput>,
        mutableList: MutableList<Uri>,

        ) {
        viewModelScope.launch {

//            var user = UseCaseLocal.SaveProductLocal(
//                name,
//                precio,
//                precioU,
//                marca,
//                detalles,
//                categoria,
//                stockcantidad,
//                stockunidad,
//                qr)
//            if (user != -1) {
//                UseCaseLocal.SaveImageProduct(
//                    mutableList,
//                    user,
//                )
//                AgregateState.postValue(ResponseAdd(Upload.STORAGE,true))
//            }

            var user = UseCaseLocal.SaveProductAllLocal(
                name,
                precio,
                precioNeto,
                disconut,
                stockcantidad,
                stockC_attr,


                stockU,
                stockU_attr,

                peso,
                peso_attr,

                stock_attr,
                detalles,

                qr,

                available_shipment,
                available_now,
                available_date,

                uuid,

                categoria,
                marca,
                mutableList,
            )
            if (user != -1) {
                AgregateState.postValue(ResponseAdd(Upload.STORAGE,true))
            }else{
                AgregateState.postValue(ResponseAdd(Upload.STORAGE,false))
            }
        }
    }

    fun AgregateServer(
        name: String,
        precio: String,
        precioNeto: String,
        disconut: String,
        stockcantidad: String,
        stockC_attr: Int,


        stockU: String,
        stockU_attr: Int,

        peso: String,
        peso_attr: Int,

        stock_attr: Int,
        detalles: String,

        qr: String,

        available_shipment: Boolean,
        available_now: Boolean,
        available_date: LocalDateTime?,

        uuid: String,

        categoria: MutableList<CategoriesInput>,
        marca: MutableList<BrandsInput>,
        mutableList: MutableList<Uri>,

        ) {
        viewModelScope.launch {
            var retorned = UseCaseServer.AgregateProduct(
                name,
                precio,
                precioNeto,
                disconut,
                stockcantidad,
                stockC_attr,
                stockU,
                stockU_attr,
                peso,
                peso_attr,
                stock_attr,
                detalles,
                qr,
                available_shipment,
                available_now,
                available_date,
                uuid,
                categoria,
                marca,
                mutableList,
            )
            if (retorned != null) {
                if (retorned.createproduct.product_name == name) {
                    AgregateState.postValue(ResponseAdd(Upload.SERVER, true))
                } else {
                    AgregateState.postValue(ResponseAdd(Upload.SERVER, false))
                }
            } else {
                AgregateState.postValue(ResponseAdd(Upload.SERVER, false))
            }
        }
    }

    fun AgregateProductLocal(
        name: String,
        precio: String,
        precioNeto: String,
        disconut: String,
        stockcantidad: String,
        stockC_attr: Int,


        stockU: String,
        stockU_attr: Int,

        peso: String,
        peso_attr: Int,

        stock_attr: Int,
        detalles: String,

        qr: String,

        available_shipment: Boolean,
        available_now: Boolean,
        available_date: LocalDateTime?,

        uuid: String,

        categoria: MutableList<CategoriesInput>,
        marca: MutableList<BrandsInput>,
        mutableList: MutableList<Uri>,

        ) {
        viewModelScope.launch {
            //Dos formas!! SERVIDOR GENERAL
            var item = UseCaseLL.AddProductOne(
                name,
                precio,
                precioNeto,
                disconut,
                stockcantidad,
                stockC_attr,
                stockU,
                stockU_attr,
                peso,
                peso_attr,
                stock_attr,
                detalles,
                qr,
                available_shipment,
                available_now,
                available_date,
                uuid,
                categoria,
                marca,
                mutableList,
                Upload.SERVER_LOCAL
            )
            if (item) {
                AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL, true))
            } else {
                AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL, false))
                var itemResolver = UseCaseLL.AddProductOne(
                    name,
                    precio,
                    precioNeto,
                    disconut,
                    stockcantidad,
                    stockC_attr,
                    stockU,
                    stockU_attr,
                    peso,
                    peso_attr,
                    stock_attr,
                    detalles,
                    qr,
                    available_shipment,
                    available_now,
                    available_date,
                    uuid,
                    categoria,
                    marca,
                    mutableList,
                    Upload.SERVER_LOCAL_DATABASE
                )
                if (itemResolver) {
                    AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL_DATABASE, true))
                } else {
                    AgregateState.postValue(ResponseAdd(Upload.SERVER_LOCAL_DATABASE, false))
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
