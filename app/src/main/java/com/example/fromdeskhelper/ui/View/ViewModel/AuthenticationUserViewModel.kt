package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.GetDataAsociateQuery
import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.data.model.objects.UserLogin
import com.example.fromdeskhelper.domain.GetPermisionsAnonimeUseCase
import com.example.fromdeskhelper.domain.GetPermisionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
var LOGGER_CONECTION="ATUTENTICACION_WORKED"
@HiltViewModel
class AuthenticationUserViewModel @Inject constructor(
    private val loginApollo: GetPermisionsUseCase,private val anonime : GetPermisionsAnonimeUseCase

    ): AndroidViewModel(
    Application()
) {
    var CConnection: MutableLiveData<Boolean> = MutableLiveData<Boolean>();
    var CUser: MutableLiveData<UserLogin> = MutableLiveData<UserLogin>();
//    var CObject: MutableLiveData<Int?> = MutableLiveData<Int?>();

    var CDataAsociate: MutableLiveData< GetDataAsociateQuery.Data> = MutableLiveData< GetDataAsociateQuery.Data>();

    var CFuctions: MutableLiveData<GetRolesAdminQuery.Data?> =
        MutableLiveData<GetRolesAdminQuery.Data?>();
    var CFuctionsAnonime: MutableLiveData<MutableList<GetRolesAdminQuery.Fuction>> =
        MutableLiveData<MutableList<GetRolesAdminQuery.Fuction>>();


    private val _sharedFlow = MutableSharedFlow<Int?>(replay = 0)
    val CObject = _sharedFlow.asSharedFlow()
//    val FuctionsAllPreview: MutableLiveData<ProductsPreviewQuery.Data?> = MutableLiveData();

    var ObjectItem: UserLogin? = null;
    fun setCUser(Object: UserLogin) {
        viewModelScope.launch {
            CUser.postValue(Object)
            ObjectItem = Object
        }
    }

    fun setCUser(obj: Int?) {
        viewModelScope.launch {
//            sharedFlow.postValue(obj)
            _sharedFlow.emit(obj)
        }
    }

    fun getFunctions() {
        viewModelScope.launch {
//            delay(2000)
            var data = loginApollo.getRoles()
            CFuctions.postValue(data)
            if (data == null) {
                CFuctionsAnonime.postValue(anonime.getRoles())
            }
        }
    }
    fun getDataUser() {
        viewModelScope.launch {
            var data = loginApollo.getDataUser()
            if(data != null){
                CDataAsociate.postValue(data!!)
            }else{
                getErrorConection()
            }
        }
    }
    fun getErrorConection() {
        CConnection.postValue(true)
    }
}