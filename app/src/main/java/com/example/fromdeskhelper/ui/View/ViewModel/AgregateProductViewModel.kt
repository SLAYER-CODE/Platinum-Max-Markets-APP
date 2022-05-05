package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AgregateProductViewModel @Inject constructor():
    AndroidViewModel(
    Application()
) {
    val CameraActivate:MutableLiveData<CameraTypes> = MutableLiveData();
    val ScanerStatus:MutableLiveData<Boolean> = MutableLiveData();
    val QRBindig:MutableLiveData<String> = MutableLiveData();

    val ClearFragment : MutableLiveData<Boolean> = MutableLiveData()
    val RecivImageItem: MutableLiveData<String> = MutableLiveData();

    val data: LiveData<MutableList<Uri>>
        get() = _data

    private val _data = MutableLiveData<MutableList<Uri>>(mutableListOf())
    private val list= mutableListOf<Uri>()
    init {
        loadData()
    }

    private fun loadData() {
        // This is a coroutine scope with the lifecycle of the ViewModel
        Log.i("VIEWMODEL SELLAMO","Se envio el primer cargo")
        viewModelScope.launch {
            _data.postValue(list)
        }

    }


    fun sendImageCapture(toUri:String){

        Log.i("VIEWMODEL SELLAMO","Sellamo el viewmodel"+list.toString())
        list.add(Uri.parse(toUri));
        _data.postValue(list)
//        RecivImageItem.postValue(toUri)
    }

    fun CamaraStatus(boolean: CameraTypes,focus:Boolean){
        ScanerStatus.postValue(focus);
        CameraActivate.postValue(boolean);
    }
    fun AgregateQR(String:String){
        QRBindig.postValue(String)
    }


    override fun onCleared() {
        Log.i("VIEWMODEL SELLAMO","se destruyo la vista")
        ClearFragment.postValue(true)
        super.onCleared()
    }
}
