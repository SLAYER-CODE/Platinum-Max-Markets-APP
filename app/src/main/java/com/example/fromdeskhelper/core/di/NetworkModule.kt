package com.example.fromdeskhelper.core.di
import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpCallFactory
import com.apollographql.apollo3.network.okHttpClient
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.core.ApiKeyInterceptorOkHttpClient
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.data.LocalNetwork.WifiApiClient
import com.example.fromdeskhelper.data.Network.LoginApiClient
import com.example.fromdeskhelper.data.Network.LoginApiClientGraphql
import com.example.fromdeskhelper.domain.CameraUseCase
import com.example.fromdeskhelper.ui.View.Presentations.UnoPresentacionFragmentDirections
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CyclicBarrier
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //provedor de retrofit
    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun providerRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.13:2016/").
            addConverterFactory(GsonConverterFactory.create(gson)).build()
    }


//    @ApiKeyInterceptorOkHttpClient
//    @Singleton
//    @Provides
//    fun providesApiKeyInterceptor(): Interceptor = AuthorizationInterceptor()

    @Singleton
    @Provides
    fun provederOkHttpClient  (authorizationInterceptor: AuthorizationInterceptor,):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providerGraphql(Cliente:OkHttpClient):ApolloClient{
//
//        val okHttpClient = OkHttpClient.Builder()
//            .build()


        return ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
            .okHttpClient(Cliente)
            .build()
    }

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun providerApiClientLogin(retrofit: Retrofit):LoginApiClient{
        return retrofit.create(LoginApiClient::class.java)
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


    @Singleton
    @Provides
    fun providerWifiP2PManager(@ApplicationContext appContext: Context):WifiP2pManager{
        return appContext?.applicationContext?.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager;
    }


    @Singleton
    @Provides
    fun providerWifiP2PChanel(@ApplicationContext appContext: Context,wifiP2pManager: WifiP2pManager):WifiP2pManager.Channel{
        Log.i("MODULENETWORK","SE LLAMO A P2P")
        return wifiP2pManager.initialize(appContext, Looper.getMainLooper(),null)
    }





    @Provides
    @Singleton
//    @ActivityScoped
//    fun providerCameraBase(@ActivityContext aplication:Context,@ApplicationContext appContext: Context):CameraUseCase{
    fun providerCameraBase(@ApplicationContext appContext: Context):CameraUseCase{
        return CameraUseCase(appContext)
    }

    @Singleton
    @Provides
    fun provideBaseActivity( activity: Activity): MainActivity {
        check(activity is MainActivity) { "Every Activity is expected to extend BaseActivity" }
        return activity as MainActivity
    }
//    @Singleton
//    @Provides
//    fun
}