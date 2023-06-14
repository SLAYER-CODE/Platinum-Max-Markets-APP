package com.example.fromdeskhelper.data.model.objects

object Upload {
    const val STORAGE=0
    const val SERVER=1
    const val SERVER_LOCAL=2
    const val SERVER_LOCAL_DATABASE = 3
}

data class ResponseAdd(
    val trafic: Int,
    val state: Boolean
)