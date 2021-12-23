package com.example.finalproductos.ui.adapter

import Data.listInventarioProductos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.R
import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

//import com.example.productos.databinding.ItemProductoBinding





private var reverse:Boolean=false;
class ProductoAdapter (var MyImage:List<listInventarioProductos>):
    RecyclerView.Adapter<ProductoAdapter.ImageHolder>() {

    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(ProductoAndImage:listInventarioProductos){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            view.TNombre.isSelected=true;

            view.TNombre.text=ProductoAndImage.nombre
            view.TEPrecio.text="$\\${ProductoAndImage.precioC}"
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                outWidth=200
                outHeight=200
            }
//            val theimagen = BitmapFactory.decodeStream(imagenStream,Rect(0,0,200,200),options)
            val imagenStream=ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10,flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage,0,newimage.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)
//            view.setOnClickListener{
//                val extras = FragmentNavigatorExtras(view.IVimagenItem to "image_big")
//                println("Carlos!!")
//            }
        }
        fun renderNotImagen(ProductoAndImage: listInventarioProductos){
            view.TNombre.isSelected=true;
            view.TNombre.text=ProductoAndImage.nombre
            view.TEPrecio.text="$\\${ProductoAndImage.precioC}"

        }

    }


    override fun getItemViewType(position: Int): Int {
        if(MyImage[position].imageBit==null) {
            return 2
        }
        return super.getItemViewType(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == 2) {
            return ImageHolder(
                layoutInflater.inflate(
                    R.layout.item_producto_notimage,
                    parent,
                    false
                )
            )
        }
            if (reverse) {
                reverse = false;
                return ImageHolder(layoutInflater.inflate(R.layout.item_producto, parent, false))
            } else {
                reverse = true;
                return ImageHolder(
                    layoutInflater.inflate(
                        R.layout.item_producto_reverse,
                        parent,
                        false
                    )
                )
            }

    }



    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        if(MyImage[position].imageBit!=null){
            holder.render(MyImage[position])
        }else{
            holder.renderNotImagen(MyImage[position])
        }
        holder.view.setOnClickListener {
            
        }
    }

    fun addNewListCurrent(newlist: List<listInventarioProductos>){
        val initPosition = MyImage.size
        val newnewList =MyImage.toMutableList()

        newnewList.addAll(MyImage.size,newlist)
        MyImage=newnewList.toList()
        notifyItemRangeInserted(initPosition, MyImage.size)
    }

    override fun getItemCount(): Int {
        return MyImage.size
    }

}