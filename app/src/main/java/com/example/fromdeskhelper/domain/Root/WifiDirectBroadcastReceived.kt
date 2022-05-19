package com.example.fromdeskhelper.domain

//import jdk.internal.net.http.common.Log.channel

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat


class WifiDirectBroadcastReceived : BroadcastReceiver  {
    private lateinit var mManager : WifiP2pManager;
    private lateinit var mChanel:WifiP2pManager.Channel;
    private lateinit var mListener :WifiP2pManager.PeerListListener;
    private lateinit var mListenerInfo :WifiP2pManager.ConnectionInfoListener
    constructor(mManager:WifiP2pManager,
                mChange:WifiP2pManager.Channel,
                listener: WifiP2pManager.PeerListListener,
                listenerDevice:WifiP2pManager.ConnectionInfoListener){
        this.mChanel=mChange
        this.mManager=mManager
        this.mListener=listener
        this.mListenerInfo=listenerDevice
//        val paramTypes: Array<Class<*>> = arrayOf()
//        paramTypes[0] = WifiP2pManager.Channel::class.java
//        paramTypes[1] = String::class.java
//        paramTypes[2] = WifiP2pManager.ActionListener::class.java

//        var primero= mManager.javaClass.getMethod(
//            "EdwinPaz",mChange.javaClass, String.javaClass,)
    //        mManager.
    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        Resive todas las transancciones de acerca de las conexion.

//        Log.d("P2P","Se recivio paquetes de transferencia")
//        Log.i("P2P",this.mChanel.toString())
//        Log.i("P2P",this.mManager.toString())
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(intent?.action)){
            Log.i("P2PRECONECT","Se inicializo en la accion de carga");
            var state = intent?.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Log.i("P2PRECONECT","Con conexion estamos redy");
            }else{
                Log.i("P2PRECONECT","Sin conexion de carga de contenido...");
            }

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(intent?.action)){
            Log.i("P2PRECONECT","Se cargo la parede de conexion segun la accion");
            if(mManager!=null){
                if (ActivityCompat.checkSelfPermission(
                        context!!.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("P2PRECONECT","Sin conexion de carga de contenido...");

                    return
                }
                mManager.requestPeers(mChanel,mListener);

            }

        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(intent?.action)){
            Log.i("P2PRECONECT","SE ESCUCHO LA SOLICITUD")
            if(mManager==null) return
            val network: NetworkInfo? =
                intent?.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)

            if(network!!.isConnected){
                Log.i("P2PRECONECT","Se conecto al dispositivo")
            }else{
                Log.i("P2PRECONECT","Se desconecto del dispositivo")
            }

            mManager.requestConnectionInfo(mChanel,mListenerInfo)
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(intent?.action)){
//            val device: WifiP2pDevice? =
//                intent?.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
//            device?.deviceName="Hñolas"

        }
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            for (networkInfo in info) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            WifiP2pManager.EXTRA_P2P_DEVICE_LIST
        }
        return false
    }
}