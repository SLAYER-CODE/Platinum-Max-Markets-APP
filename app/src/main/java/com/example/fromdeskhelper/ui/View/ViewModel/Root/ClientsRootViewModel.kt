package com.example.fromdeskhelper.ui.View.ViewModel.Root

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private var LOGVIEWMODEL="LOGGERCLIENTEP2P"
@HiltViewModel
class ClientsRootViewModel  @Inject constructor():
    AndroidViewModel(
        Application()
    ) {

}