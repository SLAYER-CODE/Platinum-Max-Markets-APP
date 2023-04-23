package com.example.fromdeskhelper.data.model

import java.util.*

data class Productos(
    val id:Int,
    val precio:Int,
    val nombre:String,
    val descripccion:String,
    val imagen:ByteArray,
    val imagenSeconds:String,
)

data class User(
    val id:Int,
    val token:String,
    val uenter:Date,
    val tarjeta:String,
    val typeUser:Int,
    val dni:String,
)

data class CarritoUserProductos(
    val id:Int,
    val fecha: Date,
    val user:User,
    val productos: Productos,
)

data class Carrito(
    val id:Int,
    val idProducto : Int,
    val fecha :Date,
    val usuario : Int
)


