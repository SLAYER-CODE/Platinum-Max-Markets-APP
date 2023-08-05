package com.example.fromdeskhelper.ui.View.adapter.Client

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.SloganImagesGetQuery
import com.example.fromdeskhelper.data.model.objects.Function
import com.example.fromdeskhelper.databinding.ItemClientHomeSloganListBinding
import com.example.fromdeskhelper.databinding.ItemMenuHomeListBinding
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.squareup.picasso.Picasso

//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TDetails
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TTitle
//import kotlinx.android.synthetic.main.item_menu_home_list.view.lottieAnimationView


var LOG_ADAPTER="HomeAdapter"
class SloganAdapter(var Slogans:MutableList<SloganImagesGetQuery.SloganImagesGet>):RecyclerView.Adapter<SloganAdapter.HolderClient>() {


    public  var onItemListener: SloganAdapter.OnItemListener? = null
    init {
        onItemListener=null
    }
    internal inner class RV_ItemListener :
        View.OnTouchListener {
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            onItemListener?.OnItemTouch(p0, p1)
            return true
        }
    }
    fun setOnItemListenerListener(listener: SloganAdapter.OnItemListener) {
        onItemListener = listener
    }
    interface OnItemListener {
        fun OnItemTouch(view: View?, motion:MotionEvent?):Boolean
    }

    inner class HolderClient(val view:ItemClientHomeSloganListBinding):RecyclerView.ViewHolder(view.root){
        fun render(item:SloganImagesGetQuery.SloganImagesGet){
            Picasso.get()
                .load("http://192.168.0.17:2016/slogan/" + item.image_name)
                .fit()
                .centerCrop()
                .into(view.slogan)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: View = layoutInflater
//            .inflate(R.layout.item_menu_home_list, parent,false)
        var ItemView = ItemClientHomeSloganListBinding.inflate(layoutInflater,parent,false)
        ItemView.root.setOnTouchListener(RV_ItemListener())
        return HolderClient(ItemView)
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Slogans[position])
    }

    override fun getItemCount(): Int {
        return Slogans.size
    }


}