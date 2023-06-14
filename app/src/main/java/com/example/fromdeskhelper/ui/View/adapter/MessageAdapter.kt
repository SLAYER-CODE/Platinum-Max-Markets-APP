package com.example.fromdeskhelper.ui.View.adapter

import Data.ClientList
import Data.ClientListGet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ItemMessageBinding
//import kotlinx.android.synthetic.main.item_client_list.view.*


class MessageAdapter(var Clients:MutableList<Int>):RecyclerView.Adapter<MessageAdapter.HolderClient>() {

    inner class HolderClient(val view:ItemMessageBinding):RecyclerView.ViewHolder(view.root){
        fun render(Cliente:Int){
            view.TVClientVent.text=Cliente.toString()
        }
    }
    fun addClient(cliente:Int){
        Clients.add(cliente)
        notifyDataSetChanged()
    }
    fun addClientFrist(cliente: Int){
        Clients.add(0,cliente)
        notifyItemRangeInserted(0,1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
//        return HolderClient(layoutInflater.inflate(R.layout.item_message, parent, false))
        return HolderClient(ItemMessageBinding.inflate(layoutInflater,parent,false))
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Clients[position])
    }

    override fun getItemCount(): Int {
        return Clients.size
    }


}