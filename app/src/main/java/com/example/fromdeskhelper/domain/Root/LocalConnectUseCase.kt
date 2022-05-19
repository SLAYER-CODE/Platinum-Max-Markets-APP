package com.example.fromdeskhelper.domain.Root
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.Network.LocalServiceMG
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import org.bson.Document
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject


private var LOG_USECASECONNECTLOCAL="USECASELOCALCONNECT"

class LocalConnectUseCase @Inject constructor(
    private val service:LocalServiceMG
) {
    suspend fun ConnectComprobate():Boolean{
        return service.ComprobateConect()
}
    suspend fun ComprobateTest():Boolean{
        return service.CreateCollection()
    }
    suspend fun GetProductsAll():List<ProductsModelAdapter>{
        var lista:MutableList<ProductsModelAdapter> = mutableListOf()
        var resupuesta=service.GetPProductAllsLocal()
//        Log.e("Final",resupuesta?.toMutableList().toString())
        resupuesta?.forEach {
//            Log.i(LOG_USECASECONNECTLOCAL, it.get("imageList").toString() )
            var Images= it.get("imageList") as MutableList<Document>
            var sedImage : MutableList<Pair<String,ByteArray>> = mutableListOf()
            for (x in Images){
                Log.i(LOG_USECASECONNECTLOCAL,x.keys.toString())
                for(z in x.keys.toList()){
                    Log.i(LOG_USECASECONNECTLOCAL,z.toString())
                }
                var keyName=x.keys.toList()[0]
                var imagen = Base64.getDecoder().decode(x.getString(keyName))
                var primero= keyName+"."+  x.get("extencion")
                sedImage.add(Pair(primero,imagen))
            }
            var Product:ProductsModelAdapter
            =ProductsModelAdapter(
                it.get("brandList") as List<String>,
                it.get("categoryList") as List<String>,
                it.getString("detalles"),
                sedImage,
                it.getDouble("precioC"),
                it.getDouble("precioU"),
                it.getString("name"),
                it.getString("qr"),
                it.getDouble("stockC"),
                it.getDouble("stockU"),
                it.getString( "update"),
            )
            lista.add(Product)
        }
        return lista
    }
    suspend fun AddProductOne(
        ListCategoria: List<String>,
        LittMarca: List<String>,
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
    ):Boolean{
        val name = name
        val precio: Double = precio.toDouble()
        val precioU: Double? = if (precioU.isEmpty()) 0.0 else precioU.toDouble()
        val marca: String = marca
        val detalles: String = detalles
        val categoria: String = categoria
        val stock: Int = if (stockcantidad.isEmpty()) 1 else stockcantidad.toInt()
        val stockU: Double = if (stockunidad.isEmpty()) 0.0 else stockunidad.toDouble()
        val qr: String = qr
        val imagenes= mutableListOf<Document>()

        for (x in mutableList){
            Log.i("IMAGENDATORES",x.isAbsolute.toString())
            var item=x.toString().split("/")
            var itemMage=item.get(item.size-1).split(".")
            val imagen = MediaStore.Images.Media.getBitmap(baseActivity.contentResolver, x)
////                            println(path.path)
            val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(x.path!!, imagen)
            val flujo = ByteArrayOutputStream()
            orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
            val imageInByte: ByteArray = flujo.toByteArray()
//            val upload = DefaultUpload.Builder().content(imageInByte).build()
//            uploadImagenes.add(upload)
            imagenes.add(
                try {
                    Document(
                        itemMage[0], Base64.getEncoder().encodeToString(imageInByte)
                    ).append("extencion", itemMage[1])
                }catch (ex:Exception){
                    Document(
                        UUID.randomUUID().toString(), Base64.getEncoder().encodeToString(imageInByte)
                    ).append("extencion", "jpg")
                }
            )
        }
        Log.i("SECARGOOOOOIMAGEN",imagenes.toString())

        var sendProduct = ProductsLocalImput(
            LittMarca,
            ListCategoria,
            detalles,
            imagenes,
            precio,
            precio,
            precioU,
            name,
            qr,
            stock.toDouble(),
            stockU.toDouble(),
            Date().toString()
        )
        var result = Document()
        result.
            append("update", sendProduct.update_product).
            append("name", sendProduct.product_name).
            append("precioC", sendProduct.price_cantidad).
            append("precioU", sendProduct.price_unity).
            append("detalles", sendProduct.description).
            append("stockC", sendProduct.quantity_cantidad).
            append("stockU", sendProduct.quantity_unity).
            append("qr", sendProduct.qr).
            append("imageList",sendProduct.image_realation).
            append("categoryList", sendProduct.category_products).
            append("brandList", sendProduct.brands_products)


        return service.AgregateProduct(result)
    }
}


public data class ProductsLocalImput(
    public val brands_products: List<String>,
    public val category_products: List<String>,
    public val description: String,
    public val image_realation: MutableList<Document>,
    public val old_price: Double,
    public val price_cantidad: Double,
    public val price_unity: Double?,
    public val product_name: String,
    public val qr: String?,
    public val quantity_cantidad: Double?,
    public val quantity_unity: Double?,
    public val update_product: String?,
)


public data class ProductsModelAdapter(
    public val brands_products: List<String>,
    public val category_products: List<String>,
    public val description: String,
    public val image_realation: MutableList<Pair<String,ByteArray>>,
    public val price_cantidad: Double,
    public val price_unity: Double?,
    public val product_name: String,
    public val qr: String?,
    public val quantity_cantidad: Double?,
    public val quantity_unity: Double?,
    public val update_product: String?,
)