package com.example.fromdeskhelper.domain.Root
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.fromdeskhelper.ExifUtil
import com.example.fromdeskhelper.data.Network.LocalServiceMG
import com.example.fromdeskhelper.data.model.objects.ResponseAdd
import com.example.fromdeskhelper.data.model.objects.Upload
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsLocalImput
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsModelAdapter
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.google.gson.annotations.SerializedName
import org.bson.Document
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject


private var LOG_USECASECONNECTLOCAL="USECASELOCALCONNECT"

class LocalConnectUseCase @Inject constructor(
    private val service:LocalServiceMG,
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
            var Images= it.get("image_realation") as MutableList<Document>
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
                it.get("brands_products") as List<String>,
                it.get("category_products") as List<String>,
                it.getString("description"),
                sedImage,
                it.getOrDefault("old_price",0.0).toString().toDouble(),
                it.getOrDefault("price_cantidad",0.0).toString().toDouble(),
                it.getOrDefault("price_unity",0.0).toString().toDouble(),
                it.getOrDefault("product_name","Undefinend").toString(),
                it.getOrDefault("qr","Undefined").toString(),
                it.getInteger("quantity_cantidad"),
                it.getInteger("quantity_unity"),
                it.getString( "update_product"),
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
        mutableList: MutableList<Uri>, baseActivity: EmployedMainActivity,
        server:Int?=2
    ):Boolean{
        val name = name
        val precio: Double = precio.toDouble()
        val precioU: Double? = if (precioU.isEmpty()) 0.0 else precioU.toDouble()
        val marca: String = marca
        val detalles: String = detalles
        val categoria: String = categoria
        val stock: Int = if (stockcantidad.isEmpty()) 1 else stockcantidad.toInt()
        val stockU: Int = if (stockunidad.isEmpty()) 0 else stockunidad.toInt()
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
//            orientedBitmap.compress(Bitmap.CompressFormat.JPEG, 1000, flujo)
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
            precio.toDouble(),
            precio.toDouble(),
            precioU?.toDouble(),
            name,
            qr,
            stock,
            stockU,
            Date().toString()
        )

        if(server==Upload.SERVER_LOCAL){
            var server = service.sendProductServer(sendProduct)
            Log.i("OPENSERVERLOCALPRODUCT","agrengando al servidor enviando peticion")
            //realizando un ping
//            service.sendPingLocal()
//            service.PingLocal()
            return server!=null && server.isSuccessful
        }else if(server==Upload.SERVER_LOCAL_DATABASE){
            var result = Document()
            result.append("update_product", sendProduct.update_product)
                .append("product_name", sendProduct.product_name)
                .append("old_price", sendProduct.old_price)
                .append("price_cantidad", sendProduct.price_cantidad)
                .append("price_unity", sendProduct.price_unity)
                .append("description", sendProduct.description)
                .append("quantity_cantidad", sendProduct.quantity_cantidad)
                .append("quantity_unity", sendProduct.quantity_unity)
                .append("qr", sendProduct.qr)
                .append("image_realation", sendProduct.image_realation)
                .append("category_products", sendProduct.category_products)
                .append("brands_products", sendProduct.brands_products)
            return service.AgregateProduct(result)
        }else{
            return false
        }
//        return ResponseAdd(Upload.SERVER_LOCAL_DATABASE, true)
    }

}

