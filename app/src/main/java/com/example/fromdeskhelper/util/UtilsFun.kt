package com.example.fromdeskhelper.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar


fun hideKeyboardFrom(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun MessageSnackBar(view: View, text: String, color: Int) {
    val snackbar = Snackbar.make(
        view, text,
        Snackbar.LENGTH_LONG
    ).setAction("Action", null)

    snackbar.setActionTextColor(color)
    val snackbarView = snackbar.view

    snackbarView.setBackgroundColor(Color.BLACK)
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(color)
    textView.textSize = 17f
    (snackbar.view).layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    snackbar.show()
}

//fun YourFragmentName.hideKeyboard() {
//    view?.let { activity?.hideKeyboard(it) }
//}
//
//fun YoActivityName.hideKeyboard() {
//    hideKeyboard(currentFocus ?: View(this))
//}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}


class TabletPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, pos: Float) {
        page.pivotX = (if (pos > 0) 0 else page.width).toFloat()
        page.pivotY = page.height * 0.5f
        page.rotationY = 45f * pos
    }
}