package com.example.fromdeskhelper.ui.View.adapter

import Data.listInventarioProductos
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.animation.content.Content
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ItemProductoBinding
import com.example.fromdeskhelper.databinding.ItemProductoGridviewBinding
import com.example.fromdeskhelper.databinding.ItemProductoListviewBinding
import com.example.fromdeskhelper.databinding.ItemProductoNotimageBinding
import com.example.fromdeskhelper.databinding.ItemProductoReverseBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.UtilsShowMainViewModels
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
//import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.random.Random


//import com.example.productos.databinding.ItemProductoBinding


private var reverse: Boolean = false;

class ProductoAdapter(
    var MyImage: List<listInventarioProductos>,
    var ViewModelCall: ShowMainViewModel?,
    var visualise: Int,
    var util: UtilsShowMainViewModels? = null,
    var Context:Context?=null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var ProductList: List<listInventarioProductos>
    public var onItemListener: OnItemListener? = null
    var id = 0

    init {
        ProductList = MyImage
        onItemListener = null
    }

    internal inner class RV_ItemListener : View.OnClickListener,
        View.OnLongClickListener {
        override fun onClick(view: View) {
            onItemListener?.OnItemClickListener(view, view.id)
        }

        override fun onLongClick(view: View): Boolean {
            onItemListener?.OnItemLongClickListener(view, view.id)
            return true
        }
    }


    inner class ImageHolderReverse(val view: ItemProductoReverseBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun render(ProductoAndImage: listInventarioProductos) {
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
            view.TEPriceNeto.text = "$\\${ProductoAndImage.precioNeto}"
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                outWidth = 200
                outHeight = 200
            }
//            val theimagen = BitmapFactory.decodeStream(imagenStream,Rect(0,0,200,200),options)
            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)
            view.SaveStorage.setOnClickListener {
                check(true)
                ViewModelCall?.AddResponse(ProductoAndImage)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }
        }
    }

    inner class ImageHolderNotImage(val view: ItemProductoNotimageBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun render(Product: listInventarioProductos) {
            view.TNombre.isSelected = true;
            view.TNombre.text = Product.nombre
            view.TEPrecio.text = "$.${Product.precioC}"
//            if(Product.)
            var PriceVent=Product.precioC
            if(Product.disconut!=null){
                PriceVent=-Product.disconut
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

            if(Product.disconut!=null){
                view.TEDiscount.text = Product.disconut.toString()
            }

            if(Product.precioNeto!=null){
                view.TEPriceNeto.text=Product.precioNeto.toString()
                view.TEGanacia.text=(Product.precioNeto-PriceVent).toString()
            }
            if(Product.qr!=null){
                view.TEQR.text=Product.qr
            }
            if(Product.detalles!=null){
                view.TEDetails.text = Product.detalles
            }

            if(Product.stockC!=null){
                view.TECantidad.text=Product.stockC.toString()
                if(Context!=null && Product.stockC_attr !=null){
                    view.TECantidadAttr.text=Context!!.resources.getStringArray(R.array.dlagsCantidad)[Product.stockC_attr]
                }
            }

            if(Product.stockU!=null){
                view.TECantidadU.text=Product.stockU.toString()
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
                ViewModelCall?.AddResponse(Product)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }
        }
    }

    inner class ImageHolderListView(val view: ItemProductoListviewBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun render(ProductoAndImage: listInventarioProductos) {

            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
            view.TEPrecioU.text = "$\\${ProductoAndImage.precioNeto}"
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                outWidth = 200
                outHeight = 200
            }
//            val theimagen = BitmapFactory.decodeStream(imagenStream,Rect(0,0,200,200),options)
            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)
            view.SaveStorage.setOnClickListener {
                check(true)
                ViewModelCall?.AddResponse(ProductoAndImage)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }

        }
    }


    inner class ImageHolderGridView(val view: ItemProductoGridviewBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun render(ProductoAndImage: listInventarioProductos) {

            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
            view.TEPrecioU.text = "$\\${ProductoAndImage.precioNeto}"
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                outWidth = 200
                outHeight = 200
            }
//            val theimagen = BitmapFactory.decodeStream(imagenStream,Rect(0,0,200,200),options)
            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)
            view.SaveStorage.setOnClickListener {
                check(true)
                ViewModelCall?.AddResponse(ProductoAndImage)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }
        }
    }

    inner class ImageHolder(val view: ItemProductoBinding) : RecyclerView.ViewHolder(view.root) {

        fun render(ProductoAndImage: listInventarioProductos) {

            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
            view.TEPriceNeto.text = "$\\${ProductoAndImage.precioNeto}"
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                outWidth = 200
                outHeight = 200
            }
//            val theimagen = BitmapFactory.decodeStream(imagenStream,Rect(0,0,200,200),options)
            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)
            view.SaveStorage.setOnClickListener {
                check(true)
                ViewModelCall?.AddResponse(ProductoAndImage)
            }
            view.BSincronise.setOnClickListener {
                check(true)

            }
            view.BSLocal.setOnClickListener {
                check(true)
            }

