package com.example.fromdeskhelper.data.model.Controller

import android.content.Intent
import androidx.fragment.app.Fragment

object imagenController {
    fun selectFromPothoGallery(activity:Fragment?,code :Int){
        val intent =Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        activity?.startActivityForResult(Intent.createChooser(intent,"Seleciones sus productos"),code)
    }
}