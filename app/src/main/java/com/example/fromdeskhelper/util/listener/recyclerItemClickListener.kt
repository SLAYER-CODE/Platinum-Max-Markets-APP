package com.example.fromdeskhelper.util.listener

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts


class recyclerItemClickListener(
    private val mRecycler: RecyclerView,
    private val clickListener: ((position: Int, view: View) -> Unit)? = null,
    private val longClickListener: ((position: Int, view: View) -> Unit)? = null
) : RecyclerView.OnChildAttachStateChangeListener {

    override fun onChildViewDetachedFromWindow(view: View) {
        view.setOnClickListener(null)
        view.setOnLongClickListener(null)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        view.setOnClickListener { v -> setOnChildAttachedToWindow(v) }
    }

    private fun setOnChildAttachedToWindow(v: View?) {
        if (v != null) {
            val position = mRecycler.getChildLayoutPosition(v)
            if (position >= 0) {
                clickListener?.invoke(position, v)
                longClickListener?.invoke(position, v)
            }
        }
    }
}

class RecyclerViewItemClickListener(
    context: Context?,
    recyclerView: RecyclerView,
    clickListener: ShowProducts.ClickListener?
) :
    OnItemTouchListener {
    //GestureDetector to detect touch event.
    private val gestureDetector: GestureDetector
    private val clickListener: ShowProducts.ClickListener?
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        //On Touch event
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildLayoutPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    init {
        this.clickListener = clickListener
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                //Find child on x and y position relative to screen
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child))
                }
            }
        })
    }
}

//
//class recyclerItemClickListener(
//    private val mRecycler: RecyclerView,
//    private val clickListener: ((position: Int, view: View) -> Unit)? = null,
//    private val longClickListener: ((position: Int, view: View) -> Unit)? = null
//) : RecyclerView.OnChildAttachStateChangeListener {
//
//    override fun onChildViewDetachedFromWindow(view: View) {
//        view.setOnClickListener(null)
//        view.setOnLongClickListener(null)
//    }
//
//    override fun onChildViewAttachedToWindow(view: View) {
//        view.setOnClickListener { v -> setOnChildAttachedToWindow(v) }
//    }
//
//    private fun setOnChildAttachedToWindow(v: View?) {
//        if (v != null) {
//            val position = mRecycler.getChildLayoutPosition(v)
//            if (position >= 0) {
//                clickListener?.invoke(position, v)
//                longClickListener?.invoke(position, v)
//            }
//        }
//    }
//}
