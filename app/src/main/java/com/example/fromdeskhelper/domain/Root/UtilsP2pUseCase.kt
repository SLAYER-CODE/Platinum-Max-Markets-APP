package com.example.fromdeskhelper.domain

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.fromdeskhelper.ui.View.fragment.Root.ClientesRoot
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class UtilsP2pUseCase @Inject constructor(public val wifimanager: WifiManager,
                                          public val wifiP2pManager: WifiP2pManager,
                                          private val chanel: WifiP2pManager.Channel){
     operator fun invoke(): Boolean {

         Log.d("WIFI","El Wifi esta ["+if(wifimanager.isWifiEnabled)"Esta Activado!]" else "Esta desactivado!]")
         Log.i("WIFI","Habilitando el wifi...")

         if(Build.VERSION.SDK_INT < 29) {
            wifimanager.isWifiEnabled = !wifimanager.isWifiEnabled
             Log.d("WIFI","El Wifi esta ["+if(wifimanager.isWifiEnabled)"Esta Activado!]" else "Esta desactivado!]")
            return true
        }else{
            Log.d("WIFI","El Wifi esta ["+if(wifimanager.isWifiEnabled)"Esta Activado!]" else "Esta desactivado!]")
        }
        return false

    }
    fun GetWifiState():Boolean{
        return wifimanager.isWifiEnabled
    }
    fun getManager():WifiP2pManager{
        return wifiP2pManager
    }
    fun getChanel(): WifiP2pManager.Channel{
        return chanel
    }

}


class BroadCastConnect @Inject constructor(
    private val wifimanager: WifiManager,
    private val wifiP2pManager:WifiP2pManager,
    private val chanel:WifiP2pManager.Channel,
//    @ApplicationContext private val  context: Context
    ){

    operator fun invoke(listener: WifiP2pManager.PeerListListener,infoListener:WifiP2pManager.ConnectionInfoListener):WifiDirectBroadcastReceived{
        return WifiDirectBroadcastReceived(wifiP2pManager,chanel,listener,infoListener)
//        wifiP2pManager.requestPeers()
    }
}