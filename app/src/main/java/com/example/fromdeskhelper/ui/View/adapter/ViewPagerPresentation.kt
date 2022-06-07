package com.example.fromdeskhelper.ui.View.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.fromdeskhelper.ui.View.Presentations.UnoPresentacionFragment
import com.example.fromdeskhelper.ui.View.activity.PresenatationActivity
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.fragment_uno_precentacion.view.*


private var LOGG_ADAPTER_PRESENTATION = "VIEWPAGERADAPTERFRAGMENTES"

class ViewPagerPresentation :
    PagerAdapter {
    private var myViewLister: List<View>? = null

    //    private var context:Windowacty
//    private var acty:PresenatationActivity
    //    override fun getItemCount(): Int {
//        return colorList.size
//    }
    companion object {
        private const val ARG_OBJECT = "object"
    }

    //    override fun getItemCount(): Int=3
//
//    override fun createFragment(position: Int): Fragment {
//        Log.i(LOGG_ADAPTER_PRESENTATION,"Se inicio "+position.toString())
//        when(position){
//            0-> {return ShowProducts()}
//            1-> {return ShowProductsServer()}
//            2-> {return ShowProductsDatabase()}
//        }
//        return ShowProducts()
//    }
//constructor (myViewList: List<View>?,context:Window,acty:PresenatationActivity) {
    constructor (myViewList: List<View>?) {
        myViewLister = myViewList
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItemPosition(`object`: Any): Int {
//        switch (position) {
//            case 0: // Fragment # 0 - This will show FirstFragment
//            return FirstFragment.newInstance(0, "Page # 1");
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
//        super.destroyItem(container, position, `object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        container.addView(myViewLister?.get(position))
//        return super.instantiateItem(container, position)
//        myViewLister!![0]!!.blurView.visibility=View.GONE
//        return UnoPresentacionFragment.newInstance(0);
//        return
        return myViewLister?.get(position) as Any
    }

//    private fun blurbackground(view: BlurView) {
//        val radius = 5f
//        val decorView: View =  context!!.decorView
//        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
//        val windowBackground = decorView.background
//        view.setupWith(rootView)
//            .setFrameClearDrawable(windowBackground)
//            .setBlurAlgorithm(RenderScriptBlur(acty))
//            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
//            .setHasFixedTransformationMatrix(true)
//    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        view.textView.text="CARLITOS"

        return view == `object`
    }

}
