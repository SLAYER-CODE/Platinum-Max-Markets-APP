package com.example.fromdeskhelper.data.model.objects

import android.net.Uri
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import java.time.LocalDateTime

data class Form(
    val name: String,
    val precio: String,
    val precioNeto: String,
    val disconut: String,
    val stockcantidad: String,
    val stockC_attr: Int,


    val stockU: String,
    val stockU_attr: Int,

    val peso: String,
    val peso_attr: Int,

    val stock_attr: Int,
    val detalles: String,

    val  qr: String,

    val available_shipment: Boolean,
    val available_now: Boolean,
    val available_date: LocalDateTime?,

    val uuid: String,

    val categoria: MutableList<CategoriesInput>,
    val marca: MutableList<BrandsInput>,
    val mutableList: MutableList<Uri>,

    val Storage:Boolean,
    val SServer:Boolean,
    val SLocal: Boolean,
)