package com.example.fromdeskhelper

import android.app.Application
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ActivityMainApp:Application(){
    override fun onCreate() {
        FacebookSdk.sdkInitialize(this);
        FirebaseApp.initializeApp(this);
        super.onCreate()
    }
}