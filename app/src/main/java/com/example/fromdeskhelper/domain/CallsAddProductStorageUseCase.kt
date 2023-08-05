package com.example.fromdeskhelper.domain

import Data.Brands
import Data.Categories
import Data.ImagenesProduct
import Data.Producto
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.model.Controller.SavedProductController
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class CallsAddProductStorageUseCase @Inject constructor(
    private val ProductController: SavedProductController,
    private val context: Context,

    ) {

    suspend fun SaveProductAllLocal(
        name: String,
        precio: String,
        precioNeto: String,
        disconut: String,
        stockcantidad: String,
        stockC_attr:Int,


        stockU: String,
        stockU_attr:Int,

        peso:String,
        peso_attr:Int,

        stock_attr:Int,
        detalles: String,

        qr: String,

        available_shipment:Boolean,
        available_now:Boolean,
        available_date: LocalDateTime?,

        uuid:String,

        categoria: MutableList<CategoriesInput>,
        marca: MutableList<BrandsInput>,
        mutableList: MutableList<Uri>,
    ): Int {
        var images = mutableListOf<ImagenesProduct>()
//        corrigiendo imagenes
        if(mutableList.isNotEmpty()) {
            for (path:Uri in mutableList){

                val imagen = MediaStore.Images.Media.getBitmap(context.contentResolver, path)
////                            println(path.path)
                val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(path.path!!, imagen)

                val flujo = ByteArrayOutputStream()
                orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 100, flujo)
                val imageInByte: ByteArray = flujo.toByteArray()
                val imagenSave = ImagenesProduct(Date(), 0, imageInByte)
                images.add(imagenSave)
            }
        }
        //Factorizando los demas datos
        val _name:String = name
        val _precio: Double = precio.toDouble()
        val _precioNeto: Double? = if (precioNeto.isEmpty()) null else precioNeto.toDouble()
        val _disconut: Double? = if (disconut.isEmpty()) null else disconut.toDouble()

        //Cantidad Unidades o paquete , caja ,
        val _stock: Int? = if (stockcantidad.isEmpty()) null else stockcantidad.toInt()
        val _stockC_attr: Int? = if (stockcantidad.isEmpty()) null else stockC_attr

        //Contenido , unida, litro
        val _stockU: Int? = if (stockU.isEmpty()) null else stockU.toInt()
        val _stockU_attr: Int? = if (stockU.isEmpty()) null else stockU_attr

        val _peso:Double? = if(peso.isEmpty()) null else peso.toDouble()
        val _peso_attr:Int? = if(peso.isEmpty()) null else peso_attr

        val _stock_attr:Int? = if(stockcantidad.isEmpty()) null else stock_attr

        val _qr: String? = if (qr=="") null else qr


        val _available_shipment:Boolean = available_shipment
        val _available_now:Boolean = available_now
        val _available_date:Date? = if(available_date==null) null else Date.from(available_date.atZone(ZoneId.systemDefault()).toInstant())

        val _uuid:String=uuid

        val _detalles: String? = if (detalles=="") null else detalles

//        val _marca: MutableList<BrandsInput> = marca
//        val _categoria: MutableList<CategoriesInput> = categoria

//            val image:List<ImagenesNew> = listOf(ImagenesNew(_image,Date()))
        val timeUpdate = Date()
        var producto: Producto
        if (name.isEmpty() || precio == null) {
            return -1
        } else {
            producto = Producto(
                timeUpdate,
                _name,
                _precio,
                _precioNeto,
                _disconut,
                _stock,
                _stockC_attr,
                _stockU,
                _stockU_attr,
                _peso,
                _peso_attr,
                _stock_attr,
                _detalles,
                _qr,
                _available_shipment,
                _available_now,
                _available_date,
                _uuid,
            )
            producto.ImageList=images
            var cateogoryLocalModel:MutableList<Categories> = mutableListOf()
            for (i in categoria){
                cateogoryLocalModel.add(Categories(i.category_name,0))
            }
            producto.CategorieList = cateogoryLocalModel

            var brandLocalModel:MutableList<Brands> = mutableListOf()
            for (i in marca){
                brandLocalModel.add(Brands(i.brand_name,0))
            }
            producto.BrandList = brandLocalModel
            return ProductController.saveProductALl(producto)
        }
    }

//    suspend fun SaveProductLocal(
//        name: String,
//        precio: String,
//        precioU: String,
//        marca: MutableList<BrandsInput>,
//        detalles: String,
//        categoria: MutableList<CategoriesInput>,
//        stockcantidad: String,
//        stockunidad: String,
//        qr: String,
//    ): Int {
////        Corigiendo imagenes
//
//        val name:String = name
//        val precio: Double? = precio.toDoubleOrNull()
//        val precioU: Double = if (precioU.isEmpty()) 0.0 else precioU.toDouble()
//        val marca: MutableList<BrandsInput> = marca
//        val detalles: String = detalles
//        val categoria: MutableList<CategoriesInput> = categoria
//        val stock: Int = if (stockcantidad.isEmpty()) 1 else stockcantidad.toInt()
//        val stockU: Int = if (stockunidad.isEmpty()) 0 else stockunidad.toInt()
//        val qr: String = qr
//
////            val image:List<ImagenesNew> = listOf(ImagenesNew(_image,Date()))
//        val timeUpdate: Date = Date()
//        var producto: Producto
//        if (name.isEmpty() || precio == null) {
//            return -1
//        } else {
//            var pwd: Long
//            producto = Producto(
//                timeUpdate,
//                _name,
//                _precio,
//                _precioNeto,
//                _disconut,
//                _stock,
//                _stockC_attr,
//                _stockU,
//                _stockU_attr,
//                _peso,
//                _peso_attr,
//                _stock_attr,
//                _detalles,
//                _qr,
//                _available_shipment,
//                _available_now,
//                _available_date,
//                _uuid,
//            )
////            producto.ImageList = listOf()
//            return ProductController.saveProduct(producto)
//        }
//    }
//    suspend fun SaveImageProduct(
//        mutableList: MutableList<Uri>,user:Int,
//    ){
//        if(mutableList.isNotEmpty()) {
//            for (path:Uri in mutableList){
//
//                val imagen = MediaStore.Images.Media.getBitmap(context.contentResolver, path)
//////                            println(path.path)
//                val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(path.path!!, imagen)
//
//                val flujo = ByteArrayOutputStream()
//                orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
//                val imageInByte: ByteArray = flujo.toByteArray()
//                val imagenSave = ImagenesProduct(Date(), user.toInt(), imageInByte)
////                CoroutineScope(Dispatchers.IO).launch {
////                    daoNew.productosData().insertAllImages(imagenSave)
////                                        imagen.recycle()
////                }
//
//                ProductController.saveImage(imagenSave)
//                imagen.recycle()
//            }
//        }
//    }


}
