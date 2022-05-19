package com.example.fromdeskhelper.ui.View.fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionSet
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.*
import android.widget.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.afterMeasure
import com.example.fromdeskhelper.data.model.objects.Constrains
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.databinding.FragmentCameraBinding
import com.example.fromdeskhelper.ui.View.ViewModel.AgregateProductViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.hideKeyboardFrom
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import java.lang.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val LOGFRAGMENT: String = "CameraFotos"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CameraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    //    Variables de inicialisacion para la camara y sus movimientos respectivos
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var imageCapture: ImageCapture;
    private var imageCap: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var CamStatus: CameraTypes = CameraTypes.NULL;
    private var CamClick: CameraTypes = CameraTypes.NULL;

    //    Variables de inicialisacion para el fragmento obteniendo el contexto de la base de la activdad

    private lateinit var MyCamera: Camera;
    private var PermisosCamera: Boolean = false;
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

    private var res: Boolean = false;

    //Instancias de View Model
    private val CameraView: CameraViewModel by viewModels(
        ownerProducer = {  requireParentFragment() }
    )
    private val AgregateProductsState : AgregateProductViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    );


//    private val CameraView:CameraViewModel by navGraphViewModels(R.id.nav_camera_functions)


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            this.baseActivity = context

        }
        this.contextFragment = context
    }

    override fun onResume() {
        Log.i(LOGFRAGMENT, "Resumiendo el ChildrenFragment "+PermisosCamera.toString())
        if (PermisosCamera) {
            if (Constrains.REQUIERED_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        baseActivity, it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {

                if (CamClick == CameraTypes.CAMERA) {
                    binding.LayoutCamera.visibility = View.VISIBLE
                        startCamera(cameraProvider)
                }
                Log.i(LOGFRAGMENT, "Permisos de camera")

            } else {
                if (CamClick == CameraTypes.CAMERA) {
                    binding.LayoutCamera.visibility = View.GONE
                }
                MessageSnackBar(view as View, "Se necesita acceso a la camara...", Color.RED)
                CameraView.CloseInFragment(true)
                CameraView.CamaraStatus(CameraTypes.NULL,false)
            }
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        Log.i(LOGFRAGMENT, "La camara es $toogleCamera")
        AnimationUpCamera = ObjectAnimator.ofFloat(binding.despliegeCamera, "translationY", -80f)
        AnimationUpCamera.repeatCount = ObjectAnimator.INFINITE
        AnimationUpCamera.repeatMode = ObjectAnimator.REVERSE
        AnimationUpCamera.duration = 800

        AnimationUpIntercalate = binding.despliegeCamera.background as AnimationDrawable
        AnimationUpIntercalate.setEnterFadeDuration(1000)
        AnimationUpIntercalate.setExitFadeDuration(1000)
        AnimationUpIntercalate.start()
        AnimationUpCamera.start()


//        Estas son las bases para el incremento de la camara segun la perspectiva del telefono
        HeigthIncrement = binding.LayoutCamera.layoutParams.height
//        if (recycleviewWidth == -1){
//            recycleviewWidth = binding.RVCaptureImages.layoutParams.width
//        }


        val FocusEditListener = View.OnFocusChangeListener { p0, p1 ->
            if (!p1) {
                hideKeyboardFrom(requireContext(), view)
            }
        }



        CameraView.processCameraProvider.observe(viewLifecycleOwner, Observer {
            cameraProvider = it ?: return@Observer
            PermisosCamera = true;
            if (::cameraProvider.isInitialized && CameraView.GetPermisionsGranted(baseActivity)) {
                startCamera(cameraProvider)
                binding.LayoutCamera.visibility = View.VISIBLE;
            }
        })


        CameraView.CameraActivate.observe(viewLifecycleOwner, Observer {
//            toogleCamera=it;

            CamClick = CameraTypes.CAMERA;
            Log.e(LOGFRAGMENT, "Se llamo a la actividad de camara!!")
        })

        //(FINAL) MANEGAR LOS ESTILOS DE RECORRIDO
        binding.despliegeCamera.setOnClickListener {
//            baseActivity.returnbinding().PBbarList.visibility=View.VISIBLE
//            CamStatus = CameraTypes.NULL
//            CamClick = CameraTypes.NULL
//            binding.LayoutCamera.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
//
//            binding.LayoutCamera.visibility = View.GONE;
//            cameraProvider.unbindAll()
//
//            binding.LayoutCamera.requestLayout()

            CameraView.CloseInFragment(true)
            CameraView.CamaraStatus(CameraTypes.NULL,false)
        }


        CameraView.CloseCameraChildren.observe(viewLifecycleOwner, Observer {
//            Log.i("CARLOS","SE LLAMO AL MINIFRAGMENTO "+it.toString())
            if(it) {
                Log.i("CARLOS", "MINIFRAGMENTO"+it.toString())
                CamStatus = CameraTypes.NULL
                CamClick = CameraTypes.NULL
                binding.LayoutCamera.visibility = View.GONE;
                cameraProvider.unbindAll()
            }
        })



        var contadorImagen = 1
        binding.BcaptureImage.setOnClickListener {

            val imageFile = File.createTempFile("anImage", ".jpg")
            binding.BcaptureImage.animate().rotation(60F * (contadorImagen++)).setDuration(400)
                .start()

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
            imageCapture.takePicture(outputFileOptions, cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {

                    override fun onError(error: ImageCaptureException) {
                        // insert your code here.
//                            baseActivity.runOnUiThread {
//                            Toast.makeText(context,"Ubo un error intentelo denuevo ${error}",Toast.LENGTH_SHORT).show()
//                            }

                        Log.e(LOGFRAGMENT, "Ubo un error $error")
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                        Api de envio para el parent de la imagen Capturada()

//                            baseActivity.runOnUiThread {
                        AgregateProductsState.sendImageCapture(outputFileResults.savedUri)
//                            adapter.addImage(outputFileResults.savedUri.toString())
//                            binding.RVCaptureImages.scrollToPosition(0);
//                            comprobateItem()
////                                cameraProvider.unbindAll()
//                        }
                    }
                })


        }
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }




        binding.SBzoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    MyCamera.cameraControl.setZoomRatio(progress.toFloat() / 100f)
                }
            }
        })


        binding.BLinterna.setOnClickListener {
            MyCamera.apply {
                if (MyCamera.cameraInfo.hasFlashUnit()) {
                    MyCamera.cameraControl.enableTorch(binding.BLinterna.isChecked);
                }
            }
        }

    }


    private fun comprobateItem() {

    }


    private fun startCamera(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().also { mPreview ->
            mPreview.setSurfaceProvider(
                binding.PVCmain.surfaceProvider
            )
        }

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()
            //ImageCapture
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(
                    Size(
                        binding.LayoutCamera.layoutParams.height,
                        binding.LayoutCamera.layoutParams.width
                    )
                )
                .setFlashMode(ImageCapture.FLASH_MODE_ON)
                .build()
            //capture
            imageCapture.setCropAspectRatio(Rational(200, 200))
            // Bind use cases to camera
            MyCamera = cameraProvider.bindToLifecycle(
                baseActivity,
                cameraSelector,
                preview,
                imageCapture
            )

//            MyCamera.cameraControl.enableTorch(true)
            MyCamera.cameraControl.cancelFocusAndMetering()
            focusControllerCamera()


//            preview?.setSurfaceProvider(binding.PVCmain.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(LOGFRAGMENT, "Error al asignar la camara", exc)
        }
    }

