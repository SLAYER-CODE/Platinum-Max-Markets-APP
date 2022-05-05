package com.example.fromdeskhelper.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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