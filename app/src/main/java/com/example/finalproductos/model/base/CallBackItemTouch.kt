package com.example.finalproductos.model.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface CallBackItemTouch {
    fun itemTouchMode(oldPosition:Int,newPosition:Int)
    fun onSwiped(viewHolder :RecyclerView.ViewHolder,position:Int)
    fun onDrop(eliminated:Boolean,position:Int);
    fun prepareViews(views:Boolean,viewholds:RecyclerView.ViewHolder?);
    fun CalculateArea(posX:Float,posY:Float);
    fun isItemSelected(posX:Float,posY: Float):Int
}