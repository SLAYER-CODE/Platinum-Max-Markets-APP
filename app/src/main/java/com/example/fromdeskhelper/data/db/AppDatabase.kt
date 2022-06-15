package com.example.fromdeskhelper.data.db

import Data.ClientAndProducts
import Data.ClientList
import Data.ImagenesNew
import Data.Producto
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fromdeskhelper.data.model.base.ProductoData

@Database(entities = [Producto::class, ImagenesNew::class,ClientList::class,ClientAndProducts::class],version = 5)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase(){

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