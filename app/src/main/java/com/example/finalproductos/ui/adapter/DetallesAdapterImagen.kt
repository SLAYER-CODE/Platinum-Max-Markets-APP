package com.example.finalproductos.ui.adapter

import Data.ImagenesNew
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.R
import kotlinx.android.synthetic.main.item_image_detalles.view.*
import java.io.ByteArrayInputStream

private var primero:Boolean=true;
class DetallesAdapterImagen (private val mContext: Context,var MyImage:List<ImagenesNew>):
    RecyclerView.Adapter<DetallesAdapterImagen.ImageHolder>() {

    fun addImage()
    {
        notifyDataSetChanged()
    }

    class ImageHolder(val view: View):RecyclerView.ViewHolder(view){
        fun render(superImage:ImagenesNew){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            val imagenStream= ByteArrayInputStream(superImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            view.IVimagen.setImageBitmap(theimagen)
            if(primero){
                view.transitionName="transtionexit"
            }
//            view.ImagenOrder.setImageURI(Uri.parse(superImage))
        }

    }

    override fun getItemViewType(position: Int): Int {
        primero = position == 0
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ImageHolder(layoutInflater.inflate(R.layout.item_image_detalles,parent,false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.render(MyImage[position])
    }

    override fun getItemCount(): Int {
        return MyImage.size
    }

}