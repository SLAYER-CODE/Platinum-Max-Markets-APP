package com.example.fromdeskhelper.ui.View.adapter.Client

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.ProductsPreviewOffertsQuery
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ItemClientHomeBrandListBinding
import com.example.fromdeskhelper.databinding.ItemClientHomeOffetsListBinding
import com.squareup.picasso.Picasso


//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TDetails
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TTitle
//import kotlinx.android.synthetic.main.item_menu_home_list.view.lottieAnimationView

class BrandsAdapter(var Slogans:MutableList<BrandsQuery.Brand>):RecyclerView.Adapter<BrandsAdapter.HolderClient>() {


    public  var onItemListener: BrandsAdapter.OnItemListener? = null
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

    interface OnItemListener {
        fun OnItemTouch(view: View?, motion:MotionEvent?):Boolean
    }

    inner class HolderClient(val view:ItemClientHomeBrandListBinding):RecyclerView.ViewHolder(view.root){
        fun render(item:BrandsQuery.Brand){
            var name = item.brand_name.lowercase()
            if(name=="sansumg") view.BrandImage.setImageResource(R.mipmap.sansumh)
            if(name=="sony") view.BrandImage.setImageResource(R.mipmap.sony)
            else if(name=="faber castel") view.BrandImage.setImageResource(R.mipmap.faber)
            else if(name=="iberica") view.BrandImage.setImageResource(R.mipmap.iberica)
            else if(name=="movistar") view.BrandImage.setImageResource(R.mipmap.movistart)
            else if(name=="claro") view.BrandImage.setImageResource(R.mipmap.clarologo)
            else if(name=="puma") view.BrandImage.setImageResource(R.mipmap.puma)
            else if(name=="adidas") view.BrandImage.setImageResource(R.mipmap.adidas)
            else if(name=="lg") view.BrandImage.setImageResource(R.mipmap.lg)
            else if(name=="huawei") view.BrandImage.setImageResource(R.mipmap.huawei)
            else if(name=="xiaomi") view.BrandImage.setImageResource(R.mipmap.xiaomi)
            view.nameBrand.text=name.capitalize()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: View = layoutInflater
//            .inflate(R.layout.item_menu_home_list, parent,false)
        var ItemView = ItemClientHomeBrandListBinding.inflate(layoutInflater,parent,false)
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