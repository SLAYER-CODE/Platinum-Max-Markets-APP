package com.example.fromdeskhelper.ui.View.ViewModel.Root

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


private val LOGVIEWMODEL="LOGGNOTIFICATION"
@HiltViewModel
class NotificationsViewModel @Inject constructor():
    AndroidViewModel(
        Application()
    ) {

}