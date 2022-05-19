package com.example.fromdeskhelper.util.listener

import android.content.ClipData
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.Toast
import java.lang.String
import kotlin.Boolean
import kotlin.math.absoluteValue


// Touched
var LOG_TOUCH="TOUCHED"
var LOG_TOUCHSECOND="TOUCHEDSECOND"
var LOG_TOUCHTREE="TOUCHEDSECOND"
class TouchDropListenerAction : OnTouchListener {
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            MotionEvent.ACTION_MOVE -> {
                Log.i(LOG_TOUCH,"Se Movio")
                true
            }
            MotionEvent.ACTION_HOVER_MOVE->{
                Log.i(LOG_TOUCH,"Se Movio Hover")
                true
            }
            MotionEvent.ACTION_DOWN -> {
                Log.i(LOG_TOUCH,"Se Apreto El boton")

                var Viewed = String.valueOf(view.getId())
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)

                // Drag Function When Item Touched
                view.startDrag(data, shadowBuilder, view, 0)
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.i(LOG_TOUCH,"Se subio el Mouse")
                true
            }
            else -> true
        }
    }
}

class TouchlistenerMove(var moveView:View,var Save:(x:Float,y:Float)->(Unit) ) : OnTouchListener {
    var dX = 0f
    var dY = 0f
    var lastAction = 0
    var floatingLayout: LinearLayout? = null
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = moveView.x - event.rawX
                dY = moveView.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
            }
            MotionEvent.ACTION_MOVE -> {
                moveView.y = event.rawY + dY
                moveView.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> {
                Save(moveView.x,moveView.y)
                Log.i(LOG_TOUCHTREE,"Se guardo [MOVE]")
                true
            }
        }
        return true
    }
}

class TouchListenerResize(var moveView:View,var Save:(x:Float,y:Float)->(Unit) ) : OnTouchListener {
    var dX = 0f
    var dY = 0f
    var lastAction = 0
    var floatingLayout: LinearLayout? = null
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = moveView.x - event.rawX
                dY = moveView.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
            }
            MotionEvent.ACTION_MOVE -> {
//
//                Log.i(LOG_TOUCHTREE,moveView.y.toString())
//                Log.i(LOG_TOUCHTREE,moveView.scaleY.toString())
//                Log.i(LOG_TOUCHTREE,moveView.height.toString())
//                Log.i(LOG_TOUCHTREE,event.y.toString())
//                Log.i(LOG_TOUCHTREE,event.rawY.toString())
                val res=(((moveView.y+(moveView.height/2))-event.rawY)*0.00205).absoluteValue.toFloat()
                val resX=(((moveView.x+(moveView.width/2))-event.rawX)*0.00205).absoluteValue.toFloat()
                moveView.scaleY =  if (res>0.4) res else 0.4f
                moveView.scaleX = if(resX>0.4) resX else 0.4f
                lastAction = MotionEvent.ACTION_MOVE

            }
            MotionEvent.ACTION_UP -> {
                Save(moveView.scaleX,moveView.scaleY)
                Log.i(LOG_TOUCHTREE,"Se guardo [Rezie]")
                true
            }
        }
        return true
    }
}
