package com.example.fromdeskhelper.ui.View.adapter

import Data.ImagenesNew
import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.item_image_detalles.view.*
import java.io.ByteArrayInputStream


private var primero:Boolean=true;
class DetallesAdapterImagen (private val mContext: Context,var MyImage:List<ImagenesNew>):
    PagerAdapter( ) {

//    fun addImage()
//    {
//        notifyDataSetChanged()
//    }

    class ImageHolder(val view: View):RecyclerView.ViewHolder(view){

        fun render(superImage:ImagenesNew){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)
            val imagenStream= ByteArrayInputStream(superImage.imageBit)
            val theimagen = BitmapFactory.decodeStream(imagenStream)
            view.IVimagen.setImageBitmap(theimagen)
//            if(primero){
//                view.transitionName="transtionexit"
//            }
//            view.ImagenOrder.setImageURI(Uri.parse(superImage))
        }

    }

//    override fun getItemViewType(position: Int): Int {
//        primero = position == 0
//        return super.getItemViewType(position)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
//        val layoutInflater = LayoutInflater.from(mContext)
//        return ImageHolder(layoutInflater.inflate(R.layout.item_image_detalles,parent,false))
//    }
//
//    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
//        holder.render(MyImage[position])
//    }


    override fun getCount(): Int {
        return MyImage.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view==`object`;
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imagenStream= ByteArrayInputStream(MyImage[position].imageBit)
        val theimagen = BitmapFactory.decodeStream(imagenStream)
        val imageView:ImageView=ImageView(mContext)
        imageView.setImageBitmap(theimagen)
        container.addView(imageView)

        if(position==0){
            imageView.transitionName="image_big"
        }

        return imageView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
//        super.destroyItem(container, position, `object`)
    }



}