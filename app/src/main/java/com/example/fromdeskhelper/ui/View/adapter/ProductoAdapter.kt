package com.example.fromdeskhelper.ui.View.adapter

import Data.listInventarioProductos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.UtilsShowMainViewModels
import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.random.Random


//import com.example.productos.databinding.ItemProductoBinding


private var reverse: Boolean = false;

class ProductoAdapter(
    var MyImage: List<listInventarioProductos>,
    var ViewModelCall: ShowMainViewModel?,var visualise:Int,var util:UtilsShowMainViewModels?=null
) :
    RecyclerView.Adapter<ProductoAdapter.ImageHolder>(),Filterable {
    private var ProductList:List<listInventarioProductos>
    public  var onItemListener: OnItemListener? = null
    var id = 0

    init {
        ProductList= MyImage
        onItemListener=null
    }

    internal  inner class RV_ItemListener : View.OnClickListener,
        View.OnLongClickListener {
        override fun onClick(view: View) {
            onItemListener?.OnItemClickListener(view, view.id)
        }

        override fun onLongClick(view: View): Boolean {
            onItemListener?.OnItemLongClickListener(view, view.id)
            return true
        }
    }
    inner class ImageHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun render(ProductoAndImage: listInventarioProductos) {

            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"
            view.TEPrecioU.text = "$\\${ProductoAndImage.precioU}"
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
            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
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

        fun renderNotImagen(ProductoAndImage: listInventarioProductos) {
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"

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

        fun renderListView(ProductoAndImage: listInventarioProductos) {
            view.TNombre.isSelected = true;
            view.TNombre.text = ProductoAndImage.nombre
            view.TEPrecio.text = "$\\${ProductoAndImage.precioC}"

            val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            val flujo = ByteArrayOutputStream()
            theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
            val newimage = flujo.toByteArray()
            val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
            view.ratingBar.rating = ((Random.nextFloat() * (view.ratingBar.numStars - 1)))
            view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
            view.IVimagenItem.setImageBitmap(imagen)

            view.SaveStorage.setOnClickListener {
                check(true)
                Log.i("SE CLICLEO","Item")
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



    fun setItem(inte:Int){
        visualise=inte
    }


    override fun getItemViewType(position: Int): Int {
        if (MyImage[position].imageBit == null) {
            return 3
        }

        return visualise
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: ImageHolder = ImageHolder(
//            layoutInflater.inflate(
//                R.layout.item_producto_reverse,
//                parent,
//                false
//            )
//        )
        var ItemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_reverse, parent,false)

        Log.i("primeroo",viewType.toString())
        if (viewType == 2) {
            ItemView =
                layoutInflater.inflate(
                    R.layout.item_producto_notimage,
                    parent,
                    false
            )
        } else if (viewType == 1) {
            if (reverse) {
                reverse = false;
                ItemView =
                    layoutInflater.inflate(R.layout.item_producto, parent, false)
            } else {
                reverse = true;
                ItemView =
                    layoutInflater.inflate(
                        R.layout.item_producto_reverse,
                        parent,
                        false
                )
            }
        } else if (viewType == 3) {
            ItemView = layoutInflater.inflate(R.layout.item_producto_listview, parent, false)

        }else if(viewType == 4){
            ItemView = layoutInflater.inflate(R.layout.item_producto_gridview, parent, false)
        }

        ItemView.id=id
        id++
        ItemView.setOnClickListener(RV_ItemListener());
        ItemView.setOnLongClickListener(RV_ItemListener());
        return ImageHolder(ItemView)
    }


    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        if (MyImage[position].imageBit != null) {
            holder.render(MyImage[position])
        } else {
            holder.renderNotImagen(MyImage[position])
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

    private var EvenFilter:Filter = object :Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var Filter:MutableList<listInventarioProductos> = mutableListOf();
            if(constraint==null ||  constraint?.length == 0){
                Filter.addAll(ProductList)
            }else{
                var filterPatter=constraint.toString().lowercase().trim()
                for (item:listInventarioProductos in ProductList){
                    if(item.nombre.lowercase().contains(filterPatter) || item.qr.toString().contains(filterPatter) ){
                        Filter.add(item)
                    }
                }
            }
            var Result:FilterResults = FilterResults()
            Result.values=Filter
            return Result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            MyImage= listOf()
            MyImage =(results?.values as MutableList<listInventarioProductos>).toList()
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

