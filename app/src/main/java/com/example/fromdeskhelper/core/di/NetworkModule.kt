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
import com.example.fromdeskhelper.core.static.AppConst
import com.example.fromdeskhelper.data.LocalNetwork.WifiApiClient
import com.example.fromdeskhelper.data.Network.LoginApiClient
import com.example.fromdeskhelper.data.model.objects.DateAdapter
import com.example.fromdeskhelper.domain.CameraUseCase
import com.example.fromdeskhelper.type.DateTime
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
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
import java.net.ConnectException
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.annotation.Nullable
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //provedor de retrofit
    var gson = GsonBuilder()
        .setLenient()
        .create()
    var PROVIDER_INTERNAL_INTERCEPTOR = "INTERNAL_CONECTION"

    @Singleton
    @Provides
    @Nullable
    @Named("Server")
    fun providerRetrofit():Retrofit?{
        var rester = OkHttpClient().
        newBuilder().readTimeout(200,TimeUnit.SECONDS).
        connectTimeout(200,TimeUnit.SECONDS).
        retryOnConnectionFailure(true).addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    try{
                        Log.i(PROVIDER_INTERNAL_INTERCEPTOR,"CONECTTUNEL")
                        return chain.proceed(chain.request());
                    }catch(e:Exception){
                        Log.i(PROVIDER_INTERNAL_INTERCEPTOR,e.toString())
                        return Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(999)
                            .message("UNDEFINED")
                            .body(ResponseBody.create(null, "{${e}}")).build()
                    }
                }
            }).build()
        try {
            Log.i(PROVIDER_INTERNAL_INTERCEPTOR,"Connectig SERVER LOCAL")
            return Retrofit.Builder()
                .baseUrl(AppConst.BASE_URL_SERVER).client(rester).
                addConverterFactory(GsonConverterFactory.create(gson)).build()
        }catch (ex:Exception){
            Log.i(PROVIDER_INTERNAL_INTERCEPTOR,ex.message.toString())
            return null
        }
    }


//    @ApiKeyInterceptorOkHttpClient
//    @Singleton
//    @Provides
//    fun providesApiKeyInterceptor(): Interceptor = AuthorizationInterceptor()
    var PROVIDER_INTERNAL = "INTERNAL_CONECTION"

    @Singleton
    @Provides
    @Nullable
    fun provederOkHttpClient  (authorizationInterceptor: AuthorizationInterceptor):OkHttpClient?{
        try {
            return OkHttpClient.Builder()
                .addInterceptor(authorizationInterceptor)
                .build()
        }catch (ex:Exception){
            Log.e(PROVIDER_INTERNAL,ex.message.toString())
            return null
        }
    }


    var PROVIDER_GRAPQL = "GRAPQL_SHOWING"



    //Esta es la clase que maneja todas las solicitudes
    @Singleton
    @Provides
    @Nullable
    fun providerGraphql(Cliente:OkHttpClient?):ApolloClient?{
//
//        val okHttpClient = OkHttpClient.Builder()
//            .build()
        try {
            if(Cliente!=null){
                return ApolloClient.Builder().serverUrl(AppConst.BASE_URL_GRAPHQL)
                    .okHttpClient(Cliente)
                    .addCustomScalarAdapter(DateTime.type,DateAdapter)
                    .build()
            }else{
                return null
            }
        }catch (ex:Exception){
            Log.e(PROVIDER_GRAPQL,ex.message.toString())
            return null
        }
    }

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    @Nullable
    fun providerApiClientLogin(@Named("Server") retrofit: Retrofit?):LoginApiClient?{
        try {
            return retrofit?.create(LoginApiClient::class.java)
        }catch (ex:ConnectException){
            return null
        }
    }

    @Singleton
    @Provides
    @Nullable
    fun providerQuoteApiClient(@Named("Server") retrofit: Retrofit?):WifiApiClient?{
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
    fun provideBaseActivity( activity: Activity): EmployedMainActivity {
        check(activity is EmployedMainActivity) { "Every Activity is expected to extend BaseActivity" }
        return activity as EmployedMainActivity
    }

    @Singleton
    @Provides
    fun getContext(@ApplicationContext appContext: Context):Context{
        return appContext
    }
//    @Singleton
//    @Provides
//    fun
}