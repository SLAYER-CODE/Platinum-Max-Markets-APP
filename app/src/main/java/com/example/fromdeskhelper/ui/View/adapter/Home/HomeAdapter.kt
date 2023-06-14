package com.example.fromdeskhelper.ui.View.adapter.Home

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
import com.example.fromdeskhelper.data.model.objects.Function
import com.example.fromdeskhelper.databinding.ItemMenuHomeListBinding
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter

//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TDetails
//import kotlinx.android.synthetic.main.item_menu_home_list.view.TTitle
//import kotlinx.android.synthetic.main.item_menu_home_list.view.lottieAnimationView


var LOG_ADAPTER="HomeAdapter"
class HomeAdapter(var Clients:MutableList<GetRolesAdminQuery.Fuction>,var context:Context?):RecyclerView.Adapter<HomeAdapter.HolderClient>() {


    public  var onItemListener: HomeAdapter.OnItemListener? = null
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
    fun setOnItemListenerListener(listener: HomeAdapter.OnItemListener) {
        onItemListener = listener
    }
    interface OnItemListener {
        fun OnItemTouch(view: View?, motion:MotionEvent?):Boolean
    }

    inner class HolderClient(val view:ItemMenuHomeListBinding):RecyclerView.ViewHolder(view.root){
        fun render(item:GetRolesAdminQuery.Fuction){
//            var back = DrawableCompat.wrap(view.background)
            var id = item.id_fuction.toInt()

//            var item = mapOf(
//                Pair(
//                    Function.Actions,
//                    mapOf(
//                        Pair("C",R.color.md_blue_500),
//                        Pair("A",R.raw.dashboard_action),
//                        Pair("T",R.string.home_action_panel_title),
//                        Pair("D",R.string.home_action_panel_details),
//                    )
//                ),
//
//            )

            if(id==Function.Actions){
                view.lottieAnimationView.setBackgroundResource(R.color.md_blue_500)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_action)
                view.TTitle.text=context?.getString(R.string.home_action_panel_title)
                view.TDetails.text=context?.getString(R.string.home_action_panel_details)
//                view.root.setOnTouchListener(object:View.OnTouchListener{
//                    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                        when (p1?.action) {
//                            MotionEvent.ACTION_DOWN -> {                 // PRESSED
//                                view.lottieAnimationView.playAnimation()
//                                return true
//                            }
//                        }
//                        return true
//                    }
//
//                })
//                view.root.setOnClickListener{
//                    Log.i("NAVIGATION","Se iso click")
//                }
//                view.root.setOnTouchListener(object :View.OnTouchListener{
//                    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                        when(p1?.action){
//                            MotionEvent.ACTION_POINTER_DOWN->{
//                                Log.i("NAVIGATION","Se iso click")
//                            }
//                        }
//                        return true
//                    }
//                })



            }else if(id==Function.Tasks){
                view.lottieAnimationView.setBackgroundResource(R.color.md_deep_orange_400)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_task)
                view.TTitle.text=context?.getString(R.string.home_employed_panel_title)
                view.TDetails.text=context?.getString(R.string.home_employed_panel_details)
            }else if(id==Function.Catering){
                    view.lottieAnimationView.setBackgroundResource(R.color.md_blue_grey_200)
                    view.lottieAnimationView.setAnimation(R.raw.dashboard_catering)
                    view.TTitle.text=context?.getString(R.string.home_providers_panel_title)
                    view.TDetails.text=context?.getString(R.string.home_providers_panel_details)
            }else if(id==Function.Arrange){
                view.lottieAnimationView.setBackgroundResource(R.color.md_yellow_100)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_organize)
                view.TTitle.text=context?.getString(R.string.home_organize_panel_title)
                view.TDetails.text=context?.getString(R.string.home_organize_panel_details)
            }else if(id==Function.Logger){
                view.lottieAnimationView.setBackgroundResource(R.color.md_green_A400)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_register)
                view.TTitle.text=context?.getString(R.string.home_register_panel_title)
                view.TDetails.text=context?.getString(R.string.home_register_panel_details)
                view.go.setOnClickListener(object: View.OnClickListener {
                    override fun onClick(p0: View?) {
                        Log.i("NAVIGATION","Se iso click")
                        Navigation.findNavController(view.root).navigate(R.id.action_HomeFragmnet_to_agregateProducts3)
                        true
                    }
                })
            }else if(id==Function.sync){
                view.lottieAnimationView.setBackgroundResource(R.color.md_blue_500)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_sincronize)
                view.TTitle.text=context?.getString(R.string.home_sincronize_local_panel_title)
                view.TDetails.text=context?.getString(R.string.home_sincronize_local_panel_details)
            }else if(id==Function.Find){
                view.lottieAnimationView.setBackgroundResource(R.color.md_cyan_300)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_search)
                view.TTitle.text=context?.getString(R.string.home_search_panel_title)
                view.TDetails.text=context?.getString(R.string.home_search_panel_details)
            }else if(id==Function.Issue){
                view.lottieAnimationView.setBackgroundResource(R.color.md_green_900)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_emit)
                view.TTitle.text=context?.getString(R.string.home_emit_panel_title)
                view.TDetails.text=context?.getString(R.string.home_emit_panel_details)
            }else if(id==Function.Customers){
                view.lottieAnimationView.setBackgroundResource(R.color.md_indigo_300)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_client)
                view.TTitle.text=context?.getString(R.string.home_client_panel_title)
                view.TDetails.text=context?.getString(R.string.home_client_panel_details)
            }else if(id==Function.Receipts){
                view.lottieAnimationView.setBackgroundResource(R.color.md_blue_200)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_recibe)
                view.TTitle.text=context?.getString(R.string.home_receipts_panel_title)
                view.TDetails.text=context?.getString(R.string.home_receipts_panel_details)
            }else if(id==Function.History){
                view.lottieAnimationView.setBackgroundResource(R.color.md_deep_purple_300)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_history)
                view.TTitle.text=context?.getString(R.string.home_history_panel_title)
                view.TDetails.text=context?.getString(R.string.home_history_panel_details)
            }else if(id==Function.Configuration){
                view.lottieAnimationView.setBackgroundResource(R.color.md_grey_400)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_config)
                view.TTitle.text=context?.getString(R.string.home_config_panel_title)
                view.TDetails.text=context?.getString(R.string.home_config_panel_details)
            }else if(id==Function.Analytics){
                view.lottieAnimationView.setBackgroundResource(R.color.md_yellow_200)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_analytics)
                view.TTitle.text=context?.getString(R.string.home_analitic_panel_title)
                view.TDetails.text=context?.getString(R.string.home_analitic_panel_details)
            }else if(id==Function.Scan){
                view.lottieAnimationView.setBackgroundResource(R.color.md_teal_400)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_escaneo)
                view.TTitle.text=context?.getString(R.string.home_fast_qr_panel_title)
                view.TDetails.text=context?.getString(R.string.home_fast_qr_panel_details)
            }
            //News (31-05)
            else if(id==Function.Purchase){
                view.lottieAnimationView.setBackgroundResource(R.color.md_brown_500)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_compra)
                view.TTitle.text=context?.getString(R.string.home_user_shoping_local_qr_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_shoping_local_qr_panel_details)
            }else if(id==Function.Products){
                view.lottieAnimationView.setBackgroundResource(R.color.md_purple_A100)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_product)
                view.TTitle.text=context?.getString(R.string.home_user_products_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_products_panel_details)
            }else if(id==Function.Stores){
                view.lottieAnimationView.setBackgroundResource(R.color.md_deep_orange_100)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_tienda)
                view.TTitle.text=context?.getString(R.string.home_user_stores_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_stores_panel_details)
            }else if(id==Function.Link){
                view.lottieAnimationView.setBackgroundResource(R.color.md_blue_100)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_vincule)
                view.TTitle.text=context?.getString(R.string.home_user_vincule_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_vincule_panel_details)
            }else if(id==Function.Filters){
                view.lottieAnimationView.setBackgroundResource(R.color.md_pink_900)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_filter)
                view.TTitle.text=context?.getString(R.string.home_user_filter_store_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_filter_store_panel_details)
            }else if(id==Function.Search){
                view.lottieAnimationView.setBackgroundResource(R.color.home_client_panel)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_busqueda)
                view.TTitle.text=context?.getString(R.string.home_user_search_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_search_panel_details)
            }else if(id==Function.Interactions){
                view.lottieAnimationView.setBackgroundResource(R.color.md_red_A700)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_interaccion)
                view.TTitle.text=context?.getString(R.string.home_user_interact_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_interact_panel_details)
            }else if(id==Function.MyAccounts){
                view.lottieAnimationView.setBackgroundResource(R.color.md_indigo_800)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_cuenta)
                view.TTitle.text=context?.getString(R.string.home_user_accounts_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_accounts_panel_details)
            }else if(id==Function.Information){
                view.lottieAnimationView.setBackgroundResource(R.color.md_yellow_500)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_user_information)
                view.TTitle.text=context?.getString(R.string.home_user_shoping_qr_panel_title)
                view.TDetails.text=context?.getString(R.string.home_user_shoping_qr_panel_details)
            }

            else{
                view.lottieAnimationView.setBackgroundResource(R.color.md_falcon_400)
                view.lottieAnimationView.setAnimation(R.raw.dashboard_notfound)
                view.TTitle.text=item.name
                view.TDetails.text=context?.getString(R.string.home_error_panel_details)
            }
        }
    }
    fun addClient(cliente:GetRolesAdminQuery.Fuction){
//        Clients.add(cliente)
        notifyDataSetChanged()
    }
    fun addClientFrist(cliente: GetRolesAdminQuery.Fuction){
//        Clients.add(0,cliente)
        notifyItemRangeInserted(0,1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClient {
        val layoutInflater = LayoutInflater.from(parent.context)
//        var ItemView: View = layoutInflater
//            .inflate(R.layout.item_menu_home_list, parent,false)
        var ItemView = ItemMenuHomeListBinding.inflate(layoutInflater,parent,false)
        ItemView.root.setOnTouchListener(RV_ItemListener())
        return HolderClient(ItemView)
    }

    override fun onBindViewHolder(holder: HolderClient, position: Int) {
        holder.render(Clients[position])
    }

    override fun getItemCount(): Int {
        return Clients.size
    }


}