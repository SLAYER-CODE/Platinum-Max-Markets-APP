package com.example.fromdeskhelper.ui.View.adapter.Client

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.ProductsPreviewOffertsQuery
import com.example.fromdeskhelper.databinding.ItemClientHomeOffetsListBinding
import com.squareup.picasso.Picasso


//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TDetails
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TTitle
//import kotlinx.android.synthetic.main.item_menu_home_list.view.lottieAnimationView

class OffertsAdapter(var Slogans:MutableList<ProductsPreviewOffertsQuery.Producto>):RecyclerView.Adapter<OffertsAdapter.HolderClient>() {


    public  var onItemListener: OffertsAdapter.OnItemListener? = null
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
    fun setOnItemListenerListener(listener: OffertsAdapter.OnItemListener) {
        onItemListener = listener
    }
    interface OnItemListener {
        fun OnItemTouch(view: View?, motion:MotionEvent?):Boolean
    }

    inner class HolderClient(val view:ItemClientHomeOffetsListBinding):RecyclerView.ViewHolder(view.root){
        fun render(item:ProductsPreviewOffertsQuery.Producto){
            view.TVNameDetails.text= item.product_name

            var price = item.price.toFloat()
            if(item.discount!=null){
                var discount = item.discount.toFloat()
                var porcentajediscount = ((discount*100)/price).toInt()
                view.TVPorcentajeDiscount.text = porcentajediscount.toString()
                view.TVPorcentajeDiscount.visibility=View.VISIBLE


                view.TVOldPrice.text = "$${price}"
                view.TVOldPrice.paintFlags = view.TVOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                view.TVOldPrice.visibility = View.VISIBLE

                price =- discount
            }
            var priceText = price.toString().split(".")
            view.TVPrice.text="S\\${priceText[0]}"
            view.TVPriceDouble.text=".${priceText[1]}"
            view.TVNameDetails.text = "${item.product_name} , ${item.description}"

            if(item.quantity_cantidad!=null && item.quantity_cantidad!=0){
                view.TVStockText.text = item.quantity_cantidad.toString()
                view.CVCantidad.visibility = View.VISIBLE
            }

            if(item.available_shipment!=null){
                view.sendProduct.visibility=View.VISIBLE
            }
            if(item.available_date!=null){
//                view.TVDate.text=item.available_date
                var date = item.available_date
                view.LLDDate.visibility=View.VISIBLE
                view.LLDDateT.visibility=View.VISIBLE
                view.TVDate.text=date.toString()
//                view.TVHora.text="${date.hours}:${date.minutes}"
            }

            if(item.available_now!=null){
                view.LLDNow.visibility=View.VISIBLE
                if(item.estableshment_relation?.mall_id?.direction_id!=null){
                    view.TVUbication.text=item.estableshment_relation.mall_id.direction_id.avenide
                    view.TVUbication.visibility = View.VISIBLE
                }
            }

            if(item.image_realation!=null && item.image_realation.isNotEmpty()){
                Picasso.get()
                    .load("http://192.168.0.17:2016/uploads/${item.image_realation[0].image_name}")
                    .fit()
                    .centerCrop()
                    .into(view.IVImage)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: View = layoutInflater
//            .inflate(R.layout.item_menu_home_list, parent,false)
        var ItemView = ItemClientHomeOffetsListBinding.inflate(layoutInflater,parent,false)
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