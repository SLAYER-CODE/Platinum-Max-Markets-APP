package com.example.fromdeskhelper.ui.View.ViewModel

import Data.ClientList
import Data.ClientListGet
import Data.listInventarioProductos
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.domain.CallsAddProductStorageUseCase
import com.example.fromdeskhelper.domain.CallsAddProductsUseCase
import com.example.fromdeskhelper.domain.CallsClientUseCase
import com.example.fromdeskhelper.domain.Root.LocalConnectUseCase
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private var LOG_VIEWMODEL = "VIEWMODELCLIENTADD"

@HiltViewModel
class ClientAddShortViewModel @Inject constructor(
    private val UseCaseLocal: CallsClientUseCase,
) :
    AndroidViewModel(
        Application()
    ) {

    public val ItemsClients: MutableList<ClientList> = mutableListOf()

    fun agregateItem(client: ClientList) {
        viewModelScope.launch {
            UseCaseLocal.addClientItem(client)
        }
    }

    fun getItemClients():LiveData<List<ClientListGet>> {
        return UseCaseLocal.getClientItem()
    }
}