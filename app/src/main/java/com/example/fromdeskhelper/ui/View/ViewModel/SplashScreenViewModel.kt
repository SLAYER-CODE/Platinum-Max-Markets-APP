package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.core.di.NetworkModule
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.LoginIntProvider
import com.example.fromdeskhelper.domain.ComprobationUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import java.sql.Time
import java.util.concurrent.TimeUnit
import javax.inject.Inject


var LOG_CLASS="SPLASH_VIEWMODEL"
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val loginApollo:ComprobationUserUseCase,
    private val loginPreferences: PreferencesManager,
    private val authorizationInterceptor: AuthorizationInterceptor,
):AndroidViewModel(Application()) {
    val initLogOperator = MutableLiveData<Boolean>()
    val initPrecentation = MutableLiveData<Boolean>()
    val initLogin = MutableLiveData<Boolean>()
    val UserPreferences = loginPreferences.preferencesUserFlow

    operator fun invoke(){
        viewModelScope.launch {
            UserPreferences.collect { it ->
                Log.i(LOG_CLASS,"PRESENATACION")
                Log.i(LOG_CLASS,it.presentation.toString())
                Log.i(LOG_CLASS,it.loginInit.toString())
                if (it.presentation){
                    Log.i(LOG_CLASS,"El usuario no pudo verificar si esta registrado")
                    initPrecentation.postValue(true);
                } else if (!it.loginInit){

                    var usuario = FirebaseAuth.getInstance().currentUser?.uid
                    if(usuario==null){
                        Log.i(LOG_CLASS,"El usuario falta por registrarse")
                        initLogin.postValue(true)
                    }else {
                        Log.i(LOG_CLASS,"El usuario esta registrado comprobando")
                        FirebaseAuth.getInstance().getAccessToken(true).addOnCompleteListener { token->
                            if(token.isSuccessful){
                                viewModelScope.launch {
                                    authorizationInterceptor.setSessionToken(token.result.token.toString())
//                                    var resultado = loginApollo(token.result.token.toString())
                                    initLogOperator.postValue(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}