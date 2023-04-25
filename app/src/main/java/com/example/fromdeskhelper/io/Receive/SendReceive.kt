package com.example.fromdeskhelper.io.Receive

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer

public class SendReceive:Thread{
    public final var MEESAGE_READ:Int=1;
    public lateinit var sock: Socket
    public lateinit var Istream: InputStream
    private lateinit var OStream : OutputStream
    private lateinit var handel: android.os.Handler
    constructor(skt: Socket, handler: android.os.Handler){
        sock=skt
        handel=handler
        try {
            Istream=sock.getInputStream();
            OStream=sock.getOutputStream();
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    override fun run() {
        while (sock!=null){
            var buffer:ByteArray=ByteArray(1024)
            var byte:Int;
            try {
                byte = Istream.read(buffer)
//                var size = ByteBuffer.wrap( )
                if(byte>0){
                    handel.obtainMessage(MEESAGE_READ,byte,-1,buffer).sendToTarget();
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        super.run()
    }
    fun write(byes:ByteArray){
        try {
            OStream.write(byes)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}
