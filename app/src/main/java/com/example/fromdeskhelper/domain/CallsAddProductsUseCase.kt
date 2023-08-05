package com.example.fromdeskhelper.domain

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.apollographql.apollo3.api.DefaultUpload
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Upload
import com.apollographql.apollo3.exception.ApolloException
import com.example.fromdeskhelper.AgregateProductMutation
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.Network.ServicesGraph
import com.example.fromdeskhelper.type.*
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class CallsAddProductsUseCase @Inject constructor(
    private val cliente: ServicesGraph?,
    private val context: Context,

    ) {
    private var LOG_INFO = "USECASEEXTRATADDPRODUCT";
    suspend fun ReturnCategories(): CategoriasQuery.Data {
        try{
            return cliente?.CategoriesGetService()?.data?: CategoriasQuery.Data(emptyList())
        }
        catch(ex:ApolloException){
            return CategoriasQuery.Data(emptyList())
        }
    }

//    public val brands_products: List<BrandsInput>,
//    public val category_products: List<CategoriesInput>,
//    public val description: Optional<String?> = Optional.Absent,
//    public val old_price: Double,
//    public val price_cantidad: Double,
//    public val price_unity: Double,
//    public val product_name: String,
//    public val qr: Optional<Double?> = Optional.Absent,
//    public val quantity_cantidad: Optional<Double?> = Optional.Absent,
//    public val quantity_unity: Optional<Double?> = Optional.Absent,
//    public val update_product: Optional<Any?> = Optional.Absent,

    suspend fun AgregateProduct(
        name: String,
        precio: String,
        precioNeto: String,
        disconut: String,
        stockcantidad: String,
        stockC_attr: Int,


        stockU: String,
        stockU_attr: Int,

        peso: String,
        peso_attr: Int,

        stock_attr: Int,
        detalles: String,

        qr: String,

        available_shipment: Boolean,
        available_now: Boolean,
        available_date: LocalDateTime?,

        uuid: String,

        categoria: MutableList<CategoriesInput>,
        marca: MutableList<BrandsInput>,
        mutableList: MutableList<Uri>,

        ): AgregateProductMutation.Data? {

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
        val _available_date:Date? = if(available_date==null) null else Date.from(available_date.atZone(
            ZoneId.systemDefault()).toInstant())

        val _uuid:String=uuid

        val _detalles: String? = if (detalles=="") null else detalles

        val imagenes= mutableListOf<ImageProductInput>()
        val uploadImagenes = mutableListOf<Upload>()

        for (x in mutableList){
            var item=x.toString().split("/")
            imagenes.add(ImageProductInput(
                image_description=Optional.presentIfNotNull<String>(""),
                image_name =  UUID.randomUUID().toString()+".png" ))
            val imagen = MediaStore.Images.Media.getBitmap(context.contentResolver, x)
////                            println(path.path)
            val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(x.path!!, imagen)

            val flujo = ByteArrayOutputStream()
            orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
            val imageInByte: ByteArray = flujo.toByteArray()
            val upload = DefaultUpload.Builder().content(imageInByte).build()
            uploadImagenes.add(upload)
        }


        Log.i("SECARGOOOOOIMAGEN",uploadImagenes.toString())


        var sendProduct = ProductsInput(
            Optional.presentIfNotNull(_available_date),
            Optional.presentIfNotNull(_available_now),
            Optional.presentIfNotNull(_available_shipment),
            Optional.presentIfNotNull(marca.toList()),
            Optional.presentIfNotNull(categoria.toList()),
            Optional.presentIfNotNull(_detalles),
            Optional.presentIfNotNull(_disconut),
            Optional.presentIfNotNull(imagenes.toList()),
            Optional.presentIfNotNull(_peso),
            Optional.presentIfNotNull(_peso_attr),
            _precio,
            Optional.presentIfNotNull(_precioNeto),
            _name,
            Optional.presentIfNotNull(_qr),
            Optional.presentIfNotNull(_stock?.toDouble()),
            Optional.presentIfNotNull(_stockU?.toDouble()),
            Optional.presentIfNotNull(_stockC_attr?.toDouble()),
            Optional.presentIfNotNull(_stockU_attr?.toDouble()),
            Optional.presentIfNotNull(_stock_attr),
            _uuid,
            Optional.presentIfNotNull(Date()),
        )


        var JsonProduct: AgregateProductMutation.Data? = cliente?.SendProduct(sendProduct,uploadImagenes)?.data
        return JsonProduct
    }

}