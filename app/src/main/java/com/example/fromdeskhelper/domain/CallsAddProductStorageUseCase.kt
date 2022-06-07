package com.example.fromdeskhelper.domain

import Data.ClientList
import Data.ImagenesNew
import Data.Producto
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.model.Controller.SavedProductController
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class CallsAddProductStorageUseCase @Inject constructor(private val ProductController: SavedProductController) {


    suspend fun SaveProductLocal(
        name: String,
        precio: String,
        precioU: String,
        marca: String,
        detalles: String,
        categoria: String,
        stockcantidad: String,
        stockunidad: String,
        qr: String,
    ): Int {
        val name = name
        val precio: Double? = precio.toDoubleOrNull()
        val precioU: Double = if (precioU.isEmpty()) 0.0 else precioU.toDouble()
        val marca: String = marca
        val detalles: String = detalles
        val categoria: String = categoria
        val stock: Int = if (stockcantidad.isEmpty()) 1 else stockcantidad.toInt()
        val stockU: Int = if (stockunidad.isEmpty()) 0 else stockunidad.toInt()
        val qr: String = qr

//            val image:List<ImagenesNew> = listOf(ImagenesNew(_image,Date()))
        val timeUpdate: Date = Date()
        var producto: Producto
        if (name.isEmpty() || precio == null) {
            return -1
        } else {
            var pwd: Long
            producto = Producto(
                timeUpdate,
                name,
                precio,
                precioU,
                marca,
                detalles,
                categoria,
                stock,
                stockU,
                qr
            )

            return ProductController.saveProduct(producto)
        }
    }
    suspend fun SaveImageProduct(
        mutableList: MutableList<Uri>,user:Int,baseActivity:MainActivity
    ){
        if(mutableList.isNotEmpty()) {
            for (path:Uri in mutableList){

                val imagen = MediaStore.Images.Media.getBitmap(baseActivity.contentResolver, path)
////                            println(path.path)
                val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(path.path!!, imagen)

                val flujo = ByteArrayOutputStream()
                orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
                val imageInByte: ByteArray = flujo.toByteArray()
                val imagenSave = ImagenesNew(Date(), user.toInt(), imageInByte)
//                CoroutineScope(Dispatchers.IO).launch {
//                    daoNew.productosData().insertAllImages(imagenSave)
//                                        imagen.recycle()
//                }

                ProductController.saveImage(imagenSave)
                imagen.recycle()
            }
        }
    }


}
