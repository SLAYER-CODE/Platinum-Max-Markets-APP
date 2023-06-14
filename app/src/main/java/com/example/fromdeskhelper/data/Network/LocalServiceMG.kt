package com.example.fromdeskhelper.data.Network

import android.util.Log
import com.example.fromdeskhelper.core.static.Collections
import com.example.fromdeskhelper.data.LocalNetwork.QuoteApiLocalServer
import com.example.fromdeskhelper.data.model.objects.mongod.ProductsLocalImput
import com.mongodb.MongoException
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.Document
import org.bson.conversions.Bson
import retrofit2.Response
import javax.inject.Inject

private var LOG_Service = "MONGODBSERVICE"

class LocalServiceMG @Inject constructor(
    private val Database: MongoDatabase,
    private val restService : QuoteApiLocalServer?
) {

    suspend fun sendProductServer(product:ProductsLocalImput): Response<Boolean>? {
        return withContext(Dispatchers.IO){
            try {
                return@withContext restService?.addproduct(product)
            }catch (ex:Exception){
                return@withContext null
            }
        }
    }

    suspend fun sendPingLocal(): Response<ResponseBody>? {
        return withContext(Dispatchers.IO){
            return@withContext  restService?.pingAdd("1")
        }
    }

    suspend fun PingLocal(): Response<ResponseBody>? {
        return withContext(Dispatchers.IO){
            return@withContext  restService?.pingLocal()
        }
    }
    suspend fun ComprobateConect(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                var Command: Bson = BsonDocument("ping", BsonInt64(1))
                var comandResult: Document = Database.runCommand(Command)
                Log.i(LOG_Service, "BASE DE DATOS INICIO CORRECTAMENTE")
                true
            } catch (me: MongoException) {
                Log.i(LOG_Service, "Ocurrio un error")
                Log.e(LOG_Service, me.toString())
                false
            }
        }
    }

    suspend fun AgregateProduct(Product:Document): Boolean {
        return withContext(Dispatchers.IO) {
            try {
//                Database.createCollection("productos
                var segundo = Database.getCollection(Collections.BASE_DATABASE_COLLECTION_PRODUCT)
                segundo.insertOne(Product)
                true
            } catch (me: MongoException) {
                Log.e(LOG_Service, me.toString())

                false
            }
        }
    }

    suspend fun GetPProductAllsLocal(): List<Document>? {
        return withContext(Dispatchers.IO) {
            try {
//                Database.createCollection("productos
                var segundo = Database.getCollection(Collections.BASE_DATABASE_COLLECTION_PRODUCT)
                var result = segundo.find()
                return@withContext result.toList()
            } catch (me: MongoException) {
                Log.e(LOG_Service, me.toString())
                return@withContext null
            }
        }
    }

    suspend fun CreateCollection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
//                Database.createCollection("productos
                var segundo = Database.getCollection(Collections.BASE_DATABASE_COLLECTION_PRODUCT)
                segundo.insertOne(
                    Document(
                        mapOf(
                            Pair("update", "carlos"),
                            Pair("nombre", "carlos"),
                            Pair("precioC", "carlos"),
                            Pair("precioU", "carlos"),
                            Pair("marca", "carlos"),
                            Pair("detalles", "carlos"),
                            Pair("stockC", "carlos"),
                            Pair("stockU", "carlos"),
                            Pair("qr", "carlos"),
                            Pair("imageList", mapOf(Pair("name", "carlos"))),
                            Pair("categoryList", mapOf(Pair("name", "carlos"))),
                            Pair("brandList", mapOf(Pair("name", "carlos"))),
                        )
                    )
                )
                true
            } catch (me: MongoException) {
                Log.i(LOG_Service, "Ocurrio un error",me)
                false
            }
        }
    }
}