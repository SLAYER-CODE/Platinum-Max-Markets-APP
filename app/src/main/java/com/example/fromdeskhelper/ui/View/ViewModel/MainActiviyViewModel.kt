package com.example.fromdeskhelper.ui.View.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActiviyViewModel @Inject constructor(
    private val PreferencesM: PreferencesManager,

    ): AndroidViewModel(Application()) {
    val OpenCamera: MutableLiveData<PositionsValues> = MutableLiveData();

    fun SavePointMoveCamera(x:Float,y:Float){
        viewModelScope.launch {
            PreferencesM.changeMoveCamera(x,y)
        }
    }

    fun SavePointScaleCamera(x:Float,y:Float){
        viewModelScope.launch {
            PreferencesM.changeScalaCamera(x,y)
        }
    }

    fun RestoreCamera(){
        Log.i("MAINLOGGER","SE LLAMO A RESTORE")
        viewModelScope.launch {
            var Values = PreferencesM.preferencesFlowCamera.first()
            OpenCamera.postValue(PositionsValues(Values.MoveX,Values.MoveY,Values.ScaleX,Values.ScaleY))
        }
    }
}
data class PositionsValues(
    var movex:Float,
    var movey:Float,
    var scalex:Float,
    var scaley: Float
)