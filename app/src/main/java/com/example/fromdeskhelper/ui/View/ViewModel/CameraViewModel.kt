package com.example.fromdeskhelper.ui.View.ViewModel

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application
import android.graphics.drawable.AnimationDrawable
import android.transition.AutoTransition
import android.transition.TransitionSet
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fromdeskhelper.data.PreferencesManager
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.domain.CameraUseCase
import com.example.fromdeskhelper.domain.PermissionsUseCase
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val getCameraUseCase: CameraUseCase,
    private val Permissions:PermissionsUseCase,
):
    AndroidViewModel(
    Application()
){
    //    Variables de inicialisacion para la camara y sus movimientos respectivos
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var imageCapture: ImageCapture;
    private var imageCap: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var CamStatus: CameraTypes = CameraTypes.NULL;
    private var CamClick: CameraTypes = CameraTypes.NULL;

    //    Variables de inicialisacion para el fragmento obteniendo el contexto de la base de la activdad

    private lateinit var MyCamera: Camera;
    private var PermisosCamera:Boolean=false;
    private lateinit var imageAnalisis: ImageAnalysis;
    private lateinit var AnimationUpCamera: ValueAnimator;

    // Variable iniciacidad de la camara
    private var toogleCamera: Boolean = false;

    //    Mas funciones Globales para la camara y sus movimientos
    private var autoTransition: TransitionSet = AutoTransition().setDuration(250);
    private var HeigthIncrement: Int = 0;
    private var recycleviewWidth: Int = -1;

    //Distancias para la elimiancion guardado y muestra
    private lateinit var AnimationUpIntercalate: AnimationDrawable;
    private var Corutine: Job? = null;

    //Variables que de igual
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    val CameraActivate:MutableLiveData<CameraTypes> = MutableLiveData();
    val ScanerStatus:MutableLiveData<Boolean> = MutableLiveData();


    //Binding conexion para la vista
    val CloseCamera: MutableLiveData<Boolean> = MutableLiveData();
    val CloseCameraChildren: MutableLiveData<Boolean> = MutableLiveData();
    val QRBindig: MutableLiveData<String> = MutableLiveData();

    fun CamaraStatus(type: CameraTypes,focus:Boolean){
        ScanerStatus.postValue(focus);
        CameraActivate.postValue(type);
    }
    val processCameraProvider:LiveData<ProcessCameraProvider>
        get() = getProcessCameraProvider

    fun GetPermisionsGranted(Activity:Activity):Boolean{
        return Permissions.AllPermisionGrantedCamera(Activity);
    }

    fun CloseInFragment(primero:Boolean){
        CloseCameraChildren.postValue(primero)
    }
    fun AgregateQR(String: String) {
        QRBindig.postValue(String)
    }
    fun  CloseCameraInto(pwd:Boolean){
        CloseCamera.postValue(pwd);
    }


    private val getProcessCameraProvider by lazy {
        MutableLiveData<ProcessCameraProvider>().apply {
            val cameraProviderFuture=ProcessCameraProvider.getInstance(getCameraUseCase.returnActivity())

            cameraProviderFuture.addListener(
                {
                    try{
                        value=cameraProviderFuture.get()
                    }catch (exc :ExecutionException){
                        throw IllegalStateException("failed to retrieve camera process",exc)
                    }catch (exc:InterruptedException){
                        throw IllegalStateException("failed to retrieve camera process",exc)
                    }
                },ContextCompat.getMainExecutor(getCameraUseCase.returnActivity())
            )
        }
    }


}