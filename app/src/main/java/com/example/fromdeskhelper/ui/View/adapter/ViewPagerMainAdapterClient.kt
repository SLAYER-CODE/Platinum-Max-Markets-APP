package com.example.fromdeskhelper.ui.View.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments.ShowAsistentP2PFragment
import com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments.ShowProductsP2PFragment
import com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments.ShowProductsServerFragment
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsDatabase
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsServer
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts

private val LOGG_ADAPTER_CLIENT="VIEWPAGERADAPTERFRAGMENTES"
class ViewPagerMainAdapterClient(fa:FragmentActivity) :
    FragmentStateAdapter(fa) {
    companion object{
        private const val ARG_OBJECT="object"
    }

    override fun getItemCount(): Int=3

    override fun createFragment(position: Int): Fragment {
        Log.i(LOGG_ADAPTER_CLIENT,"Se inicio "+position.toString())
        when(position){
            0-> {return ShowProductsP2PFragment()}
            1-> {return ShowProductsServerFragment()}
            2-> {return ShowAsistentP2PFragment()}
        }
        return ShowProducts()
    }
}
