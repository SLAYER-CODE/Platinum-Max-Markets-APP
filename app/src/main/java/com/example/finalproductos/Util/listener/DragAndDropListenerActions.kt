package com.example.finalproductos.Util.listener

import android.content.ClipDescription
import android.content.Context
import android.view.DragEvent
import android.view.View

public class DragAndDropListenerActions:View.OnDragListener {
    lateinit var context: Context;

    constructor(
        context:Context
    ):super(){
        this.context=context;
    }

    override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
        var dragActions=p1?.action;
        return when (dragActions){
            DragEvent.ACTION_DRAG_STARTED->{
                var DesClip: ClipDescription = p1!!.clipDescription;
                true
            }
            DragEvent.ACTION_DRAG_ENTERED->{
                println("Entro")
                true
            }
            DragEvent.ACTION_DROP ->{
                println("Solto el Drop")
                true
            }
            else->{
                false
            }
        }

    }
}