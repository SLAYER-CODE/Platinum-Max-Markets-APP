package com.example.fromdeskhelper.ui.View.adapter.Home

import Data.ClientList
import Data.ClientListGet
import Data.HomeListClient
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import kotlinx.android.synthetic.main.item_client_list.view.*
import kotlinx.android.synthetic.main.item_menu_home_list.view.TDetails
import kotlinx.android.synthetic.main.item_menu_home_list.view.TTitle


class HomeAdapter(var Clients:MutableList<HomeListClient>):RecyclerView.Adapter<HomeAdapter.HolderClient>() {

    inner class HolderClient(val view:View):RecyclerView.ViewHolder(view){
        fun render(Cliente:HomeListClient){
            view.TTitle.text=Cliente.title
            view.TDetails.text = Cliente.info
        }
    }
    fun addClient(cliente:HomeListClient){
        Clients.add(cliente)
        notifyDataSetChanged()
    }
    fun addClientFrist(cliente: HomeListClient){
        Clients.add(0,cliente)
        notifyItemRangeInserted(0,1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HolderClient(layoutInflater.inflate(R.layout.item_menu_home_list, parent, false))
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Clients[position])
    }

    override fun getItemCount(): Int {
        return Clients.size
    }


}