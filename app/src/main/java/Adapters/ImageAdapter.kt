package Adapters

import CallBacks.ItemTouchHelperViewHolder
import CallBacks.TouchDropListenerAction
import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.R
import kotlinx.android.synthetic.main.image_new_new.view.*

import android.net.Uri
import android.widget.FrameLayout
import android.widget.RelativeLayout


class ImageAdapter (var MyImage:MutableList<Uri>):
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    fun addImage(path:String)
    {
        MyImage.add(0, Uri.parse(path))
        notifyItemInserted(0)
    }
    fun addImage(path:Uri)
    {
        MyImage.add(0,path)
        notifyItemInserted(0)
    }

    fun addImage(position:Int,path: Uri){

    }

    fun restoreItem(path:String,position:Int){
        MyImage.add(position, Uri.parse(path))
        notifyItemInserted(position)
    }

    fun restoreItem(path:Uri,position:Int){
        MyImage.add(position,path)
        notifyItemInserted(position)
    }

    fun removeImagen(position:Int){
        MyImage.removeAt(position)
        notifyItemRemoved(position)
        return
    }

    class ImageHolder:RecyclerView.ViewHolder, ItemTouchHelperViewHolder {
        var viewF:RelativeLayout;
        var viewB:RelativeLayout;
        var viewParent:FrameLayout;
        var view:View;
        constructor(itemView: View) : super(itemView){
            viewF=itemView.relativeDos
            viewB=itemView.relativeUno
            viewParent=itemView.viewParent
            view=itemView
        }

        fun render(superImage: Uri){
            view.ImagenOrder.setImageURI(superImage)

//            view.setOnTouchListener(TouchDropListenerAction())
        }

        override fun onItemSelected() {
//            println("Funcion")
        }

        override fun onItemClear() {
//            println("Carlos")
        }

    }
    public fun clearimagen(){
        val size = MyImage.size;
        MyImage.clear()
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