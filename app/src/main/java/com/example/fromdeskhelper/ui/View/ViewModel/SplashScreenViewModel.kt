package com.example.fromdeskhelper.ui.View.ViewModel

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
import com.example.fromdeskhelper.data.model.objects.UserLogin
import com.example.fromdeskhelper.domain.ComprobationUseCase
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
class SplashScreenViewModel @Inject constructor(
    private val loginApollo: ComprobationUseCase ,
    private val loginPreferences: PreferencesManager,
    private val authorizationInterceptor: AuthorizationInterceptor,
) : AndroidViewModel(Application()) {
    val initLogOperator = MutableLiveData<UserLogin>()
    val initLogMessage = MutableLiveData<String>()
    val initPrecentation = MutableLiveData<Boolean>()
    val initLogin = MutableLiveData<Boolean>()
    val initLoginAnonime = MutableLiveData<Boolean>()
    val UserPreferences = loginPreferences.preferencesUserFlow
    val CountFailure: Int = 0
    operator fun invoke(context: Context) {
        viewModelScope.launch {
            while (true) {
                if (isConnected(context)) {
                    UserPreferences.collect { it ->
                        Log.i(LOG_CLASS, "PRESENATACION")
                        Log.i(LOG_CLASS, it.presentation.toString())
                        Log.i(LOG_CLASS, it.loginInit.toString())
                        if (it.presentation) {
                            Log.i(LOG_CLASS, "El usuario no pudo verificar si esta registrado")
                            initPrecentation.postValue(true);
                        } else if (!it.loginInit) {
                            var usuario = FirebaseAuth.getInstance().currentUser?.uid
                            if (usuario == null) {
                                Log.i(LOG_CLASS, "El usuario falta por registrarse")
                                initLogin.postValue(true)
                            } else {
                                Log.i(LOG_CLASS, "El usuario esta registrado comprobando")
                                FirebaseAuth.getInstance().getAccessToken(true)
                                    .addOnCompleteListener { token ->
                                        if (token.isSuccessful) {
                                            //Este bloque de codigo muestra que fue correcta la autenticacion en el servidor de firebase
                                            viewModelScope.launch {
                                                authorizationInterceptor.setSessionToken(token.result.token.toString())
                                                //Agregando el valor de usuarios
                                                //Restulado tiene las funciones y el rol que cumple en caso de que no sea usuario

                                                if(loginApollo.connected()!=null){
                                                    var resultado = loginApollo()

                                                    if(resultado==null){
                                                        //Sin conexion hacia el servidor O no es empleado
                                                        //Comprobar si es usuario >:

                                                        initLogOperator.postValue(UserLogin(false,null, false,false,true))
                                                    }else{
                                                        //ROOT->ADMIN->EMPLOYED
//                                                        if(resultado){
                                                            //Si es empleado
                                                            var Admin = loginApollo.Cadmin()
                                                            if(Admin!=null){
                                                                Log.i(LOG_CLASS, "Entro como admin")
                                                                initLogOperator.postValue(UserLogin(null,null, false,true,null))
                                                                //Iniciame con todos los modulos de usuario y empleado
                                                            }else{
                                                                Log.i(LOG_CLASS, "Admin es igual a null")
                                                                //Si no es compruebame si tiene un rol definido
                                                                var Empleado = (loginApollo.Cemployed())
                                                                if(Empleado!=null){
                                                                    //Iniciamos como empleado
                                                                    initLogOperator.postValue(UserLogin(true,null, false,false,null))
                                                                }else{
                                                                    Log.i(LOG_CLASS, "NO ubo una interaccion clara con el servidor")

                                                                    //NO ubo conexion o hay un error
                                                                    //Mensaje de reintento o iniciar como Anonimo
                                                                    initLogOperator.postValue(UserLogin(false,null, false,false,true))
                                                                }
                                                            }
                                                            //Significa que es trabajador de la empresa (Empleado,Jefe,Admin,etc..)

//                                                        }else{
                                                            //Este bloque no deberia utilizarse pero si no es entonces deberia ser Anonimo!
                                                            //Significa que es usuario

//                                                        }
                                                    }
                                                }else{
                                                    //No hay conexion al servidor pero si logor conectar a firebase
                                                    Log.i(LOG_CLASS, "Quiere iniciar como operador")
                                                    initLogOperator.postValue(UserLogin(false,true, false,false,null))
                                                }
                                            }
                                        } else {
                                            Log.i(LOG_CLASS, "Quiere iniciar como operador")
                                            //No hay una validacion con firebase o no se logor ingresar
                                           //Este otro bloque de codigo muestra si esa conexion fue desecha y se intenta validar nuevamente, es decir si se cambia la contraseña
                                           //en uso de la aplicacion entonces retornara este error (Lo mandara nuevamente al inicio de session)
                                            initLogOperator.postValue(UserLogin(false,null, false,false,null))
                                        }
                                    }
                            }
                        }
                    }
                    break
                }else{
                    Log.i(LOG_CLASS, "Sin internet")
                    initLogMessage.postValue("Sin acceso a internet")
                    delay(1000)
                    initLogOperator.postValue(UserLogin(false,null, false,false,null))
                    break
                }
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