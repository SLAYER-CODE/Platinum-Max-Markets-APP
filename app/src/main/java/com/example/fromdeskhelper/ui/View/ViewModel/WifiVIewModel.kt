package com.example.fromdeskhelper.ui.View.ViewModel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.fromdeskhelper.data.model.WifiModel
import com.example.fromdeskhelper.domain.BroadCastConnect
import com.example.fromdeskhelper.domain.GetP2pUseCase
import com.example.fromdeskhelper.domain.UtilsP2pUseCase
import com.example.fromdeskhelper.domain.WifiDirectBroadcastReceived
import com.example.fromdeskhelper.io.Receive.SendReceive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import javax.inject.Inject

@HiltViewModel
class WifiVIewModel @Inject constructor(
    private val getWifiUseCase: GetP2pUseCase,
    private val WifiUtils: UtilsP2pUseCase,
    private val Broadcast: BroadCastConnect,
) : AndroidViewModel(Application()) {
    val WifiModel: MutableLiveData<WifiModel> = MutableLiveData<WifiModel>()
    val WifiActivate: MutableLiveData<Boolean> = MutableLiveData();
    val P2pPermisions: MutableLiveData<Boolean> = MutableLiveData();
    var P2pPermisin: Boolean = false;

    val WifiActivateBroadcast: MutableLiveData<Boolean> = MutableLiveData();
    val DeviceListObserver: MutableLiveData<WifiP2pDeviceList?> = MutableLiveData();
    val DeviceListConectObserver: MutableLiveData<MutableList<Pair<SendReceive,Any>>> = MutableLiveData();
    val DeviceListConectSegureObserver: MutableLiveData<MutableList<String>> = MutableLiveData();

    var Items:WifiP2pDeviceList?= null;
    var ItemsConected:MutableList<Pair<SendReceive,Any>> = mutableListOf();
    var conexSendResponse:MutableLiveData<SendReceive> = MutableLiveData()

    var ItemsConectedSecure:MutableList<String> = mutableListOf();

    fun SearchList() {
        //     val currentWifi = WifiProvider.quoteModel.random()
        //     WifiModel.postValue(currentWifi)
    }

    fun getPermissions(){
        if(!P2pPermisin){
            P2pPermisions.postValue(true)
            P2pPermisin=true
        }else{
            P2pPermisions.postValue(false)
        }
    }

    fun sendListP2P(List: WifiP2pDeviceList?) {
        viewModelScope.launch {
            Items=List
            DeviceListObserver.postValue(Items)
        }
    }
    fun refreshsendListP2P() {
        viewModelScope.launch {
            DeviceListObserver.postValue(Items)
        }
    }

    fun addDeviceConected(Device:String,sendReceive: SendReceive){
        Log.d("P2PREBUSQUED ADDRES [CALLED]::",Device)
        viewModelScope.launch {
            Log.d("P2PREBUSQUED ADDRES::",Device)
            Log.d("P2PREBUSQUED ITEMS::",Items.toString())
            if(Items!=null && Items?.get(Device)!=null) {
                    Log.d("P2PREBUSQUED","Se agrego correctamente")
                    ItemsConected.add(Pair<SendReceive,Any>(sendReceive,Items!!.get(Device)))
            }else{
                ItemsConected.add(Pair<SendReceive,Any>(sendReceive,Device))
            }
            addGetDeviceConectItems()
        }
    }

    fun getItemConected(sendItem:SendReceive){
        conexSendResponse.postValue(sendItem)
    }



    fun addGetDeviceConectItems(){
        DeviceListConectObserver.postValue(ItemsConected)
        DeviceListConectSegureObserver.postValue(ItemsConectedSecure)
    }


    fun RetunrManger(): WifiP2pManager {
        return WifiUtils.getManager()
    }

    fun ReturnChangel(): WifiP2pManager.Channel {
        return WifiUtils.getChanel()
    }

    fun onCreate() {
        WifiActivate.postValue(true)
        viewModelScope.launch {
            val result = getWifiUseCase();
            if (!result.isNullOrEmpty()) {
                WifiModel.postValue(result[0])
            }
        }
    }

    fun onCreateWIfi() {
        WifiActivate.postValue(true)
    }

    fun VerifyWifi(): Boolean {
        val res = WifiUtils.GetWifiState()
        return res;
    }

    fun ActiveAndDeactiveWifi() {
        val res = WifiUtils()
        WifiActivate.postValue(res);
    }


    fun getregisterBroadcast(
        context: WifiP2pManager.PeerListListener,
        infoListener: WifiP2pManager.ConnectionInfoListener
    ): WifiDirectBroadcastReceived {
        Log.i("P2PRECONECTVIEWMODEL", "SE LLAMO AL BROADCAST")
        return Broadcast.invoke(context, infoListener)
    }

    //    @SuppressLint("MissingPermission")
    fun DiscoveryActivate(context: Context) {
        Log.i("P2PRECONECTVIEWMODEL", "Se llamo al discovery")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("P2PRECONECTVIEWMODEL", "Sin permiso")
            WifiActivateBroadcast.postValue(false)
            return
        }
//        Log.i("P2PRECONECTVIEWMODEL", "Canal " + WifiUtils.wifiP2pManager)
//        Log.i("P2PRECONECTVIEWMODEL", "P2PMANAGER " + WifiUtils.getChanel())
//        val method: Method = WifiUtils.getManager()::class.java.getMethod(
//            "setDeviceName",
//            WifiP2pManager.Channel::class.java,
//            String::class.java,
//            WifiP2pManager.ActionListener::class.java
//        )
//        method.invoke(
//            WifiUtils.wifiP2pManager,
//            WifiUtils.getChanel(),
//            "New Device Name",
//            object : WifiP2pManager.ActionListener {
//                override fun onSuccess() {}
//                override fun onFailure(reason: Int) {}
//            })

        WifiUtils.wifiP2pManager.discoverPeers(WifiUtils.getChanel(),
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.i("P2PRECONECTVIEWMODEL", "Si entro correctamente")
                    WifiActivateBroadcast.postValue(true)
                }

                override fun onFailure(p0: Int) {
                    Log.i("P2PRECONECTVIEWMODEL", "Fallo el dato" + p0.toString())
                    WifiActivateBroadcast.postValue(false)
                }
            })
    }
}