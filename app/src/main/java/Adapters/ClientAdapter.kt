package Adapters

import Data.ClientList
import Data.listInventarioProductos
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.R
import kotlinx.android.synthetic.main.item_client_list.view.*
import kotlinx.android.synthetic.main.item_client_vent.view.*

class ClientAdapter(var Clients:MutableList<ClientList>):RecyclerView.Adapter<ClientAdapter.HolderClient>() {

    inner class HolderClient(val view:View):RecyclerView.ViewHolder(view){
        fun render(Cliente:ClientList){
            val ViewDrawable=DrawableCompat.wrap(view.TVClientVent.background);
            DrawableCompat.setTint(ViewDrawable,Cliente.color)

            view.TVClientVent.background=ViewDrawable;
//            val NewDrawable = (GradientDrawable())
//            NewDrawable.setStroke(3,Cliente.color)
//            NewDrawable.setColor(Color.BLACK)
//            view.TVClientVent.background=NewDrawable
            view.TVClientVent.setText(Cliente.number.toString());
        }
    }
    fun addClient(cliente:ClientList){
        Clients.add(cliente)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientAdapter.HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HolderClient(layoutInflater.inflate(R.layout.item_client_list, parent, false))
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Clients[position])
    }

    override fun getItemCount(): Int {
        return Clients.size
    }


}