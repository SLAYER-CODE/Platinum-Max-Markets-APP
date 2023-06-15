package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.*
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.data.Result

import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.Providers
import com.example.fromdeskhelper.data.model.LoggedInUser
import com.example.fromdeskhelper.ui.login.*
import com.facebook.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

var LOG_PRESENATION="VIEWINITMODEL"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val loginPreferences: PreferencesManager,
    private val authorizationInterceptor: AuthorizationInterceptor
) : AndroidViewModel(Application()) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    private val _registerResult = MutableLiveData<RegisterResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    val registerResult: LiveData<RegisterResult> = _registerResult
    val registerSusessfull: MutableLiveData<Boolean> = MutableLiveData();

    val erroremailregister: MutableLiveData<String> = MutableLiveData();
    val errorDesconosido: MutableLiveData<Exception> = MutableLiveData();

    val usuarioLoginProvider: MutableLiveData<Result<LoggedInUser>> = MutableLiveData();
    val usuarioLoginProviderError: MutableLiveData<Result<Exception>> = MutableLiveData();

    val UserPreferences = loginPreferences.preferencesUserFlow

    val initLogin = MutableLiveData<Boolean>()

    operator fun invoke(){
        Log.i(LOG_PRESENATION,"Se esta cambiando la presentacion")
        viewModelScope.launch {
            loginPreferences.changePresentation(false)
        }
    }
    suspend fun initFirebaseToken(){
        FirebaseAuth.getInstance().getAccessToken(true).addOnCompleteListener { token ->
            authorizationInterceptor.setSessionToken(token.result.token.toString())
            initLogin.postValue(true)
        }
    }


    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password,usuarioLoginProvider,usuarioLoginProviderError)
//        if (result is Result.Success) {
//            _loginResult.value =
//                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
    }


    fun register(username: String, password: String) {
//        val result = loginRepository.login(username, password)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username,password).addOnCompleteListener {
            if(it.isSuccessful){
                registerSusessfull.postValue(true)
//                Toast.makeText(context,"Se reguistro sin ningun Problema!", Toast.LENGTH_LONG).show()
//                AppMainInitApp(it.result.user?.email.toString() , Providers.BASIC,it.result.user)

            }else{
                registerSusessfull.postValue(false)
                try {
                    throw it.exception?.cause!!
                }catch (e: FirebaseAuthUserCollisionException){
                    erroremailregister.postValue(e.email)
//                    showAlert("El email ya esta registrado!")
                }catch (e: Exception){
                    errorDesconosido.postValue(e)
//                    showAlert("Ubo un error de registro Intente Nuevamente")
                }
//              Toast.makeText(context,"Ubo un error de reguistro Iniciar denuevo",Toast.LENGTH_SHORT).show()
            }

        }


//        if (result is Result.Success) {
//            _registerResult.value =
//                RegisterResult(success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _registerResult.value = RegisterResult(error = "Error a la hora de Reguistrar")
//        }


    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }


    fun loginDataChangedRegister(username: String, password: String,repetPasswrd:String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
        if(isPasswordRepet(repetPasswrd,password)) {
            _registerForm.value = RegisterFormState(repetError = R.string.invalid_passwordRepet)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }

    private fun isPasswordRepet(repet:String,password: String): Boolean {
        return repet!=password
    }
}