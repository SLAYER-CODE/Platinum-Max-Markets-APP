package com.example.fromdeskhelper.ui.View.adapter

import Data.ClientList
import Data.ClientListGet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ItemClientListBinding
//import kotlinx.android.synthetic.main.item_client_list.view.*


class ClientAdapter(var Clients:MutableList<ClientListGet>):RecyclerView.Adapter<ClientAdapter.HolderClient>() {

    inner class HolderClient(val view:ItemClientListBinding):RecyclerView.ViewHolder(view.root){
        fun render(Cliente:ClientListGet){
            val ViewDrawable=DrawableCompat.wrap(view.TVClientVent.background);
            DrawableCompat.setTint(ViewDrawable,Cliente.color)

            view.TVClientVent.background=ViewDrawable;
//            val NewDrawable = (GradientDrawable())
//            NewDrawable.setStroke(3,Cliente.color)
//            NewDrawable.setColor(Color.BLACK)
//            view.TVClientVent.background=NewDrawable
            view.TVClientVent.setText(Cliente.uid.toString());
        }
    }
    fun addClient(cliente:ClientListGet){
        Clients.add(cliente)
        notifyDataSetChanged()
    }
    fun addClientFrist(cliente: ClientListGet){
        Clients.add(0,cliente)
        notifyItemRangeInserted(0,1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HolderClient(ItemClientListBinding.inflate(layoutInflater,parent,false))
//        return HolderClient(layoutInflater.inflate(R.layout.item_client_list, parent, false))
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Clients[position])
    }

    override fun getItemCount(): Int {
        return Clients.size
    }


}