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
        view.setOnLongClickListener { v-> setOnChildAttachedToWindowLong(v)}
    }

    private fun setOnChildAttachedToWindow(v: View?) {
        if (v != null) {
            val position = mRecycler.getChildLayoutPosition(v)
            if (position >= 0) {
                clickListener?.invoke(position, v)
            }
        }
    }
    private fun setOnChildAttachedToWindowLong(v: View?): Boolean {
        if (v != null) {
            val position = mRecycler.getChildLayoutPosition(v)
            if (position >= 0) {
                longClickListener?.invoke(position, v)
            }
        }
        return true
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
//Segundo


class RecyclerItemClickListenr(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {

    private val mGestureDetector: GestureDetector

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }

    init {

        mGestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(
                            childView,
                            recyclerView.getChildAdapterPosition(childView)
                        )
                    }
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
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
