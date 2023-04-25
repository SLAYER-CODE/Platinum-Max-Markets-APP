package com.example.fromdeskhelper.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import android.os.Build




class ConNet {
     companion object {
//         fun ComprobationInternet(activdad: Activity): Boolean {
//             val connectivityManager =
//                 activdad.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
////             val res = connectivityManager.activeNetwork
//
//             val networkInfo = connectivityManager.activeNetworkInfo
////             println(networkInfo?.extraInfo)
////             println(networkInfo?.type)
////             val Nuevo=connectivityManager.getNetworkCapabilities(res)
////             return Nuevo!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||  Nuevo!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//
//             return networkInfo != null && networkInfo.isConnected
//         }

         fun ComprobationInternet(activdad: Activity): Boolean {
             val cm = activdad.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
             if (cm != null) {
                 if (Build.VERSION.SDK_INT < 23) {
                     val ni = cm.activeNetworkInfo
                     if (ni != null) {
                         return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                     }
                 } else {
                     val n = cm.activeNetwork
                     if (n != null) {
                         val nc = cm.getNetworkCapabilities(n)
                         return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                             NetworkCapabilities.TRANSPORT_WIFI
                         )
                     }
                 }
             }
             return false
         }
     }
}