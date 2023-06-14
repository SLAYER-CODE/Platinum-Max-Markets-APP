package com.example.fromdeskhelper.ui.View.adapter.Client

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.AsistentP2pBinding
import com.example.fromdeskhelper.databinding.ItemDeviceBinding
import com.example.fromdeskhelper.io.Receive.SendReceive
//import kotlinx.android.synthetic.main.asistent_p2p.view.*
//import kotlinx.android.synthetic.main.image_new_new.view.*
//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_device.view.*
//import kotlinx.android.synthetic.main.item_producto.view.*
//import kotlinx.android.synthetic.main.item_producto.view.TNombre
import java.lang.IllegalArgumentException
import java.lang.reflect.Method

class P2pClientConectedAdapter(
    var DeviceList: MutableCollection<Pair<SendReceive,Any>>,
    var mManager: WifiP2pManager?,
    var mChange: WifiP2pManager.Channel?
) :
    RecyclerView.Adapter<P2pClientConectedAdapter.ClientP2PHolder>() {
    fun addClientP2p(Device: Pair<SendReceive,Any>) {
        DeviceList.add(Device)
        notifyItemRangeInserted(0, 1)
//        notifyDataSetChanged()
        Log.i("P2PRECONECT", "SE AGREGO!!")
    }
//    fun addImage(path: Uri)
//    {
//        Log.i("VIEWMODELADDPRODUCT","SE LLAMO"+path.toString())
//        MyImage.add(0,path)
//        notifyItemInserted(0)
//    }

    fun removeImagen(position: Pair<SendReceive,Any>) {
        DeviceList.remove(position)
        notifyItemRemoved(DeviceList.size)
        return
    }

    inner class ClientP2PHolder(val view: ItemDeviceBinding) : RecyclerView.ViewHolder(view.root) {
        @SuppressLint("MissingPermission")
        fun render(ProductoAndImage: WifiP2pDevice) {

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)

            view.NombreText.text = ProductoAndImage.deviceName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ProductoAndImage.wfdInfo != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        view.TDescription.text = ProductoAndImage.wfdInfo!!.deviceInfo.toString()
                    } else {
                        view.TDescription.text = "No es accionado android 10"
                    }
                } else {
                    view.TDescription.text = "Not Permissions"
                }
            } else {

                view.TDescription.text = "Not Suported Content"
            }

            view.TDeviceADDR.text = ProductoAndImage.deviceAddress
            view.TTipePrimary.text = ProductoAndImage.primaryDeviceType
            view.TTipeSecond.text = ProductoAndImage.secondaryDeviceType


//            view.TEstate.text = ProductoAndImage.status.toString()
//            view.TConectable.text = if (ProductoAndImage.isServiceDiscoveryCapable) "No es captable" else "Transpasable"
//            view.LLItem.setOnClickListener {
//                if (view.LContetDetails.visibility == View.GONE) {
//                    view.LContetDetails.visibility = View.VISIBLE
//                } else {
//                    Log.i("P2PRECONECT", "VISTA SE PRODUJO")
//                    view.LContetDetails.visibility = View.GONE
//                }
//                view.requestLayout()
//            }
//            view.BConnect.setOnClickListener {
//                var device = ProductoAndImage
//                var config: WifiP2pConfig = WifiP2pConfig()
//                config.deviceAddress = device.deviceAddress
//
//                mManager?.connect(mChange, config, object : WifiP2pManager.ActionListener {
//                    override fun onSuccess() {
////                        val ViewDrawable= DrawableCompat.wrap(view.background);
////                        DrawableCompat.setTint(ViewDrawable,Color.GREEN)
////                        view.background=ViewDrawable
//                        view.NombreText.setTextColor(Color.GREEN)
//                    }
//
//                    override fun onFailure(p0: Int) {
////                        val ViewDrawable= DrawableCompat.wrap(view.background);
////                        DrawableCompat.setTint(ViewDrawable,Color.RED)
////                        view.background=ViewDrawable
//                        view.NombreText.setTextColor(Color.RED)
//
//                    }
//
//                })
//            }
//            view.BDiscconnect.setOnClickListener {
//
//            }

        }

        fun RenderSecure(name:String,sendReceive: SendReceive){
//            view.TNombre.text = name
//            view.BAlerUser.setOnClickListener {
//                sendReceive.write("Un cliente necesita tu ayuda Ubicacion [-5122112.2,+4122112.2] Segundo Piso Abajo".toByteArray())
//            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        var item=DeviceList.elementAt(position)
        var type= TYPE_SECURE
        if(item.second is WifiP2pDevice){
            type= TYPE_DETAILS
        }else if(item.second is String){
            type= TYPE_SECURE
        }
        return super.getItemViewType(type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientP2PHolder {
        Log.i("P2PRECONECT", "SE AGREGO!! SE CREOOOO")
        val layoutInflater = LayoutInflater.from(parent.context)
//        val layout=when(viewType){
//            TYPE_SECURE-> layoutInflater.inflate(
//                R.layout.asistent_p2p,
//                parent,
//                false
//            )
//            TYPE_DETAILS->
//                layoutInflater.inflate(
//                    R.layout.asistent_p2p,
//                    parent,
//                    false
//                )
//            else->throw IllegalArgumentException("TYPO INVALIDO NO UBICADO!! :D")
//        }
        return ClientP2PHolder(ItemDeviceBinding.inflate(layoutInflater,parent,false))
//        return ClientP2PHolder(
//            layoutInflater.inflate(
//                R.layout.asistent_p2p,
//                parent,
//                false
//            )
//        )
    }


    override fun onBindViewHolder(holder: P2pClientConectedAdapter.ClientP2PHolder, position: Int) {
        Log.i("P2PRECONECT", "SE AGREGO!! POSICIONADOOOOOO")
        var item=DeviceList.elementAt(position)
        if(item.second is WifiP2pDevice){
            holder.render(item.second as WifiP2pDevice)
        }else if(item.second is String){

//            holder.RenderSecure(item.second as String,item.first)

        }else{
            Log.w("ADAPTER","No existe este typo de dato"+item.toString())
        }

    }

    override fun getItemCount(): Int {
        return DeviceList.size
    }
    companion object {
        private const val TYPE_DETAILS = 0
        private const val TYPE_SECURE = 1
    }
}