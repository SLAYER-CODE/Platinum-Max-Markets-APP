package com.example.fromdeskhelper.data.model.Helper

import com.example.fromdeskhelper.ui.View.adapter.ImageAdapter
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.data.model.base.CallBackItemTouch
import kotlinx.android.synthetic.main.image_new_new.view.*


class MyItemTouchHelperCallback : ItemTouchHelper.Callback{
    lateinit var callbackItemTouch: CallBackItemTouch;
    var mFrom = -1
    var mTo = -1
    private var removeItem = false
    private var resPosX:Float = -1f;
    private var resPosY:Float = -1f;
    constructor(callbackItemTouch: CallBackItemTouch) : super() {
        this.callbackItemTouch = callbackItemTouch

    }

    override fun isLongPressDragEnabled(): Boolean {
        return super.isLongPressDragEnabled()
}

    override fun isItemViewSwipeEnabled(): Boolean {
        return super.isItemViewSwipeEnabled()
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        val dagFlags:Int=ItemTouchHelper.START or ItemTouchHelper.END or ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val dagFlags:Int=ItemTouchHelper.START or ItemTouchHelper.END
        val swipeFlags:Int=ItemTouchHelper.UP or ItemTouchHelper.DOWN;
        return makeMovementFlags(dagFlags,swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //Para que el otro cuadro se mueva si intercambia la pocicion.
//        val fromPosition: Int = viewHolder.absoluteAdapterPosition
//        val toPosition: Int = target.absoluteAdapterPosition
//
//        if(dragFrom == -1) {
//            dragFrom =  fromPosition;
//        }
//        dragTo = toPosition;
//

////        adapter.onItemMove(fromPosition, toPosition);

//        if (mFrom == null)
//            mFrom = source.getAdapterPosition();
//        mTo = target.getAdapterPosition();

        // Notify the adapter of the move
//        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());

//        if (viewHolder.itemViewType != target.itemViewType) {
//            return false;
//        }

        // remember FIRST from position
//        if (mFrom == null)
//            mFrom = viewHolder.absoluteAdapterPosition;
//        mTo = target.absoluteAdapterPosition

        callbackItemTouch.itemTouchMode(viewHolder.absoluteAdapterPosition,target.absoluteAdapterPosition)
        return true;
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        callbackItemTouch.onSwiped(viewHolder,viewHolder.absoluteAdapterPosition)
    }


    //Implementado los metodos hasta ahora

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,

        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if(actionState==ItemTouchHelper.ACTION_STATE_DRAG) {
//            val foregroudView:View=((ImageAdapter.ImageHolder(viewHolder.itemView))).viewF
//            getDefaultUIUtil().onDrawOver(c,recyclerView,foregroudView,dX,dY,actionState,isCurrentlyActive);
//            println(dX.toString()+" "+dY.toString())
            resPosX=viewHolder.itemView.viewParent.x
            resPosY=viewHolder.itemView.viewParent.y
            callbackItemTouch.CalculateArea(viewHolder.itemView.viewParent.x,viewHolder.itemView.viewParent.y)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        }else {
            val foregroudView: View = ((ImageAdapter.ImageHolder(viewHolder.itemView))).viewB
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                foregroudView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            );
        }
    }


    override fun getAnimationDuration(
        recyclerView: RecyclerView,
        animationType: Int,
        animateDx: Float,
        animateDy: Float
    ): Long {
        println("SOCCC")
        if (removeItem) return 0
        return 0

        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }


    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState!=ItemTouchHelper.ACTION_STATE_DRAG){
            val foregroudView:View=((ImageAdapter.ImageHolder(viewHolder.itemView))).viewF
            getDefaultUIUtil().onDraw(c,recyclerView,foregroudView,dX,dY,actionState,isCurrentlyActive);
        }
//        else{
//            val ViewParent:View=((ImageAdapter.ImageHolder(viewHolder.itemView))).viewParent
//            if(dY>0){
//                ViewParent.scaleX=-0.01f;
//                ViewParent.scaleY=-0.01f;
//            }
//        }



//        else{
//            if(dY!=upanddow) {
//                val up = 10000
//                val dow = 5
//                val foregroudView: View =
//                    ((ImageAdapter.ImageHolder(viewHolder.itemView))).viewParent
//                if(dY>upanddow) {
//                    foregroudView.scaleY += (dY / up)
//                    foregroudView.scaleX += (dY / up)
//                }else{
//                    foregroudView.scaleY -= (dY / up)
//                    foregroudView.scaleX -= (dY / up)                }
//            }
//        }
//        upanddow=dY;



    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        println("Funciona")
//        viewHolder.itemView.alpha = ALPHA_FULL
        callbackItemTouch.prepareViews(false,viewHolder);
//
//        if (viewHolder is ItemTouchHelperViewHolder) {
//            val itemViewHolder: ItemTouchHelperViewHolder = viewHolder as ItemTouchHelperViewHolder
//            itemViewHolder.onItemClear()
//        }

        val foregroudView:View=((ImageAdapter.ImageHolder(viewHolder.itemView))).viewF
        foregroudView.setBackgroundColor(ContextCompat.getColor(ImageAdapter.ImageHolder(viewHolder.itemView).viewF.context,android.R.color.black));
        getDefaultUIUtil().clearView(foregroudView)
//        return
        super.clearView(recyclerView, viewHolder)
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return super.canDropOver(recyclerView, current, target)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        removeItem = false
        callbackItemTouch.prepareViews(true,viewHolder);

        if(viewHolder!=null){
            val foregroudView:View=((ImageAdapter.ImageHolder(viewHolder.itemView))).viewF
            if(actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                foregroudView.setBackgroundColor(Color.GREEN);
            }
            getDefaultUIUtil().onSelected(foregroudView)
        }else{

            if(callbackItemTouch.isItemSelected(resPosX,resPosY)==1){
                removeItem=true

            }

        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

}