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
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsModelAdapter
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





private var reverse:Boolean=false;
class LocalAdapter (var producto:List<ProductsModelAdapter>,var visualise:Int,var util: UtilsShowMainViewModels?=null):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {
    private var ProductList:List<ProductsModelAdapter>
    init {
        ProductList= producto
    }

    inner class ImageHolder(val view: ItemProductoBinding): RecyclerView.ViewHolder(view.root){
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
//        fun renderNotImagen(ProductoAndImage: ProductsModelAdapter){
//            view.TNombre.isSelected=true;
//            view.TNombre.text=ProductoAndImage.product_name
//            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"
//
//        }
    }
    inner class ImageHolderReverse(val view: ItemProductoReverseBinding): RecyclerView.ViewHolder(view.root){

        fun render(ProductoAndImage:ProductsModelAdapter){
            view.TNombre.isSelected=true;

            view.TNombre.text=ProductoAndImage.product_name

            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"

            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)

            view.IVimagenItem.setImageBitmap(imagen)
        }
    }

    inner class ImageHolderNotImage(val view: ItemProductoNotimageBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage:ProductsModelAdapter){
            view.TNombre.isSelected=true;
            view.TNombre.text=ProductoAndImage.product_name
            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"
        }
    }

    inner class ImageHolderListView(val view: ItemProductoListviewBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage:ProductsModelAdapter) {
            view.TNombre.isSelected = true;

            view.TNombre.text = ProductoAndImage.product_name

            view.TEPrecio.text = "$\\${ProductoAndImage.price_cantidad}"

            val imagen = BitmapFactory.decodeByteArray(
                ProductoAndImage.image_realation[0].second,
                0,
                ProductoAndImage.image_realation[0].second.size
            )
            view.IVimagenItem.transitionName = (ProductoAndImage.image_realation[0].first)

            view.IVimagenItem.setImageBitmap(imagen)
        }
    }


    inner class ImageHolderGridView(val view: ItemProductoGridviewBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage:ProductsModelAdapter){
            view.TNombre.isSelected=true;
            view.TNombre.text=ProductoAndImage.product_name
            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"
            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)
            view.IVimagenItem.setImageBitmap(imagen)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: LocalAdapter.ImageHolder = ImageHolderReverse(ItemProductoReverseBinding.inflate(layoutInflater,parent,false))
//
//            ImageHolder(
//            layoutInflater.inflate(
//                R.layout.item_producto_reverse,
//                parent,
//                false
//            )
//        )
        if (viewType == 1) {
            if (reverse) {
                reverse = false;

                return ImageHolder(ItemProductoBinding.inflate(layoutInflater,parent,false))
//                return ImageHolderReverse(layoutInflater.inflate(R.layout.item_producto, parent, false))
            } else {
                reverse = true;
                return ImageHolderReverse(ItemProductoReverseBinding.inflate(layoutInflater,parent,false))
//                ItemView= ImageHolder(
//                    layoutInflater.inflate(
//                        R.layout.item_producto_reverse,
//                        parent,
//                        false
//                    )
//                )

            }
        } else if (viewType == 2) {
//            ItemView=ImageHolder(
//                layoutInflater.inflate(
//                    R.layout.item_producto_notimage,
//                    parent,
//                    false
//                )
//            )
            return ImageHolderNotImage(ItemProductoNotimageBinding.inflate(layoutInflater,parent,false))

        }else if(viewType==3){
//            ItemView=ImageHolder(
//                layoutInflater.inflate(
//                    R.layout.item_producto_listview,
//                    parent,
//                    false
//                )
//            )
            return ImageHolderListView(ItemProductoListviewBinding.inflate(layoutInflater,parent,false))

        }else if(viewType == 4){
//            ItemView = ImageHolder(layoutInflater.inflate(R.layout.item_producto_gridview, parent, false))
            return ImageHolderGridView(ItemProductoGridviewBinding.inflate(layoutInflater,parent,false))
        }

        return ImageHolderReverse(ItemProductoReverseBinding.inflate(layoutInflater,parent,false))
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



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0->{
                (holder as ImageHolderReverse).render(producto[position])
            }
            1->{
                if(producto[position].image_realation?.size!=0) {
                    (holder as ImageHolder).render(producto[position])
                }else{
                    (holder as ImageHolderNotImage).render(producto[position])
                }
            }
            2->{
                (holder as ImageHolderNotImage).render(producto[position])
            }
            3->{
                (holder as ImageHolderListView).render(producto[position])
            }
            4->{
                (holder as ImageHolderGridView).render(producto[position])
            }
        }


//        if(producto[position].image_realation?.size!=0){
//            holder.render(producto[position])
//        }else{
//            holder.renderNotImagen(producto[position])
//        }

//        holder.view.setOnClickListener {
//
//        }
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



