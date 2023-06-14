package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import android.widget.Adapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.core.socket.ServerConnection
import com.example.fromdeskhelper.data.model.Controller.ProductsController
import com.example.fromdeskhelper.data.model.Controller.UtilsController
import com.example.fromdeskhelper.data.model.objects.Indetifier
import com.example.fromdeskhelper.data.model.objects.SocktFuctionEvent
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsModelAdapter
import com.example.fromdeskhelper.domain.Local.SocketConnectUseCase
import com.example.fromdeskhelper.domain.Local.SocketFuctionUseCase
import com.example.fromdeskhelper.domain.Root.LocalConnectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import javax.inject.Inject

private val LOG_VIEWMODELLOCAL="LOGVIEWMODELLOCAL"
private val LOG_VIEWMODELLOCALLISTENERSOCKET="SOCKETLISTENERRECIVED"
private val LOG_VIEWMODELLOCALLISTENERSOCKETSTATUS="SOCKETLISTENERSENDSTATUS"
@HiltViewModel
class ShowLocalViewModel @Inject constructor(
    private val UseCaseLocal:LocalConnectUseCase,private val SocketLocalConnect:SocketConnectUseCase,private val SocketFunction:SocketFuctionUseCase) :
    AndroidViewModel(
    Application()
) {
    val ConnectLocalCorrect: MutableLiveData<Boolean> = MutableLiveData();
    val AdapterSend: MutableLiveData<List<ProductsModelAdapter>> = MutableLiveData();
    val AdapterConnection: MutableLiveData<Boolean> = MutableLiveData();
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

    fun ConnectServer(){
        viewModelScope.launch {
            SocketLocalConnect(object :ServerConnection.ServerListener{
                override fun onNewMessage(message: String?) {
                    Log.i(LOG_VIEWMODELLOCALLISTENERSOCKET,message.toString())
                    if(message=="PONG"){
                        Log.i(LOG_VIEWMODELLOCALLISTENERSOCKET,"Ser recivio la conexion [PONG]")
                    }
                }

                override fun onStatusChange(status: ServerConnection.ConnectionStatus?) {
                    if(status == ServerConnection.ConnectionStatus.CONNECTED){
                        Log.i(LOG_VIEWMODELLOCALLISTENERSOCKETSTATUS,"Existe una conneccion")
                        AdapterConnection.postValue(true)
                    }else{
                        Log.i(LOG_VIEWMODELLOCALLISTENERSOCKETSTATUS,"La conexion se detuvo")
                    }
                }
            })
        }
    }

    fun Register(number:String="?????????"){
        viewModelScope.launch {
            SocketFunction.registerFunction(number)
        }
    }
    fun SendMessage(Message:String){
        viewModelScope.launch {
            SocketFunction.sendString(Message)
        }
    }
}