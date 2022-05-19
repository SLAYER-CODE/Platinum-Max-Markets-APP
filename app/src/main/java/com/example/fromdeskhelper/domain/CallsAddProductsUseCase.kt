package com.example.fromdeskhelper.domain

import Data.ImagenesNew
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.DefaultUpload
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Upload
import com.example.fromdeskhelper.AgregateProductMutation
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ComprobateUserQuery
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.Network.ServicesGraph
import com.example.fromdeskhelper.type.*
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class CallsAddProductsUseCase @Inject constructor(
    private val cliente: ServicesGraph
) {
    private var LOG_INFO = "USECASEEXTRATADDPRODUCT";
    suspend fun ReturnCategories(): CategoriasQuery.Data {
        var JsonPrin: CategoriasQuery.Data? = cliente.CategoriesGetService().data
        return JsonPrin ?: CategoriasQuery.Data(emptyList())
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
        ListCategoria: List<CategoriesInput>,
        LittMarca: List<BrandsInput>,
        name: String,
        precio: String,
        precioU: String,
        marca: String,
        detalles: String,
        categoria: String,
        stockcantidad: String,
        stockunidad: String,
        qr: String,
        mutableList: MutableList<Uri>, baseActivity: MainActivity
    ): AgregateProductMutation.Data? {
        val name = name
        val precio: Double = precio.toDouble()
        val precioU: Double? = if (precioU.isEmpty()) 0.0 else precioU.toDouble()
        val marca: String = marca
        val detalles: String = detalles
        val categoria: String = categoria
        val stock: Int = if (stockcantidad.isEmpty()) 1 else stockcantidad.toInt()
        val stockU: Int = if (stockunidad.isEmpty()) 0 else stockunidad.toInt()
        val qr: String = qr
        val imagenes= mutableListOf<ImageProductInput>()
        val uploadImagenes = mutableListOf<Upload>()

        for (x in mutableList){
            var item=x.toString().split("/")
            imagenes.add(ImageProductInput(
                image_description=Optional.presentIfNotNull<String>(""),
                image_name =  UUID.randomUUID().toString()+".png" ))
            val imagen = MediaStore.Images.Media.getBitmap(baseActivity.contentResolver, x)
////                            println(path.path)
            val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(x.path!!, imagen)

            val flujo = ByteArrayOutputStream()
            orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
            val imageInByte: ByteArray = flujo.toByteArray()
            val upload = DefaultUpload.Builder().content(imageInByte).build()
            uploadImagenes.add(upload)
        }


        Log.i("SECARGOOOOOIMAGEN",imagenes.toString())


        var sendProduct = ProductsInput(
            LittMarca,
            ListCategoria,
            Optional.presentIfNotNull(detalles),
            imagenes,
            precio,
            precio,
            Optional.presentIfNotNull<Double>(precioU),
            name,
            Optional.presentIfNotNull<String>(qr),
            Optional.presentIfNotNull<Double>(stock.toDouble()),
            Optional.presentIfNotNull<Double>(stockU.toDouble()),
            Optional.presentIfNotNull(Date().toString())
        )


        var JsonProduct: AgregateProductMutation.Data? = cliente.SendProduct(sendProduct,uploadImagenes).data
        return JsonProduct
    }

}