//            view.setOnClickListener{
//                val extras = FragmentNavigatorExtras(view.IVimagenItem to "image_big")
//                println("Carlos!!")
//            }
        }

//        fun renderNotImagen(ProductoAndImage: listInventarioProductos) {
//            view.TNombre.isSelected = true;
//            view.TNombre.text = ProductoAndImage.nombre
//            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
//
//            view.SaveStorage.setOnClickListener {
//                check(true)
//                ViewModelCall?.AddResponse(ProductoAndImage)
//            }
//            view.BSincronise.setOnClickListener {
//                check(true)
//
//            }
//            view.BSLocal.setOnClickListener {
//                check(true)
//            }
//        }
//
//        fun renderListView(ProductoAndImage: listInventarioProductos) {
//            view.TNombre.isSelected = true;
//            view.TNombre.text = ProductoAndImage.nombre
//            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
//
//            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
//            val theimagen = BitmapFactory.decodeStream(imagenStream)
//            val flujo = ByteArrayOutputStream()
//            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
//            val newimage = flujo.toByteArray()
//            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
//            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
//            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
//            view.IVimagenItem.setImageBitmap(imagen)
//
//            view.SaveStorage.setOnClickListener {
//                check(true)
//                Log.i("SE CLICLEO","Item")
//                ViewModelCall?.AddResponse(ProductoAndImage)
//            }
//            view.BSincronise.setOnClickListener {
//                check(true)
//
//            }
//            view.BSLocal.setOnClickListener {
//                check(true)
//            }
//
//        }

    }


    fun setItem(inte: Int) {
        visualise = inte
    }


    override fun getItemViewType(position: Int): Int {
        if (MyImage[position].imageBit == null) {
            return 2
        }
        if(reverse){
            reverse=false
            return 0
        }
        reverse = true
        return visualise
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var ItemView: RecyclerView.ViewHolder =
            ImageHolder(ItemProductoBinding.inflate(layoutInflater, parent, false))

        when (viewType) {
            1 -> {

                ItemView = ImageHolderReverse(ItemProductoReverseBinding.inflate(layoutInflater, parent, false))

            }
            2 -> {
                ItemView = ImageHolderNotImage(
                    ItemProductoNotimageBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            3 -> {
                ItemView = ImageHolderListView(
                    ItemProductoListviewBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            4 -> {
                ItemView = ImageHolderGridView(
                    ItemProductoGridviewBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )

            }
        }

        ItemView.itemView.id = id
        id++
        ItemView.itemView.setOnClickListener(RV_ItemListener());
        ItemView.itemView.setOnLongClickListener(RV_ItemListener());
//        return ImageHolder(ItemView)
        return ItemView
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                (holder as ProductoAdapter.ImageHolderReverse).render(MyImage[position])
            }

            2 -> {
                (holder as ProductoAdapter.ImageHolderNotImage).render(MyImage[position])
            }

            3 -> {
                (holder as ProductoAdapter.ImageHolderListView).render(MyImage[position])
            }

            4 -> {
                (holder as ProductoAdapter.ImageHolderGridView).render(MyImage[position])
            }

            5 -> {
                (holder as ProductoAdapter.ImageHolderListView).render(MyImage[position])
            }
            else->{
                (holder as ProductoAdapter.ImageHolder).render(MyImage[position])
            }
        }
    }

    fun addNewListCurrent(newlist: List<listInventarioProductos>) {
        val initPosition = MyImage.size
        val newnewList = MyImage.toMutableList()
        newnewList.addAll(MyImage.size, newlist)
        MyImage = newnewList
        println(initPosition.toString() + "Se ejecuto Ka agregacion" + MyImage.size.toString())
        notifyItemRangeInserted(initPosition, MyImage.size)
//        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return MyImage.size
    }

    override fun getFilter(): Filter {
        return EvenFilter
    }

    private var EvenFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var Filter: MutableList<listInventarioProductos> = mutableListOf();
            if (constraint == null || constraint?.length == 0) {
                Filter.addAll(ProductList)
            } else {
                var filterPatter = constraint.toString().lowercase().trim()
                for (item: listInventarioProductos in ProductList) {
                    if (item.nombre.lowercase().contains(filterPatter) || item.qr.toString()
                            .contains(filterPatter)
                    ) {
                        Filter.add(item)
                    }
                }
            }
            var Result: FilterResults = FilterResults()
            Result.values = Filter
            return Result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            MyImage = listOf()
            MyImage = (results?.values as MutableList<listInventarioProductos>).toList()
            util?.GetStorageCount(MyImage.size)
            notifyDataSetChanged()
        }

    }

    interface OnItemListener {
        fun OnItemClickListener(view: View?, position: Int)
        fun OnItemLongClickListener(view: View?, position: Int)
    }


    fun setOnItemListenerListener(listener: OnItemListener) {
        onItemListener = listener
    }

}

