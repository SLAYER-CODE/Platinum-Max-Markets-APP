package com.example.fromdeskhelper.data.Network

import android.util.Log
import com.example.fromdeskhelper.core.static.Collections
import com.mongodb.MongoException
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.Document
import org.bson.conversions.Bson
import javax.inject.Inject

private var LOG_Service = "UTILSMONGODBSERVICE"

class UtilLocalServiceMG @Inject constructor(
    private val Database: MongoDatabase
) {
    suspend fun GetPProductCount(): Int? {
        return withContext(Dispatchers.IO) {
            try {
                var segundo = Database.getCollection(Collections.BASE_DATABASE_COLLECTION_PRODUCT)
                var result = segundo.count()
                return@withContext result.toInt()
            } catch (me: MongoException) {
                Log.e(LOG_Service, me.toString())
                return@withContext null
            }
        }
    }
}