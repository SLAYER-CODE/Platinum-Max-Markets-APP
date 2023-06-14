package com.example.fromdeskhelper.data.LocalNetwork

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.fromdeskhelper.data.Result
import com.example.fromdeskhelper.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */


class LoginDataSource @Inject constructor(){
    fun login(username: String, password: String,action:  MutableLiveData<Result<LoggedInUser>>,actionError: MutableLiveData<Result<Exception>>) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
//                    Toast.makeText(context,"Se Inicio sin ningun problema!", Toast.LENGTH_LONG).show()
//                    AppMainInitApp(it.result.user?.email.toString() , Providers.BASIC,it.result.user)
                    action.postValue(Result.Success(LoggedInUser(
                        it.result.user!!.uid,
                        it.result.user?.displayName ?: "Sin Name"
                    )))

                    //Verificando si inicia session en el servidor


                } else {
//                    try {
//                        throw (it.exception as FirebaseAuthException)
//                    }catch (e: FirebaseAuthInvalidCredentialsException){
//                        showAlert("Contraseña Incorrecta!")
//                    }catch (e: FirebaseAuthInvalidUserException){
//                        binding.TEerror.visibility = View.VISIBLE
//                    }catch (e: FirebaseAuthException){
//                        showAlert("Ubo un error " + it.exception?.cause!!.hashCode().toString())
//                    }
                    actionError.postValue(Result.Error(it.exception!!) )
                }


//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
//            return Result.Success(fakeUser)

            }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}