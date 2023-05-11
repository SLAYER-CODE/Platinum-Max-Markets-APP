package com.example.fromdeskhelper.ui.animation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.NestedScrollType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class FloatingHideBehavior (context: Context, attributeSet: AttributeSet)
    : CoordinatorLayout.Behavior<View>(context, attributeSet){
    private var height = 0

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View, directTargetChild: View, target: View,
        axes: Int, type: Int
    ): Boolean {

        return axes == ViewCompat.SCROLL_INDICATOR_BOTTOM
    }


    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        @NestedScrollType type: Int
    ) {
        if (dyConsumed > 0 && dyUnconsumed==0 ) {

            if((height-dyConsumed)>-200){
                height-=dyConsumed
            }else {
                height=-200
            }
            slideLeft(child,height)
        } else if (dyConsumed < 0  && dyUnconsumed==0 ) {
            if((height-dyConsumed)<0){
                height -= dyConsumed

            }else {
                height=0

            }
            slideRigth(child,height)
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return true
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val translationY = getFabTranslationYForSnackbar(parent, child)
        val percentComplete: Float = -translationY
        val scaleFactor = 1 - percentComplete
        if(scaleFactor > 0f) {
            child.clearAnimation()
            child.animate().scaleX(scaleFactor).duration = 100
            child.animate().scaleY(scaleFactor).duration = 100
        }else{
            child.scaleX = 0f
            child.scaleY = 0f
        }
        return false
    }

    private fun slideLeft(child: View,dxConsumed:Int) {
        child.translationX=(dxConsumed.toFloat())
        child.translationY=-(dxConsumed.toFloat())
    }

    private fun slideRigth(child: View,pwd:Int) {
        child.translationX=(pwd.toFloat())
        child.translationY=-(pwd.toFloat())

    }

    private fun getFabTranslationYForSnackbar(
        parent: CoordinatorLayout,
        fab: View
    ): Float {
        var minOffset = 0f
        val dependencies = parent.getDependencies(fab)
        var i = 0
        val z = dependencies.size
        while (i < z) {
            val view = dependencies[i]
            if (view is SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(
                    minOffset,
                    ViewCompat.getTranslationY(view) - view.getHeight()
                )
            }
            i++
        }
        return minOffset
    }


}