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
import com.example.fromdeskhelper.databinding.ItemProductoListviewShopingContrateBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Client.FactureProduct
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.item_producto.view.*
//import kotlinx.android.synthetic.main.item_producto.view.TEPrecio
//import kotlinx.android.synthetic.main.item_producto.view.TNombre
//import kotlinx.android.synthetic.main.item_producto_listview_shoping.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import kotlin.random.Random

class ShopingContrateProductAdapter (
    var producto:MutableList<FactureProduct>,
    var DeleteRelaton: ((Clinet: Int, Product: Int) -> Unit?)? = null,
    var id:Int?=null,
    var update: (() -> Unit?)? = null,
):
    RecyclerView.Adapter<ShopingContrateProductAdapter.ImageHolder>() {

    inner class ImageHolder(val view: ItemProductoListviewShopingContrateBinding): RecyclerView.ViewHolder(view.root){

        fun render(ProductoAndImage: FactureProduct,pos:Int) {

            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = ProductoAndImage.price.toString()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        return ImageHolder(layoutInflater.inflate(R.layout.item_producto_listview_shoping_contrate, parent, false))
        return ImageHolder(ItemProductoListviewShopingContrateBinding.inflate(layoutInflater,parent, false))
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