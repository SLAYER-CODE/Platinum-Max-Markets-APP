package com.example.fromdeskhelper.data.db

import Data.BrandProductRef
import Data.Brands
import Data.Categories
import Data.CategoryProductRef
import Data.ClientAndProducts
import Data.ClientList
import Data.ImagenesProduct
import Data.Producto
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fromdeskhelper.data.model.base.ProductoData

@Database(
    entities = [
        Producto::class,
        ImagenesProduct::class,
        ClientList::class,
        ClientAndProducts::class,
        Brands::class,
        BrandProductRef::class,
        Categories::class,
        CategoryProductRef::class
    ], version = 5
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productosData(): ProductoData
//    companion object{
//        @Volatile
//        private var INSTACE: AppDatabase?=null
//        fun getDataBase(context: Context): AppDatabase {
//            val tempInstance = INSTACE
//            if(tempInstance != null){
//                return tempInstance
//            }
//
//            synchronized(this){
//                val instance =Room.databaseBuilder(context.applicationContext,
//                    AppDatabase::class.java,"productosMA").build()
//                INSTACE = instance
//                return instance
//            }
//        }
//    }
}