package com.example.fromdeskhelper.ui.animation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.NestedScrollType
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClientNavigationBehavior (context: Context, attributeSet: AttributeSet)
    : CoordinatorLayout.Behavior<View>(context, attributeSet){

    private var height = 0f
    private var join = 0f
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        height = child.scaleY
        join=height
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View, directTargetChild: View, target: View,
        axes: Int, type: Int
    ): Boolean {

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        @NestedScrollType type: Int
    ) {
        var consumed:Float = dyConsumed/100f
        if (consumed > 0 && dyUnconsumed==0 ) {
            if((join-consumed)>0){
                join-=consumed
            }else {
                join=0f
            }
            scaleDown(child,(join))
        } else if (consumed < 0  && dyUnconsumed==0 ) {
            if((join-consumed)<1){
                join -= consumed

            }else {
                join=1f
            }
            scaleDown(child,join)
        }
    }

    private fun scaleDown(child: View,dyConsumed: Float) {
        Log.i("CONSUMEDDD",dyConsumed.toString())

        child.scaleX=dyConsumed
        child.scaleY=dyConsumed
    }

}