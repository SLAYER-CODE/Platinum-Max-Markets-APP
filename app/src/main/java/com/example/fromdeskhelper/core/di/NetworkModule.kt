package com.example.fromdeskhelper.core.di

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.core.onOnIntercept
import com.example.fromdeskhelper.data.LocalNetwork.WifiApiClient
import com.example.fromdeskhelper.data.Network.LoginApiClient
import com.example.fromdeskhelper.domain.CameraUseCase
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import javax.annotation.Nullable
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
    @Nullable
    fun providerRetrofit():Retrofit?{

        var rester = OkHttpClient().
        newBuilder().readTimeout(200,TimeUnit.SECONDS).
        connectTimeout(200,TimeUnit.SECONDS).
        retryOnConnectionFailure(true).addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    try{
                        return chain.proceed(chain.request());
                    }catch(e:Exception){
                        Log.i("SEMOVIO","SIN CONEXION")
                        return Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(999)
                            .message("Sin respuesta")
                            .body(ResponseBody.create(null, "{${e}}")).build()
                    }
                }

            }).build()


        try {
            return Retrofit.Builder()
                .baseUrl("http://192.168.0.13:2016/").client(rester).
                addConverterFactory(GsonConverterFactory.create(gson)).build()
        }catch (ex:Exception){
            return null
        }
    }


//    @ApiKeyInterceptorOkHttpClient
//    @Singleton
//    @Provides
//    fun providesApiKeyInterceptor(): Interceptor = AuthorizationInterceptor()

    @Singleton
    @Provides
    @Nullable

    fun provederOkHttpClient  (authorizationInterceptor: AuthorizationInterceptor):OkHttpClient?{
        try {
            return OkHttpClient.Builder()
                .addInterceptor(authorizationInterceptor)
                .build()
        }catch (ex:Exception){
            return null
        }

    }

    @Singleton
    @Provides
    @Nullable
    fun providerGraphql(Cliente:OkHttpClient?):ApolloClient?{
//
//        val okHttpClient = OkHttpClient.Builder()
//            .build()

        try {
            if(Cliente!=null){
                return ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
                    .okHttpClient(Cliente)
                    .build()
            }else{
                return null
            }
        }catch (ex:Exception){
            return null
        }

    }

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    @Nullable
    fun providerApiClientLogin(retrofit: Retrofit?):LoginApiClient?{
        try {
            return retrofit?.create(LoginApiClient::class.java)
        }catch (ex:ConnectException){
            return null
        }
    }

    @Singleton
    @Provides
    @Nullable
    fun providerQuoteApiClient(retrofit: Retrofit?):WifiApiClient?{
        return retrofit?.create(WifiApiClient::class.java)
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