package com.example.fromdeskhelper.ui.View.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.data.model.QrCodeAnalyzer
import com.example.fromdeskhelper.data.model.objects.Constrains
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.databinding.FragmentCameraQrBinding
import com.example.fromdeskhelper.ui.View.ViewModel.AgregateProductViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.util.ConNet
import com.example.fromdeskhelper.util.ConnectToPost
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.afterMeasure
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val LOGFRAGMENT:String="CameraScanner"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraQrFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private var _binding: FragmentCameraQrBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAnalisis: ImageAnalysis;
    private var Corutine: Job? = null;
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var CamStatus: CameraTypes = CameraTypes.NULL;
    private var CamClick: CameraTypes = CameraTypes.NULL;
    private var HeigthIncrement: Int = 0;
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private var PermisosCamera:Boolean=false;
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var MyCamera: Camera;


    private val AgregateProductsState : AgregateProductViewModel by viewModels(
        ownerProducer = { requireActivity() }
    );

    private val CameraView: CameraViewModel by viewModels(
        ownerProducer =  {requireActivity()}
    )


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context

        }
        this.contextFragment = context
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
        _binding = FragmentCameraQrBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        Log.i(LOGFRAGMENT,"Resumiendo la actividad (MINIFRAGMENT)")
        println(PermisosCamera)
        println(CamClick)

        if(PermisosCamera) {
            if (Constrains.REQUIERED_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        baseActivity, it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {

                if(CamClick==CameraTypes.CAMERA){
                    binding.LayoutCamera.visibility = View.VISIBLE
                    startCameraEscaner(cameraProvider, true)
                }
                Log.i(LOGFRAGMENT, "Permisos de camera")

            }else{
                if(CamClick==CameraTypes.CAMERA){
                    binding.LayoutCamera.visibility = View.GONE
                }
                messageSnackBar(view as View,"Se necesita acceso a la camara...",Color.RED)
            }
//            PermisosCamera=true
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        HeigthIncrement = binding.LayoutCamera.width
        Log.i(LOGFRAGMENT,binding.LayoutCamera.layoutParams.height.toString())
        Log.i(LOGFRAGMENT,binding.LayoutCamera.layoutParams.width.toString())


        imageAnalisis=ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()


            binding.BRscanner.setOnClickListener {

            val animation = AnimationUtils.loadAnimation(
                baseActivity,
                com.example.fromdeskhelper.R.anim.animation_lineqr
            )

            binding.Vqrline.startAnimation(animation)
            binding.BRscanner.visibility=View.INVISIBLE
            if(Corutine!=null) {
                Corutine!!.cancel()
                Corutine=null;
            }

            imageAnalisis.setAnalyzer(cameraExecutor, QrCodeAnalyzer{ qrResult->
                imageAnalisis.clearAnalyzer()
                binding.PVCmain.post {
                    println("Se ejecuto!!")
//                                cameraProvider.unbindAll()
//                                startCamera()
//                            Log.d("QRCodeAnalyzer", "Barcode scanned: ${qrResult.text}")
//                            Toast.makeText(
//                                activity,
//                                "Codigo es ${qrResult.text}",
//                                Toast.LENGTH_SHORT
//                            ).show()

                    messageSnackBar(
                        view,
                        "El codigo es ${qrResult.text}",
                        Color.YELLOW
                    )
                    //Setea parte arriba
//                    binding.TEQR.setText(qrResult.text)

                    AgregateProductsState.AgregateQR(qrResult.text)

                    binding.Vqrline.clearAnimation()
                    binding.Vqrline.visibility=View.INVISIBLE;

                    binding.BRscanner.visibility = View.VISIBLE
                    Corutine = GlobalScope.launch(Dispatchers.Main) {
                        val resultKeys = withContext(Dispatchers.IO) {
                            ConnectToPost.ConnectAndGet(qrResult.text)
                        }
                        println(resultKeys)
                    }
                }
            })
        }

        CameraView.CameraActivate.observe(viewLifecycleOwner, Observer {
            CamClick=CameraTypes.CAMERA;
            Log.e(LOGFRAGMENT,"Se llamo a la actividad de camara!!")
        })

        CameraView.processCameraProvider.observe(viewLifecycleOwner, Observer {
            cameraProvider = it ?: return@Observer
            PermisosCamera=true;
            if(::cameraProvider.isInitialized && CameraView.GetPermisionsGranted(baseActivity)){
                binding.LayoutCamera.visibility=View.VISIBLE;
                startCameraEscaner(cameraProvider,true)
            }
        })
//        CameraView.CloseCameraChildren
        CameraView.CloseCameraChildren.observe(viewLifecycleOwner, Observer {
            if(it) {
                Log.i("CARLOS", "SE ACTIVO")
                Log.i("CARLOS", it.toString())
                CamStatus = CameraTypes.NULL
                CamClick = CameraTypes.NULL
//                binding.LayoutCamera.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                binding.LayoutCamera.visibility = View.GONE;
                cameraProvider.unbindAll()
//                binding.LayoutCamera.requestLayout()
            }
//                baseActivity.finish()

        //            CameraView.CloseCameraChildren.removeObservers(this)
//            }else{
//                CameraView.retarder()
//            }
        })

        super.onViewCreated(view, savedInstanceState)
    }


    private fun startCameraEscaner(cameraProvider: ProcessCameraProvider,analizer:Boolean){

        val animation = AnimationUtils.loadAnimation(
            baseActivity,
            R.anim.animation_lineqr
        )

        binding.Vqrline.startAnimation(animation)
        binding.Vqrline.visibility=View.VISIBLE

        val preview = Preview.Builder().build().also { mPreview ->
            mPreview.setSurfaceProvider(
                binding.PVCmain.surfaceProvider
            )
        }

        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        Log.i(LOGFRAGMENT,"El dato es"+binding.PVCmain.width.toString())
            if(analizer) {
                imageAnalisis
                    .apply {
                        setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrResult ->

                            clearAnalyzer()
                            binding.PVCmain.post {
                                messageSnackBar(
                                    view as View,
                                    "El codigo es ${qrResult.text}",
                                    Color.YELLOW
                                )
                                AgregateProductsState.AgregateQR(qrResult.text)

                                binding.Vqrline.clearAnimation()
                                binding.Vqrline.visibility=View.INVISIBLE;
                                binding.BRscanner.visibility = View.VISIBLE
//                            cameraProvider.unbindAll()
                                if(ConNet.ComprobationInternet(baseActivity)) {
                                    baseActivity.returnbinding().appBarMain.PBbarList.visibility=View.VISIBLE
                                    Corutine = GlobalScope.launch(Dispatchers.Main) {
                                        val resultKeys = withContext(Dispatchers.IO) {
                                            ConnectToPost.ConnectAndGet(qrResult.text)
                                        }
                                        println(resultKeys)
                                    }

                                }else{
                                    Toast.makeText(contextFragment,"Sin internet!!", Toast.LENGTH_SHORT).show();
                                }
//                                primero.cancel();
                            }

                        })

                    }
            }


