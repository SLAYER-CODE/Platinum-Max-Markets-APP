package com.example.fromdeskhelper.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fromdeskhelper.data.LocalNetwork.LoginDataSource
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.Repositorys.LoginRepository
import com.example.fromdeskhelper.ui.View.ViewModel.LoginViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
//
//class LoginViewModelFactory : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//            return LoginViewModel(
//                loginRepository = LoginRepository(
//                    dataSource = LoginDataSource()
//                ), loginPreferences = PreferencesManager()
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}