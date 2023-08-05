package com.example.fromdeskhelper.domain.Root
import android.content.Context
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
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.google.gson.annotations.SerializedName
import org.bson.Document
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


private var LOG_USECASECONNECTLOCAL="USECASELOCALCONNECT"

class LocalConnectUseCase @Inject constructor(
    private val service:LocalServiceMG,
    private val context: Context,

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
                it.getString("product_name"),
                it.get("price").toString().toDouble(),
                it.getOrDefault("price_neto",null)?.toString()?.toDouble(),
                it.getOrDefault("discount",null)?.toString()?.toDouble(),

                it.getOrDefault("quantity_cantidad",null)?.toString()?.toInt(),
                it.getOrDefault("stockC_attr",null)?.toString()?.toInt(),
                it.getOrDefault("quantity_unity",null)?.toString()?.toInt(),
                it.getOrDefault("stockU_attr",null)?.toString()?.toInt(),
                it.getOrDefault("peso",null)?.toString()?.toDouble(),
                it.getOrDefault("peso_attr",null)?.toString()?.toInt(),
                it.getOrDefault("stock_attr",null)?.toString()?.toInt(),
                it.getOrDefault("qr","Undefined").toString(),
                it.getOrDefault("description",null)?.toString(),
                it.getOrDefault("available_date",null)?.toString(),

                it.getOrDefault("available_now",null).toString().toBoolean(),
                it.getOrDefault("available_shipment",null).toString().toBoolean(),

                it.getOrDefault("update_product",null).toString(),
                it.getString("uniqueid"),
                it.get("brands_products") as MutableList<BrandsInput>,
                it.get("category_products") as MutableList<CategoriesInput>,
                sedImage
            )
            lista.add(Product)
        }
        return lista
    }


    suspend fun AddProductOne(
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
        server:Int
        ):Boolean{

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

        val imagenes= mutableListOf<Document>()

        for (x in mutableList){
            Log.i("IMAGENDATORES",x.isAbsolute.toString())
            var item=x.toString().split("/")
            var itemMage=item.get(item.size-1).split(".")
            val imagen = MediaStore.Images.Media.getBitmap(context.contentResolver, x)
//                            println(path.path)
            val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(x.path!!, imagen)
            val flujo = ByteArrayOutputStream()
            orientedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, flujo)
            val imageInByte: ByteArray = flujo.toByteArray()
//            val upload = DefaultUpload.Builder().content(imageInByte).build()
//            uploadImagenes.add(upload)
            var encode = Base64.getEncoder().encodeToString(imageInByte)
//            Log.i("IMAGEN",encode)
            imagenes.add(
//                try {
                    Document(
                        "img", encode
                    ).append("extencion", itemMage[1])
//                }catch (ex:Exception){
//                    Document(
//                        UUID.randomUUID().toString(), Base64.getEncoder().encodeToString(imageInByte)
//                    ).append("extencion", "jpg")
//                }
            )
        }

//        Log.i("SECARGOOOOOIMAGEN",imagenes.toString())

        var sendProduct = ProductsLocalImput(
            product_name = _name,
            price = _precio,
            price_neto = _precioNeto,
            discount = _disconut,
            quantity_cantidad = _stock,
            stockC_attr = _stockC_attr,
            quantity_unity = _stockU,
            stockU_attr= _stockU_attr,
            peso= _peso,
            peso_attr = _peso_attr,
            stock_attr = _stock_attr,
            qr = _qr,
            description = _detalles,
            available_date = _available_date?.toString(),
            available_now = _available_now,
            available_shipment = _available_shipment,
            update_product = Date().toString(),
            unique = _uuid,
            brands_products = marca,
            category_products = categoria,
            image_realation = imagenes,
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

            val brands= mutableListOf<Document>()
            for (x in sendProduct.brands_products) {
                brands.add(Document("brand_name",x.brand_name))
            }
            val categoryes= mutableListOf<Document>()
            for (x in sendProduct.category_products) {
                categoryes.add(Document("category_name",x.category_name))
            }


            result.append("product_name", sendProduct.product_name)
                .append("price", sendProduct.price)
                .append("price_neto", sendProduct.price_neto)
                .append("discount", sendProduct.discount)
                .append("quantity_cantidad", sendProduct.quantity_cantidad)
                .append("stockC_attr", sendProduct.stockC_attr)
                .append("quantity_unity", sendProduct.quantity_unity)
                .append("stockU_attr", sendProduct.stockU_attr)
                .append("peso", sendProduct.peso)
                .append("peso_attr", sendProduct.peso_attr)
                .append("stock_attr", sendProduct.stock_attr)
                .append("description", sendProduct.description)
                .append("available_date", sendProduct.available_date)
                .append("available_now", sendProduct.available_now)
                .append("available_shipment", sendProduct.available_shipment)
                .append("update_product", sendProduct.update_product)
                .append("uniqueid", sendProduct.unique)

                .append("brands_products", brands)
                .append("category_products", categoryes)
                .append("image_realation", imagenes)
            return service.AgregateProduct(result)
        }else{
            return false
        }
//        return ResponseAdd(Upload.SERVER_LOCAL_DATABASE, true)
    }

}

