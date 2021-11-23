package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.R
import kotlinx.android.synthetic.main.image_new_new.view.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri


class ImageAdapter (var MyImage:List<Uri>):
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    fun addImage(path:String)
    {
        val dato =MyImage.toMutableList()
        dato.add(0,Uri.parse(path))
        MyImage=dato.toList()
        notifyItemChanged(0)
    }
    fun addImage(path:Uri)
    {
        val dato =MyImage.toMutableList()
        dato.add(0,path)
        MyImage=dato.toList()
        notifyItemChanged(0)
    }

    class ImageHolder(val view: View):RecyclerView.ViewHolder(view){
        fun render(superImage:String){

//            val bmp = BitmapFactory.decodeByteArray(superImage, 0, superImage.size)
//            val bmp = assetsToBitmap("purpleFlower.png")
//            view.ImagenOrder.setImageBitmap(bmp)

              view.ImagenOrder.setImageURI(Uri.parse(superImage))
        }
        fun render(superImage: Uri){
            view.ImagenOrder.setImageURI(superImage)
        }

    }
    public fun clearimagen(){
        val dato = MyImage.toMutableList()
        val size = MyImage.size;
        dato.clear()
        MyImage=dato.toList();
        notifyItemRangeRemoved(0,size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageHolder(layoutInflater.inflate(R.layout.image_new_new,parent,false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.render(MyImage[position])
    }

    override fun getItemCount(): Int {
        return MyImage.size
    }

}