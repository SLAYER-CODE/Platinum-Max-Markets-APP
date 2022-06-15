package com.example.fromdeskhelper.ui.View.adapter
import Data.Producto
import Data.listInventarioProductos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_producto.view.*
import kotlinx.android.synthetic.main.item_producto.view.TEPrecio
import kotlinx.android.synthetic.main.item_producto.view.TNombre
import kotlinx.android.synthetic.main.item_producto_listview_shoping.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import kotlin.random.Random

class ShopingProductAdapter(
    var producto:MutableList<Producto>,
    var DeleteRelaton: ((Clinet: Int, Product: Int) -> Unit?)? = null,
    var id:Int?=null,
    var update: (() -> Unit?)? = null,
):
    RecyclerView.Adapter<ShopingProductAdapter.ImageHolder>() {

    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(ProductoAndImage: Producto,pos:Int) {

            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = ProductoAndImage.precioC.toString()
//            val imagenStream = ByteArrayInputStream(ProductoAndImage.)
//            val theimagen = BitmapFactory.decodeStream(imagenStream)
//            val flujo = ByteArrayOutputStream()
//            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
//            val newimage = flujo.toByteArray()
//            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
//            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
//            view.IVimagenItem.setImageBitmap(imagen)
            if(id!=null) {
                view.ButtonIdDeleteShoped.setOnClickListener {
                    removeItem(pos)
                    DeleteRelaton?.invoke(id!!, ProductoAndImage.uid)
                    update?.invoke()
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageHolder(layoutInflater.inflate(R.layout.item_producto_listview_shoping, parent, false))
    }



    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.render(producto[position],position)
    }

    override fun getItemCount(): Int {
        return producto.size
    }

    fun removeItem(position:Int):Boolean{
        try {
            producto.removeAt(position)
            notifyItemRemoved(position)
            return true
        }catch (ex:Exception){
            return false
        }
    }

}