//    fun messageSnackBar(view: View, text: String, color: Int) {
//        val snackbar = Snackbar.make(
//            view, text,
//            Snackbar.LENGTH_LONG
//        ).setAction("Action", null)
//
//        snackbar.setActionTextColor(color)
//        val snackbarView = snackbar.view
//
//        snackbarView.setBackgroundColor(Color.BLACK)
//        val textView =
//            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//        textView.setTextColor(color)
//        textView.textSize = 17f
//        (snackbar.view).layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        snackbar.show()
//    }


    private fun allPermisionGranted(): Boolean {
        val dat = Constrains.REQUIERED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseActivity, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (!dat) {
            ActivityCompat.requestPermissions(
                baseActivity, Constrains.REQUIERED_PERMISSIONS,
                Constrains.REQUEST_CODE_PERMISSIONS
            )

        }

        return Constrains.REQUIERED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseActivity, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun focusControllerCamera() {
        binding.SBzoom.max = (MyCamera.cameraInfo.zoomState.value?.maxZoomRatio!! * 100).toInt()
        binding.SBzoom.progress = (MyCamera.cameraInfo.zoomState.value?.zoomRatio!! * 100).toInt()
        binding.SBzoom.min = (MyCamera.cameraInfo.zoomState.value?.minZoomRatio!! * 100).toInt()
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Get the camera's current zoom ratio
                val currentZoomRatio = MyCamera.cameraInfo.zoomState.value?.zoomRatio!!

                // Get the pinch gesture's scaling factor
                val delta = detector.scaleFactor

                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                MyCamera.cameraControl.setZoomRatio(currentZoomRatio * delta)
                binding.SBzoom.progress = (currentZoomRatio * 100).toInt()
                // Return true, as the event was handled
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return super.onScaleBegin(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                super.onScaleEnd(detector)
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(context, listener)

        binding.PVCmain.afterMeasure {
            val newpressanimation: AnimationDrawable =
                binding.punteroFocus.background as AnimationDrawable

            newpressanimation.setEnterFadeDuration(0)
            newpressanimation.setExitFadeDuration(500)

            binding.PVCmain.setOnTouchListener { _: View, event ->
                scaleGestureDetector.onTouchEvent(event)

                when (event.action) {

                    MotionEvent.ACTION_MOVE -> {
                        binding.punteroFocus.visibility = View.INVISIBLE
                        true;
                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                        println("SSS")
                        true
                    }
                    MotionEvent.ACTION_DOWN -> {
//                        println(event.x)
//                        println(event.y)
//                        println(binding.punteroFocus.x)

                        binding.punteroFocus.x = event.x - (binding.punteroFocus.height / 2)
                        binding.punteroFocus.y = event.y - (binding.punteroFocus.width / 2)
//                        Log.i(LOGFRAGMENT,"${event.x}   ${event.y} ${event.rawX} ${event.rawY}")
//
//                        val relativeParams =
//                            binding.punteroFocus.layoutParams as ConstraintLayout.LayoutParams
//                        relativeParams.setMargins(
//                            event.x.toInt() - (binding.punteroFocus.height / 2),
//                            event.y.toInt() - (binding.punteroFocus.width / 2),
//                            0,
//                            0
//                        )


//                        binding.punteroFocus.layoutParams = relativeParams
                        if (newpressanimation.isRunning) {
                            newpressanimation.stop()
                        }
                        binding.punteroFocus.visibility = View.VISIBLE
//                        newpressanimation.start()

//                        newpressanimation.stop()

                        //                                val trancicion  =


//                                binding.punteroFocus.animate().alpha(1F).setDuration(3000).setListener(object :AnimatorListenerAdapter(){
//                                    override fun onAnimationStart(animation: Animator?) {
//                                        newpressanimation.start()
//                                        binding.punteroFocus.visibility = View.VISIBLE
////                                        super.onAnimationEnd(animation)
//                                    }
//                                })
////
//                                binding.punteroFocus.animate().alpha(0F).setDuration(3000).setListener(object :AnimatorListenerAdapter(){
//                                    override fun onAnimationEnd(animation: Animator) {
//                                        newpressanimation.stop()
//                                        binding.punteroFocus.visibility = View.INVISIBLE
//                                    }
//                                })

                        true
                    }
                    MotionEvent.ACTION_UP -> {

                        val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            binding.PVCmain.width.toFloat(), binding.PVCmain.height.toFloat()
                        )
                        val autoFocusPoint = factory.createPoint(event.x, event.y)
                        try {
                            newpressanimation.start()
                            MyCamera.cameraControl.startFocusAndMetering(
                                FocusMeteringAction.Builder(
                                    autoFocusPoint,
                                    FocusMeteringAction.FLAG_AF
                                ).apply {
                                    //focus only when the user tap the preview
                                    binding.punteroFocus.postDelayed(Runnable {
                                        binding.punteroFocus.setVisibility(
                                            View.INVISIBLE
                                        )
                                        newpressanimation.stop()
                                    }, 500)
                                    disableAutoCancel()
                                }.build()
                            )


                        } catch (e: CameraInfoUnavailableException) {
                            newpressanimation.stop()
                            Log.d("ERROR", "cannot access camera", e)
                        }
                        true
                    }
                    else -> {
                        if (binding.punteroFocus.isVisible) {
                            binding.punteroFocus.visibility = View.INVISIBLE
                        }
                        binding.punteroFocus.requestLayout()
                        false
                    }// Unhandled event.

                }

            }

        }
    }

    override fun onPause() {
        Log.i("CameraStateRegistry", "Pauseando la acitividad")

        //LOGGER PARA APAGAR LA CAMARA
//        Log.i("CameraStateRegistry",cameraExecutor.isTerminated.toString())
        Log.i(LOGFRAGMENT, "Se pause onPause [*]")
        Log.i("CameraStateRegistry", cameraExecutor.isTerminated.toString())

        //ESTO TIENE QUE IR DENTRO DE LA CAMARA
        if (CamStatus != CameraTypes.NULL) {
            cameraProvider.unbindAll()
        }
        super.onPause()
    }


    override fun onDestroy() {


        if (CamStatus != CameraTypes.NULL) {
            Log.i("CameraStateRegistry", "Se cerro la camara al destruir la vista [*]")
//            cameraProvider.shutdown()
//            cameraProvider.unbindAll()
        }
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}