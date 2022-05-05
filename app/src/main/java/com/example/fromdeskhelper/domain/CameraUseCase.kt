package com.example.fromdeskhelper.domain

import android.content.Context
import javax.inject.Inject

class CameraUseCase @Inject constructor(protected val baseActivity:Context) {
    //suspend operator fun invoke() : List<WifiModel>?{
    //    return repository.getAllP2p();
    //}


    fun returnActivity():Context{
        return baseActivity;
    }
    suspend operator fun invoke(){

    }
}