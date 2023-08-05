package com.example.fromdeskhelper.ui.View.adapter
import Data.listInventarioProductos
import android.content.Context
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
class LocalAdapter (var producto:List<ProductsModelAdapter>,var visualise:Int,var util: UtilsShowMainViewModels?=null, var Context: Context?=null):
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

            view.TEPrecio.text="$\\${ProductoAndImage.price}"

            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)

            view.IVimagenItem.setImageBitmap(imagen)
//            Picasso.get()
//                .load("http://192.168.0.13:2016/uploads/"+ProductoAndImage.image_realation!![0].second).fit()
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

            view.TEPrecio.text="$\\${ProductoAndImage.price}"

            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)

            view.IVimagenItem.setImageBitmap(imagen)

        }
    }

    inner class ImageHolderNotImage(val view: ItemProductoNotimageBinding): RecyclerView.ViewHolder(view.root){
        fun render(Product:ProductsModelAdapter){
            view.TNombre.isSelected = true;
            view.TNombre.text = Product.product_name
            view.TEPrecio.text = "$.${Product.price}"
//            if(Product.)
            var PriceVent=Product.price
            if(Product.discount!=null){
                PriceVent=-Product.discount
            }
            if(Product.available_now!=null){
                view.TVDisponsedNow.text=if(Product.available_now) "Si" else "No"
            }

            if(Product.available_date!=null){
                view.TVNewStock.text= Product.available_date.toString()
            }

            if(Product.available_shipment!=null){
                view.TESend.text= if(Product.available_shipment) "Si" else "No"
            }

            if(Product.discount!=null){
                view.TEDiscount.text = Product.discount.toString()
            }

            if(Product.price_neto!=null){
                view.TEPriceNeto.text=Product.price_neto.toString()
                view.TEGanacia.text=(Product.price_neto-PriceVent).toString()
            }
            if(Product.qr!=null){
                view.TEQR.text=Product.qr
            }
            if(Product.description!=null){
                view.TEDetails.text = Product.description
            }

            if(Product.quantity_cantidad!=null){
                view.TECantidad.text=Product.quantity_cantidad.toString()
                if(Context!=null && Product.stockC_attr !=null){
                    view.TECantidadAttr.text=Context!!.resources.getStringArray(R.array.dlagsCantidad)[Product.stockC_attr]
                }
            }

            if(Product.quantity_unity!=null){
                view.TECantidadU.text=Product.quantity_unity.toString()
                if(Context!=null && Product.stockU_attr !=null){
                    view.TECantidadUAttr.text=Context!!.resources.getStringArray(R.array.dlagsType)[Product.stockU_attr]
                }
            }

            if(Product.peso!=null){
                view.TEPeso.text=Product.peso.toString()
                if(Context!=null && Product.peso_attr!=null){
                    view.TEPesoAttr.text=Context!!.resources.getStringArray(R.array.dlagsPeso)[Product.peso_attr]
                }
            }

            if(Product.stock_attr!=null && Context !=null){
                view.TEPesoAttr.text=Context!!.resources.getStringArray(R.array.dlagsParts)[Product.stock_attr]
            }

            view.TEPriceVent.text = "$PriceVent"

            view.SaveStorage.setOnClickListener {
                check(true)
//                ViewModelCall?.AddResponse(Product)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }
        }
    }

    inner class ImageHolderListView(val view: ItemProductoListviewBinding): RecyclerView.ViewHolder(view.root){
        fun render(ProductoAndImage:ProductsModelAdapter) {
            view.TNombre.isSelected = true;

            view.TNombre.text = ProductoAndImage.product_name

            view.TEPrecio.text = "$\\${ProductoAndImage.price}"

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
            view.TEPrecio.text="$\\${ProductoAndImage.price}"
            val imagen = BitmapFactory.decodeByteArray(ProductoAndImage.image_realation[0].second,0,ProductoAndImage.image_realation[0].second.size)
            view.IVimagenItem.transitionName=(ProductoAndImage.image_realation[0].first)
            view.IVimagenItem.setImageBitmap(imagen)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(producto[position].image_realation?.size==0) {
            return 2
        }

        if(reverse){
            reverse=false
            return 0
        }
        reverse = true
        return visualise
    }

    fun setItem(inte:Int){
        visualise=inte
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var ItemView: RecyclerView.ViewHolder = ImageHolder(ItemProductoBinding.inflate(layoutInflater,parent,false))
//
//            ImageHolder(
//            layoutInflater.inflate(
//                R.layout.item_producto_reverse,
//                parent,
//                false
//            )
//        )
        when (viewType) {
            1 -> {
                ItemView  = ImageHolderReverse(ItemProductoReverseBinding.inflate(layoutInflater,parent,false))

            }
            2 -> {

                ItemView = ImageHolderNotImage(ItemProductoNotimageBinding.inflate(layoutInflater,parent,false))

            }
            3 -> {

                ItemView = ImageHolderListView(ItemProductoListviewBinding.inflate(layoutInflater,parent,false))

            }
            4 -> {
                ItemView = ImageHolderGridView(ItemProductoGridviewBinding.inflate(layoutInflater,parent,false))
            }
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



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            1->{
                (holder as ImageHolderReverse).render(producto[position])
            }
            2->{
                (holder as ImageHolderNotImage).render(producto[position])
            }
            3->{
                (holder as ImageHolderListView).render(producto[position])
            }
            4->{
                (holder as ImageHolderGridView).render(producto[position])
            }else->{
                (holder as ImageHolder).render(producto[position])
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



