package com.example.fromdeskhelper.data.Network

import android.util.Log
import com.example.fromdeskhelper.domain.Root.ProductsLocalImput
import com.mongodb.MongoException
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.Document
import org.bson.conversions.Bson
import javax.inject.Inject

private var LOG_Service = "MONGODBSERVICE"

class LocalServiceMG @Inject constructor(
    private val Database: MongoDatabase
) {
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
                var segundo = Database.getCollection("product")
                segundo.insertOne(Product)
                true
            } catch (me: MongoException) {
                false
            }
        }
    }

    suspend fun GetPProductAllsLocal(): List<Document>? {
        return withContext(Dispatchers.IO) {
            try {
//                Database.createCollection("productos
                var segundo = Database.getCollection("product")
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
                var segundo = Database.getCollection("product")
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
                false
            }
        }
    }
}