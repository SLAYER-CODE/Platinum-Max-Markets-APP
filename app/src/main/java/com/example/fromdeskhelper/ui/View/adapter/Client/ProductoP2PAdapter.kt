package com.example.fromdeskhelper.ui.View.adapter.Client
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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
//import com.example.productos.databinding.ItemProductoBinding





class ProductoP2PAdapter (var producto:List<ProductsPreviewQuery.Producto>):
    RecyclerView.Adapter<ProductoP2PAdapter.ImageHolder>() {

    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view){

        fun render(ProductoAndImage:ProductsPreviewQuery.Producto){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            view.TNombre.isSelected=true;

            view.TNombre.text=ProductoAndImage.product_name

            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"


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
                .load("http://192.168.0.17:2016/uploads/"+ProductoAndImage.image_realation!![0].image_name).fit()
                .centerCrop()
                .into(view.IVimagenItem)
        }
        fun renderNotImagen(ProductoAndImage: ProductsPreviewQuery.Producto){
            view.TNombre.isSelected=true;
            view.TNombre.text=ProductoAndImage.product_name
            view.TEPrecio.text="$\\${ProductoAndImage.price_cantidad}"

        }

    }


    override fun getItemViewType(position: Int): Int {
        if(producto[position].image_realation?.size==0) {
            return 2
        }
        return super.getItemViewType(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageHolder(layoutInflater.inflate(R.layout.item_producto_p2p, parent, false))
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