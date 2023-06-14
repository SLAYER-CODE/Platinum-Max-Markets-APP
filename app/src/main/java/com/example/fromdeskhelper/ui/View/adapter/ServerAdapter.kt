package com.example.fromdeskhelper.ui.View.adapter

import Data.listInventarioProductos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ItemProductoBinding
import com.example.fromdeskhelper.databinding.ItemProductoGridviewBinding
import com.example.fromdeskhelper.databinding.ItemProductoListviewBinding
import com.example.fromdeskhelper.databinding.ItemProductoNotimageBinding
import com.example.fromdeskhelper.databinding.ItemProductoReverseBinding
import com.example.fromdeskhelper.ui.View.ViewModel.UtilsShowMainViewModels
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

//import com.example.productos.databinding.ItemProductoBinding


private var reverse: Boolean = false;

class ServerAdapter(var producto: List<ProductsPreviewQuery.Producto>,var itemLayout:Int,var util: UtilsShowMainViewModels?=null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {
    private var ProductList:List<ProductsPreviewQuery.Producto>
    init {
        ProductList= producto
    }
    inner class ImageHolder(val view: ItemProductoBinding) : RecyclerView.ViewHolder(view.root) {

        fun render(ProductoAndImage: ProductsPreviewQuery.Producto) {

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            view.TNombre.isSelected = true;

            view.TNombre.text = ProductoAndImage.product_name

            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"


            //FUNCION QUE APLICA LA IMAGEN DENTRO DE TVIMAGENITEM
//            val options = BitmapFactory.Options().apply {
//                inJustDecodeBounds = false
//                outWidth=200
//                outHeight=200
//            }
//            val imagenStream=ByteArrayInputStream(ProductoAndImage.imageBit)
//            val theimagen = BitmapFactory.decodeStream(imagenStream)
//            val flujo = ByteArrayOutputStream()
//            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10,flujo);
//            val newimage = flujo.toByteArray()
//            val imagen = BitmapFactory.decodeByteArray(newimage,0,newimage.size)
//
//            view.IVimagenItem.transitionName=(ProductoAndImage.uid.toString())
//            view.IVimagenItem.setImageBitmap(imagen)
            Picasso.get()
                .load("http://192.168.0.17:2016/uploads/" + ProductoAndImage.image_realation!![0].image_name)
                .fit()
                .centerCrop()
                .into(view.IVimagenItem)
        }

        fun renderNotImagen(ProductoAndImage: ProductsPreviewQuery.Producto) {
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.product_name
            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"

        }

    }
    inner class ImageHolderReverse(val view: ItemProductoReverseBinding): RecyclerView.ViewHolder(view.root){

        fun render(ProductoAndImage: ProductsPreviewQuery.Producto){
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.product_name
            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"
            Picasso.get()
                .load("http://192.168.0.17:2016/uploads/" + ProductoAndImage.image_realation!![0].image_name)
                .fit()
                .centerCrop()
                .into(view.IVimagenItem)
        }
    }

    inner class ImageHolderNotImage(val view: ItemProductoNotimageBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage: ProductsPreviewQuery.Producto){
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.product_name
            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"
        }
    }

    inner class ImageHolderListView(val view: ItemProductoListviewBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage: ProductsPreviewQuery.Producto) {
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.product_name
            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"
            Picasso.get()
                .load("http://192.168.0.17:2016/uploads/" + ProductoAndImage.image_realation!![0].image_name)
                .fit()
                .centerCrop()
                .into(view.IVimagenItem)
        }
    }


    inner class ImageHolderGridView(val view: ItemProductoGridviewBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage: ProductsPreviewQuery.Producto){
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.product_name
            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"
            Picasso.get()
                .load("http://192.168.0.17:2016/uploads/" + ProductoAndImage.image_realation!![0].image_name)
                .fit()
                .centerCrop()
                .into(view.IVimagenItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (producto[position].image_realation?.size == 0) {
            return 3
        }
        return  itemLayout
    }
    fun setItem(inte:Int){
        itemLayout=inte
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: ServerAdapter.ImageHolder = ImageHolder(
//            layoutInflater.inflate(
//                R.layout.item_producto,
//                parent,
//                false
//            )
//        )
        var ItemView:RecyclerView.ViewHolder =ImageHolder(ItemProductoBinding.inflate(layoutInflater,parent,false))
        if (viewType == 1) {
//            if (reverse) {
            reverse = false;
//                ItemView= ImageHolder(layoutInflater.inflate(R.layout.item_producto, parent, false))
            ItemView=ImageHolder(ItemProductoBinding.inflate(layoutInflater,parent,false))

//            } else {
//                reverse = true;
//                ItemView= ImageHolder(
//                    layoutInflater.inflate(
//                        R.layout.item_producto,
//                        parent,
//                        false
//                    )
//                )
//            }
        } else if (viewType == 2) {
//            ItemView=ImageHolder(
//                layoutInflater.inflate(
//                    R.layout.item_producto_notimage,
//                    parent,
//                    false
//                )
//            )
            ItemView=ImageHolderNotImage(ItemProductoNotimageBinding.inflate(layoutInflater,parent,false))

        }else if(viewType==3){
//            ItemView=ImageHolder(
//                layoutInflater.inflate(
//                    R.layout.item_producto_listview,
//                    parent,
//                    false
//                )
//            )
            ItemView=ImageHolderListView(ItemProductoListviewBinding.inflate(layoutInflater,parent,false))
        }else if(viewType == 4){
//            ItemView = ImageHolder(layoutInflater.inflate(R.layout.item_producto_gridview, parent, false))
            ItemView=ImageHolderGridView(ItemProductoGridviewBinding.inflate(layoutInflater,parent,false))
        }
        return ItemView
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            0->{
                (holder as ServerAdapter.ImageHolderReverse).render(producto[position])
            }
            1->{
                if(producto[position].image_realation?.size != 0) {
                    (holder as ServerAdapter.ImageHolder).render(producto[position])
                }else{
                    (holder as ServerAdapter.ImageHolderNotImage).render(producto[position])
                }
            }
            2->{
                (holder as ServerAdapter.ImageHolderNotImage).render(producto[position])
            }
            3->{
                (holder as ServerAdapter.ImageHolderListView).render(producto[position])
            }
            4->{
                (holder as ServerAdapter.ImageHolderGridView).render(producto[position])
            }
        }

//
//        if (producto[position].image_realation?.size != 0) {
//            holder.render(producto[position])
//        } else {
//            holder.renderNotImagen(producto[position])
//        }
//
//        holder.view.setOnClickListener {
//        }
//
    }

//    fun addNewListCurrent(newlist: List<listInventarioProductos>){
//        val initPosition = MyImage.size
//        val newnewList =MyImage.toMutableList()
//
//        newnewList.addAll(MyImage.size,newlist)
//        MyImage=newnewList.toList()
//        println(initPosition.toString()+"Se ejecuto Ka agregacion"+MyImage.size.toString())
//        notifyItemRangeInserted(initPosition, MyImage.size)
//        notifyDataSetChanged()
//    }

    override fun getFilter(): Filter {
        return EvenFilter
    }

    private var EvenFilter: Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var Filter:MutableList<ProductsPreviewQuery.Producto> = mutableListOf();
            if(constraint==null ||  constraint?.length == 0){
                Filter.addAll(ProductList)
            }else{
                var filterPatter=constraint.toString().lowercase().trim()
                for (item:ProductsPreviewQuery.Producto in ProductList){
                    if(item.product_name.lowercase().contains(filterPatter) || item.qr.toString().contains(filterPatter) ){
                        Filter.add(item)
                    }
                }
            }
            var Result: FilterResults = FilterResults()
            Result.values=Filter
            return Result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            producto= listOf()
            producto =(results?.values as MutableList<ProductsPreviewQuery.Producto>).toList()
            notifyDataSetChanged()
            util?.GetServerCount(producto.size)
        }
    }


    override fun getItemCount(): Int {
        return producto.size
    }

}