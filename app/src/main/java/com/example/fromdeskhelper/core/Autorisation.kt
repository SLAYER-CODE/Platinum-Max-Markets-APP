package com.example.fromdeskhelper.core

import com.example.fromdeskhelper.data.model.LoginIntProvider
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
class AuthorizationInterceptor @Inject constructor (): Interceptor {
    private var sessionToken: String = "NUll"
    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken

    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization",  sessionToken)
            .build()

        return chain.proceed(request)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiKeyInterceptorOkHttpClient

@Singleton
class MyServiceInterceptor @Inject constructor() : Interceptor {
    private var sessionToken: String = "None"
    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
//        if (request.header(NO_AUTH_HEADER_KEY) == null) {
//            // needs credentials
//            if (sessionToken == null) {
//                throw RuntimeException("Session token should be defined for auth apis")
//            } else {
                requestBuilder.addHeader("Cookie", sessionToken)
//            }
//        }
        return chain.proceed(requestBuilder.build())
    }
}