package com.example.fromdeskhelper.data.model.objects

object Constrains {
    const val TAG = "camerax"
    const val FILE_NAME_FORMAT="yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS=123
    val REQUIERED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
}

object ZoomCamer {
    const val SMALL=0
    const val NORMAL=100
    const val large=200
}