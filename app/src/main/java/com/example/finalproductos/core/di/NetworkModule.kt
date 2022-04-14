package com.example.finalproductos.core.di
import android.content.Context
import android.net.wifi.WifiManager
import com.example.finalproductos.data.LocalNetwork.WifiApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //provedor de retrofit
    @Singleton
    @Provides
    fun providerRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://drawsomething-59328-default-rtdb.europe-west1.firebasedatabase.app/").
            addConverterFactory(GsonConverterFactory.create()).build()
    }
    @Singleton
    @Provides
    fun providerQuoteApiClient(retrofit: Retrofit):WifiApiClient{

        return retrofit.create(WifiApiClient::class.java)
    }

    @Singleton
    @Provides
    fun providerWifiManager(@ApplicationContext appContext: Context):WifiManager{
        return appContext?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager;
    }

//    @Singleton
//    @Provides
//    fun
}