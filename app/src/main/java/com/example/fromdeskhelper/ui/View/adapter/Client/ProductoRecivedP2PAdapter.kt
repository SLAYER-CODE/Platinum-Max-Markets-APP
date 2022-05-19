package com.example.fromdeskhelper.ui.View.adapter.Client
import Data.listInventarioProductos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import kotlinx.android.synthetic.main.item_producto.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.random.Random

//import com.example.productos.databinding.ItemProductoBinding





private var reverse:Boolean=false;
class ProductoRecivedP2PAdapter (var MyImage:List<listInventarioProductos>,var ViewModelCall:ShowMainViewModel?):
    RecyclerView.Adapter<ProductoRecivedP2PAdapter.ImageHolder>() {

    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(ProductoAndImage:listInventarioProductos){

            view.TNombre.text=ProductoAndImage.nombre
            view.TEPrecio.text="$\\${ProductoAndImage.precioC}"
            view.TEPrecioU.text="$\\${ProductoAndImage.precioU}"
            view.ratingBar.rating=((Random.nextFloat()*(view.ratingBar.numStars-1)))
            if(ProductoAndImage.imageBit!=null) {
                val imagenStream = ByteArrayInputStream(ProductoAndImage.imageBit)
                val theimagen = BitmapFactory.decodeStream(imagenStream)
                val flujo = ByteArrayOutputStream()
                theimagen?.compress(Bitmap.CompressFormat.JPEG, 10, flujo);
                val newimage = flujo.toByteArray()
                val imagen = BitmapFactory.decodeByteArray(newimage, 0, newimage.size)
                view.IVimagenItem.transitionName = (ProductoAndImage.uid.toString())
                view.IVimagenItem.setImageBitmap(imagen)
            }
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
        return ImageHolder(layoutInflater.inflate(R.layout.item_producto_p2p, parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.render(MyImage[position])
    }

    override fun getItemCount(): Int {
        return MyImage.size
    }

}