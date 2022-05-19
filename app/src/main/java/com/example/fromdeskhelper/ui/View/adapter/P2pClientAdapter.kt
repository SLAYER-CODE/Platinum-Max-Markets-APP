package com.example.fromdeskhelper.ui.View.adapter

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
import kotlinx.android.synthetic.main.image_new_new.view.*
import kotlinx.android.synthetic.main.item_client_list.view.*
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.item_producto.view.*
import java.lang.reflect.Method

class P2pClientAdapter (
    var DeviceList: MutableCollection<WifiP2pDevice>,
    var  mManager:WifiP2pManager?,
    var mChange:WifiP2pManager.Channel?):
    RecyclerView.Adapter<P2pClientAdapter.ClientP2PHolder>() {
    fun addClientP2p(Device:WifiP2pDevice)
    {
        DeviceList.add(Device)
        notifyItemRangeInserted(0,1)
//        notifyDataSetChanged()
        Log.i("P2PRECONECT","SE AGREGO!!")
    }
//    fun addImage(path: Uri)
//    {
//        Log.i("VIEWMODELADDPRODUCT","SE LLAMO"+path.toString())
//        MyImage.add(0,path)
//        notifyItemInserted(0)
//    }

    fun removeImagen(position:WifiP2pDevice){
        DeviceList.remove(position)
        notifyItemRemoved(DeviceList.size)
        return
    }

    inner class ClientP2PHolder(val view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("MissingPermission")
        fun render(ProductoAndImage: WifiP2pDevice){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            view
            view.NombreText.text=ProductoAndImage.deviceName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if(ProductoAndImage.wfdInfo!=null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        view.TDescription.text= ProductoAndImage.wfdInfo!!.deviceInfo.toString()
                    }else{
                        view.TDescription.text= "No es accionado android 10"
                    }
                }else{
                    view.TDescription.text="Not Permissions"
                }
            } else {

                view.TDescription.text="Not Suported Content"
            }

            view.TDeviceADDR.text = ProductoAndImage.deviceAddress
            view.TTipePrimary.text= ProductoAndImage.primaryDeviceType
            view.TTipeSecond.text=ProductoAndImage.secondaryDeviceType
            view.TEstate.text= ProductoAndImage.status.toString()
            view.TConectable.text=if(ProductoAndImage.isServiceDiscoveryCapable)"No es captable" else "Transpasable"
            view.LLItem.setOnClickListener {
                if(view.LContetDetails.visibility==View.GONE){
                    view.LContetDetails.visibility=View.VISIBLE
                }else{
                    Log.i("P2PRECONECT","VISTA SE PRODUJO")
                    view.LContetDetails.visibility=View.GONE
                }
                view.requestLayout()
            }
            view.BConnect.setOnClickListener {
                var device =  ProductoAndImage
                var config:WifiP2pConfig = WifiP2pConfig()
                config.deviceAddress=device.deviceAddress

                mManager?.connect(mChange,config,object:WifiP2pManager.ActionListener{
                    override fun onSuccess() {
//                        val ViewDrawable= DrawableCompat.wrap(view.background);
//                        DrawableCompat.setTint(ViewDrawable,Color.GREEN)
//                        view.background=ViewDrawable
                        view.NombreText.setTextColor(Color.GREEN)
                    }

                    override fun onFailure(p0: Int) {
//                        val ViewDrawable= DrawableCompat.wrap(view.background);
//                        DrawableCompat.setTint(ViewDrawable,Color.RED)
//                        view.background=ViewDrawable
                        view.NombreText.setTextColor(Color.RED)

                    }

                })
            }
            view.BDiscconnect.setOnClickListener {

            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientP2PHolder {
        Log.i("P2PRECONECT","SE AGREGO!! SE CREOOOO")
        val layoutInflater = LayoutInflater.from(parent.context)
        return ClientP2PHolder(
            layoutInflater.inflate(
                R.layout.item_device,
                parent,
                false
        )
        )
    }


    override fun onBindViewHolder(holder: P2pClientAdapter.ClientP2PHolder, position: Int) {
        Log.i("P2PRECONECT","SE AGREGO!! POSICIONADOOOOOO")
        holder.render(DeviceList.elementAt(position))
    }

    override fun getItemCount(): Int {
        return DeviceList.size
    }

}