package com.example.finalproductos.model.base

import Data.ImagenesNew
import Data.InventarioProducts
import Data.Producto
import Data.listInventarioProductos
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*


@Dao
interface ProductoData {
    @Query("SELECT * FROM productos")
    fun getAll():LiveData<List<Producto>>

    @Query("SELECT * FROM productos  ORDER BY `update` DESC")
    fun getAllTime():LiveData<List<Producto>>

    @Query("SELECT * FROM productos WHERE uid =:id")
    fun get(id:Int):LiveData<Producto>

    @Query("SELECT `update` FROM productos WHERE uid=:id")
    fun getTime(id:Int):LiveData<Date>


    @Query("SELECT COUNT(*) FROM productos")
    fun getCount():LiveData<Int>


    @Transaction
    @Query("SELECT * FROM productos")
    suspend fun getAllInventarioA(): List<InventarioProducts>

    @Transaction
    @Query("SELECT * FROM productos WHERE uid=:id")
    fun getInventarioId(id:Int):LiveData<InventarioProducts>

    @Query("SELECT * FROM productos  ORDER BY `update` DESC LIMIT :limit OFFSET :offset ")
    fun getAllInventarioTime(limit:Int,offset:Int):LiveData<List<InventarioProducts>>

    @Transaction
    @Query("SELECT productos.uid,productos.nombre,productos.precioC,productos.precioU,imagenes.imageBit   FROM productos LEFT JOIN imagenes  on imagenes.id_producto = productos.uid group by productos.uid ORDER BY `update` DESC LIMIT :limit OFFSET :offset  ")
    fun getByInventarioProductos(limit:Int,offset:Int): LiveData<List<listInventarioProductos>>

    @Transaction
    @Query("SELECT * FROM imagenes WHERE id_producto = :id")
    fun getByImagenesId(id: Int): List<ImagenesNew>

    @Insert
    fun insertAll(producto: Producto):Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllImages(vararg imagenNew: ImagenesNew)


    @Update
    fun update(producto: Producto)
    @Delete
    fun delete(producto: Producto)
    @Delete
    fun deleteImagen(NewImage: ImagenesNew)
}