//            preview.targetRotation= ROTATION_9

            try {

                cameraProvider.unbindAll()
                MyCamera =
                    cameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalisis)


//                cameraProvider.bindToLifecycle(baseActivity,cameraSelector,preview,imageAnalysis)
                MyCamera.cameraControl.cancelFocusAndMetering()

                focusControllerCamera()
            } catch (e: Exception) {
                Log.e(LOGFRAGMENT, "Error al intentar Asignar la camara de Escaneo",e)
            }
        Log.i(LOGFRAGMENT,binding.LayoutCamera.layoutParams.height.toString())
        Log.i(LOGFRAGMENT,binding.LayoutCamera.layoutParams.width.toString())



        binding.SBzoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // TODO Auto-generated method stub
                if(fromUser) {
                    MyCamera.cameraControl.setZoomRatio(progress.toFloat()/100f)
                }
            }
        })

        binding.BLinterna.setOnClickListener {
            MyCamera.apply {
                if(MyCamera.cameraInfo.hasFlashUnit()){
                    MyCamera.cameraControl.enableTorch(binding.BLinterna.isChecked);
                }
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun focusControllerCamera() {
        binding.SBzoom.max= (MyCamera.cameraInfo.zoomState.value?.maxZoomRatio!! * 100).toInt()
        binding.SBzoom.progress=(MyCamera.cameraInfo.zoomState.value?.zoomRatio!! * 100).toInt()
        binding.SBzoom.min= (MyCamera.cameraInfo.zoomState.value?.minZoomRatio!! * 100).toInt()
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Get the camera's current zoom ratio
                val currentZoomRatio = MyCamera.cameraInfo.zoomState.value?.zoomRatio!!

                // Get the pinch gesture's scaling factor
                val delta = detector.scaleFactor

                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                MyCamera.cameraControl.setZoomRatio(currentZoomRatio * delta)
                binding.SBzoom.progress = (currentZoomRatio*100).toInt()
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

            binding.PVCmain.setOnTouchListener { _:View, event ->
                scaleGestureDetector.onTouchEvent(event)

                when (event.action) {

                    MotionEvent.ACTION_MOVE->{
                        binding.punteroFocus.visibility=View.INVISIBLE
                        true;
                    }
                    MotionEvent.ACTION_POINTER_UP->{
                        println("SSS")
                        true
                    }
                    MotionEvent.ACTION_DOWN -> {
//                        println(event.x)
//                        println(event.y)
//                        println(binding.punteroFocus.x)

                        binding.punteroFocus.x=event.x-(binding.punteroFocus.height / 2)
                        binding.punteroFocus.y=event.y-(binding.punteroFocus.width / 2)
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
                        if(newpressanimation.isRunning){
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
                        Log.d(LOGFRAGMENT,  binding.PVCmain.width.toFloat().toString())

                        Log.d(LOGFRAGMENT,  binding.PVCmain.height.toFloat().toString())
                        Log.d(LOGFRAGMENT,  binding.PVCmain.layoutParams.height.toFloat().toString())
                        Log.d(LOGFRAGMENT,  binding.PVCmain.layoutParams.width.toFloat().toString())
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
                        if(binding.punteroFocus.isVisible){
                            binding.punteroFocus.visibility=View.INVISIBLE
                        }
                        binding.punteroFocus.requestLayout()
                        false
                    }// Unhandled event.

                }

            }

        }
    }

    fun messageSnackBar(view: View, text: String, color: Int) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)

        snackbar.setActionTextColor(color)
        val snackbarView = snackbar.view

        snackbarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(color)
        textView.textSize = 17f
        (snackbar.view).layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraQrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraQrFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}