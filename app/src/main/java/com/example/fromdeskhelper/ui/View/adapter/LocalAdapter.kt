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
import com.example.fromdeskhelper.domain.Root.ProductsModelAdapter
import com.example.fromdeskhelper.ui.View.ViewModel.UtilsShowMainViewModels
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

//import com.example.productos.databinding.ItemProductoBinding





private var reverse:Boolean=false;
class LocalAdapter (var producto:List<ProductsModelAdapter>,var visualise:Int,var util: UtilsShowMainViewModels?=null):
    RecyclerView.Adapter<LocalAdapter.ImageHolder>(),Filterable {
    private var ProductList:List<ProductsModelAdapter>
    init {
        ProductList= producto
    }
    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(ProductoAndImage:ProductsModelAdapter){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            view.TNombre.isSelected=true;

            view.TNombre.text=ProductoAndImage.product_name

            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"

            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)

            view.IVimagenItem.setImageBitmap(imagen)
//            Picasso.get()
//                .load("http://192.168.0.13:2016/uploads/"+ProductoAndImage.image_realation!![0].image_name).fit()
//                .centerCrop()
//                .into(view.IVimagenItem)
        }
        fun renderNotImagen(ProductoAndImage: ProductsModelAdapter){
            view.TNombre.isSelected=true;
            view.TNombre.text=ProductoAndImage.product_name
            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"

        }

    }


    override fun getItemViewType(position: Int): Int {
        if(producto[position].image_realation?.size==0) {
            return 3
        }
        return visualise
    }

    fun setItem(inte:Int){
        visualise=inte
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var ItemView: LocalAdapter.ImageHolder = ImageHolder(
            layoutInflater.inflate(
                R.layout.item_producto_reverse,
                parent,
                false
            )
        )
        if (viewType == 1) {
            if (reverse) {
                reverse = false;
                ItemView= ImageHolder(layoutInflater.inflate(R.layout.item_producto, parent, false))
            } else {
                reverse = true;
                ItemView= ImageHolder(
                    layoutInflater.inflate(
                        R.layout.item_producto_reverse,
                        parent,
                        false
                    )
                )
            }
        } else if (viewType == 2) {
            ItemView=ImageHolder(
                layoutInflater.inflate(
                    R.layout.item_producto_notimage,
                    parent,
                    false
                )
            )
        }else if(viewType==3){
            ItemView=ImageHolder(
                layoutInflater.inflate(
                    R.layout.item_producto_listview,
                    parent,
                    false
                )
            )
        }else if(viewType == 4){
            ItemView = ImageHolder(layoutInflater.inflate(R.layout.item_producto_gridview, parent, false))
        }
        return ItemView

    }


    override fun getFilter(): Filter {
        return EvenFilter
    }

    private var EvenFilter: Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var Filter:MutableList<ProductsModelAdapter> = mutableListOf();
            if(constraint==null ||  constraint?.length == 0){
                Filter.addAll(ProductList)
            }else{
                var filterPatter=constraint.toString().lowercase().trim()
                for (item:ProductsModelAdapter in ProductList){
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
            producto =(results?.values as MutableList<ProductsModelAdapter>).toList()
            notifyDataSetChanged()
            util?.GetLocalCount(producto.size)

        }

    }



    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        if(producto[position].image_realation?.size!=0){
            holder.render(producto[position])
        }else{
            holder.renderNotImagen(producto[position])
        }
        holder.view.setOnClickListener {
            
        }
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

    override fun getItemCount(): Int {
        return producto.size
    }

}