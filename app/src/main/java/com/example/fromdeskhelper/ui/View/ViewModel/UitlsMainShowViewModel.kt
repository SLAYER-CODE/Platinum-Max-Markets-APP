package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.core.AuthorizationInterceptor
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.WifiModel
import com.example.fromdeskhelper.domain.ComprobationUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UitlsMainShowViewModel @Inject constructor() : AndroidViewModel(Application()) {
    val DesingItemListVIew:MutableLiveData<Int>  = MutableLiveData<Int>()

    val ItemFilterView:MutableLiveData<String?>  = MutableLiveData<String?>()
    val RequestLayoutItem:MutableLiveData<Boolean>  = MutableLiveData()

    fun NormalDesingLayout(){
        Log.i("NormalDesingLayout","")
        DesingItemListVIew.postValue(0)
    }
    fun ListViewDesingLayout(){
        Log.i("ListViewDesingLayout","")
        DesingItemListVIew.postValue(1)
    }
    fun GirdLayout(){
        Log.i("GirdLayout","")
        DesingItemListVIew.postValue(2)
    }

    fun GirdLayoutListView(){
        Log.i("GirdLayout","")
        DesingItemListVIew.postValue(3)
    }

    fun SendFilterLister(characters:String?){
        viewModelScope.launch {
            ItemFilterView.postValue(characters)
        }
    }
    fun RequestLayout(){
        viewModelScope.launch {
            RequestLayoutItem.postValue(true)
        }
    }
}