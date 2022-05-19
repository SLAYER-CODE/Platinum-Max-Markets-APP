package com.example.fromdeskhelper.ui.View.adapter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.StatefulAdapter
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsDatabase
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsServer
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts

var LOGG_ADAPTER="VIEWPAGERADAPTERFRAGMENTES"
class ViewPagerMainAdapter(fa:FragmentActivity) :
    FragmentStateAdapter(fa) {

//    override fun getItemCount(): Int {
//        return colorList.size
//    }
    companion object{
        private const val ARG_OBJECT="object"
    }

    override fun getItemCount(): Int=3

    override fun createFragment(position: Int): Fragment {
        Log.i(LOGG_ADAPTER,"Se inicio "+position.toString())
        when(position){
            0-> {return ShowProducts()}
            1-> {return ShowProductsServer()}
            2-> {return ShowProductsDatabase()}
        }
        return ShowProducts()
    }
}
