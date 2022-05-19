package com.example.fromdeskhelper.io

import com.example.fromdeskhelper.io.Receive.SendReceive
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

class ClientServerP2P :Thread{
    public lateinit var sock: Socket
    public var host: String?
    private lateinit var sendReceived: SendReceive
    private lateinit var handel: android.os.Handler
    private var wifiItemAdd: WifiVIewModel
    private lateinit var content:String
    constructor(hostAddres:InetAddress?,handler: android.os.Handler,wifiItem:WifiVIewModel,hostter:String){
        host=hostAddres?.hostAddress
        sock= Socket()
        handel=handler
        this.wifiItemAdd=wifiItem
        content=hostter
    }

    override fun run() {
        try{
            sock.connect(InetSocketAddress(host,8888))
            sendReceived= SendReceive(sock,handler = handel)
            sendReceived.start()
            wifiItemAdd.addDeviceConected(content,sendReceived)
        }catch (e:IOException){
            e.printStackTrace()
        }
        super.run()
    }
    fun returnRecived():SendReceive{
        return sendReceived
    }
}