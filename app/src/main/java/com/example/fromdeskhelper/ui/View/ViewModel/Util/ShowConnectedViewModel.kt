package com.example.fromdeskhelper.ui.View.ViewModel.Util
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.core.di.NetworkModule
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.LoginIntProvider
import com.example.fromdeskhelper.domain.ComprobationUserUseCase
import com.example.fromdeskhelper.util.isConnected
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import java.sql.Time
import java.util.concurrent.TimeUnit
import javax.inject.Inject


var LOG_CLASS = "SPLASH_VIEWMODEL"

@HiltViewModel
class ShowConnectedViewModel @Inject constructor(
) : AndroidViewModel(Application()) {
    val connected = MutableLiveData<Boolean>()
    val initLogMessage = MutableLiveData<String>()
    operator fun invoke(context: Context) {
        viewModelScope.launch {
            while (true) {
                //Esto envia falso si la conexion fue falsa o verdadero si la conexion fue correcta
                connected.postValue(isConnected(context))
                delay(1000)
            }
        }
    }
}

//enum class CameraTypes {
//    NULL,
//    CAMERA,
//    SCANER,
//    CAMERAIMAGEN,
//    VIDEO,
//    CAMERAVIDEO,
//}