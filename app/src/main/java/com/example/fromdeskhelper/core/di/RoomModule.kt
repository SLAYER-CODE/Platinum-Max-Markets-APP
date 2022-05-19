package com.example.fromdeskhelper.core.di

import android.content.Context
import androidx.room.Room
import com.example.fromdeskhelper.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val PRODUCT_DATABASE_NAME="productosMA"
    @Singleton
    @Provides
    fun providerRoom(@ApplicationContext context:Context)= Room.databaseBuilder(context.applicationContext,
        AppDatabase::class.java, PRODUCT_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providerProductDao(db:AppDatabase)=db.productosData()
}