package com.example.fromdeskhelper.io

import Data.listInventarioProductos
import android.util.Base64
import android.util.Log
import com.example.fromdeskhelper.io.Receive.SendReceive
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Handler

class ServerClassP2P : Thread {
    public lateinit var sock: Socket
    public lateinit var Ssock: ServerSocket
    private lateinit var sendReceived: SendReceive
    private lateinit var handel: android.os.Handler
    private var wifiItemAdd: WifiVIewModel
    private lateinit var content: String
    private lateinit var listaProductos: MutableList<listInventarioProductos>

    constructor(
        handler: android.os.Handler,
        wifiItem: WifiVIewModel,
        host: String,
        listaProductos: MutableList<listInventarioProductos>
    ) {
        this.handel = handler
        this.wifiItemAdd = wifiItem
        this.content = host
        this.listaProductos = listaProductos
    }

    override fun run() {
        try {
            Log.i("LLAMANDO", "SOCKETINICIADO")
            Ssock = ServerSocket(8888);
            sock = Ssock.accept()
            Log.i("LLAMANDO", "SOCKETLLAMANDOSENRECIE")

            sendReceived = SendReceive(sock, handel)
            Log.i("LLAMANDO", "SOCKETTERMINADO")


            wifiItemAdd.addDeviceConected(content, sendReceived)

            sendReceived.start()

            Log.i("ENVIANDO DATOS", listaProductos.toString())
            try {
                for (x in listaProductos) {
                    Log.i("Enviando Datos", listaProductos.toString())
//                    var res =("PROC|" + x.uid + ";" + x.nombre + ";" + x.precioC + ";" + x.precioU + ";"+java.util.Base64.getEncoder().encodeToString(x.imageBit))
                    var res =("PROC|" + x.uid + ";" + x.nombre + ";" + x.precioC + ";" + x.precioU + ";"+null)
                    Log.i("Enviando DATOS ZERIALIZADOS",res.toString())
                    sendReceived.write(res.toByteArray())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.run()
    }

    fun returnRecived(): SendReceive {
        sleep(3000)
        return sendReceived
    }
}
