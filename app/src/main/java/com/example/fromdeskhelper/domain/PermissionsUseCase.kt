package com.example.fromdeskhelper.domain

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fromdeskhelper.data.model.objects.Constrains
import javax.inject.Inject

class PermissionsUseCase @Inject constructor(){

    public fun AllPermisionGrantedCamera(Actictivy:Activity):Boolean{
        val dat = Constrains.REQUIERED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                Actictivy, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if(!dat){
            ActivityCompat.requestPermissions(
                Actictivy, Constrains.REQUIERED_PERMISSIONS,
                Constrains.REQUEST_CODE_PERMISSIONS
            )

        }

        return Constrains.REQUIERED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                Actictivy, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}