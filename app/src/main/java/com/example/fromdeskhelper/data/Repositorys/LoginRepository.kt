package com.example.fromdeskhelper.data.Repositorys

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.data.LocalNetwork.LoginDataSource
import com.example.fromdeskhelper.data.Result
import com.example.fromdeskhelper.data.model.LoggedInUser
import java.lang.Exception
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(
    val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }


    fun login(username: String, password: String,action: MutableLiveData<Result<LoggedInUser>>,actionError: MutableLiveData<Result<Exception>>, ) {
        // handle login
        dataSource.login(username, password,action,actionError)
//        action.observe(, Observer {  })
//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}