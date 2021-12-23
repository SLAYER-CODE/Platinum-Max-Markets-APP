package com.example.finalproductos.model.objects

import java.util.jar.Manifest

object Constrains {
    const val TAG = "camerax"
    const val FILE_NAME_FORMAT="yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS=123
    val REQUIERED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
}