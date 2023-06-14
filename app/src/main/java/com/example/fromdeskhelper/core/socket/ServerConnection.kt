package com.example.fromdeskhelper.core.socket

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.SocketTimeoutException


var SERVER_CONNECTION_LOCAL_SOCKET="CONNECTIONSOCKETLOCAL"
class ServerConnection(url: String,http:OkHttpClient?) {
    enum class ConnectionStatus {
        DISCONNECTED, CONNECTED
    }

    interface ServerListener {
        fun onNewMessage(message: String?)
        fun onStatusChange(status: ConnectionStatus?)
    }


    var mWebSocket: WebSocket? = null
    var mClient: OkHttpClient? = http
    var mServerUrl: String
    var mMessageHandler: Handler? = null
    var mStatusHandler: Handler? = null
    var mListener: ServerListener? = null

    init {
//        mClient = OkHttpClient.Builder()
//            .readTimeout(3, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)
//            .build()
        mServerUrl = url
    }

    private inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            val m = mStatusHandler!!.obtainMessage(0, ConnectionStatus.CONNECTED)
            mStatusHandler!!.sendMessage(m)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val m = mMessageHandler!!.obtainMessage(0, text)
            mMessageHandler!!.sendMessage(m)

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i(SERVER_CONNECTION_LOCAL_SOCKET,"FALLO|    :"+response.toString(),t)
        }

        override fun onClosed(
            webSocket: WebSocket,
            code: Int,
            reason: String
        ) {
            val m =
                mStatusHandler!!.obtainMessage(0, ConnectionStatus.DISCONNECTED)
            mStatusHandler!!.sendMessage(m)
        }
    }

    fun ChangePort(string: String){
        mServerUrl = string
    }
    fun connect(listener: ServerListener?):WebSocket? {

        val request = Request.Builder()
            .url(mServerUrl)
            .build()

        mWebSocket = mClient?.newWebSocket(request, SocketListener())
        mListener = listener
        mMessageHandler =
            Handler(Looper.getMainLooper()) { msg: Message ->
                mListener?.onNewMessage(msg.obj as String)
                true
            }
        mStatusHandler = Handler(Looper.getMainLooper()) { msg: Message ->
            mListener!!.onStatusChange(msg.obj as ConnectionStatus)
            true
        }
        return mWebSocket
    }

    fun disconnect() {
        mWebSocket?.cancel()
        mListener = null
        mMessageHandler?.removeCallbacksAndMessages(null)
        mStatusHandler?.removeCallbacksAndMessages(null)
    }

    fun sendMessage(message: String?):Boolean {
        try {
            return mWebSocket!!.send(message!!)
        }catch (ex:Exception){
            Log.i(SERVER_CONNECTION_LOCAL_SOCKET,"Ubo un erro al enviar el mensaje",ex)
            return false
        }
    }
}

//object MyServer {
//    @Throws(InterruptedException::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        Thread(Server()).start()
//    }
//
//    internal class Server : Runnable {
//        override fun run() {
//            var serverSocket: ServerSocket? = null
//            try {
//                serverSocket = ServerSocket(4444)
//                while (true) {
//                    try {
//                        val clientSocket: Socket = serverSocket.accept()
//                        val inputReader = BufferedReader(
//                            InputStreamReader(clientSocket.getInputStream())
//                        )
//                        println("Message from client: " + inputReader.readLine())
//                    } catch (ste: SocketTimeoutException) {
//                        ste.printStackTrace()
//                    }
//                }
//            } catch (ioe: IOException) {
//                ioe.printStackTrace()
//            } finally {
//                try {
//                    serverSocket?.close()
//                } catch (ioe: IOException) {
//                    ioe.printStackTrace()
//                }
//            }
//        }
//    }
//}
//class ServerConnection(url:string,)