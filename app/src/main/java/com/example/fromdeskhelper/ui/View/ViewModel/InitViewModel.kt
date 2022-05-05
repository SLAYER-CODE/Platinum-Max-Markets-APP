package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.domain.GetPermisionsUseCase
import com.example.fromdeskhelper.ui.login.RegisterResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private val LOGVIEWMODEL="ViewModelInit"
@HiltViewModel
class InitViewModel @Inject constructor(
    private val loginPreferences: PreferencesManager,
    private val comprobateUseCase:GetPermisionsUseCase
) : AndroidViewModel(Application()) {
//):ViewModel(){
    val UserPreferences = loginPreferences.preferencesUserFlow
    private val _registerResult = MutableLiveData<Boolean>()
    val registerresult = MutableLiveData<Boolean>()
    val UserAutenticate : LiveData<Boolean> = _registerResult;
    val initLog = MutableLiveData<Boolean>()
//    val UserComprobateLogin : LiveData<Boolean> = _registerResult;

    operator fun invoke() {
        viewModelScope.launch {
            UserPreferences.collect { it ->
                if(it.loginInit){
                    registerresult.postValue(true)
                }
            }
        }
    }

    suspend fun ComprobateInit( tokenUI:String){
        viewModelScope.launch {
            if(comprobateUseCase(tokenUI) == Privilegies.NULL){
                Log.i(LOGVIEWMODEL,"EL usuario es NULL")
            }
        }
    }
}