package com.example.fromdeskhelper.ui.View.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fromdeskhelper.ui.View.fragment.Root.Clients.ShowClientesFragment
import com.example.fromdeskhelper.ui.View.fragment.Root.Clients.ShowClientesPerToPerFragment
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsDatabase
import com.example.fromdeskhelper.ui.View.fragment.Root.ShowProductsServer
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts


var LOGG_ADAPTER_ROOT_CLIENT="VIEWPAGERADAPTERFRAGMENTES"
class ViewPagerRootClientAdapter(fa:FragmentActivity) :
    FragmentStateAdapter(fa) {

//    override fun getItemCount(): Int {
//        return colorList.size
//    }
    companion object{
        private const val ARG_OBJECT="object"
    }

    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        when(position){
            1-> {return ShowClientesPerToPerFragment()}
            0-> {return ShowClientesFragment()}
        }
        return ShowClientesFragment()
    }
}
class ViewMainRootClientAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}


internal class ViewPagerClientRootAdapter(manager: FragmentManager?) :
    FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}
