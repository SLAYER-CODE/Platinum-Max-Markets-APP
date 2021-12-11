package CallBacks

import android.view.MotionEvent

import android.content.ClipData
import android.view.View

import android.view.View.OnTouchListener
import java.lang.String



// Touched
class TouchDropListenerAction : OnTouchListener {
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            MotionEvent.ACTION_MOVE -> true
            MotionEvent.ACTION_DOWN -> {
                var Viewed = String.valueOf(view.getId())
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)

                // Drag Function When Item Touched
                view.startDrag(data, shadowBuilder, view, 0)
                println("Funcionapsspsppspp")
                true
            }
            MotionEvent.ACTION_UP -> true
            else -> true
        }
    }

}