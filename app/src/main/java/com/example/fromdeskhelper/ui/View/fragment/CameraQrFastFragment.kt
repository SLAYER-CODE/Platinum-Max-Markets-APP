package com.example.fromdeskhelper.ui.View.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.data.model.QrCodeAnalyzer
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.data.model.objects.Constrains
import com.example.fromdeskhelper.databinding.FragmentCameraQrFastBinding
import com.example.fromdeskhelper.ui.View.ViewModel.AgregateProductViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.util.ConNet
import com.example.fromdeskhelper.util.ConnectToPost
import com.example.fromdeskhelper.util.MessageSnackBar
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [CameraQrFastFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CameraQrFastFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    private var _binding: FragmentCameraQrFastBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAnalisis: ImageAnalysis;
    private var Corutine: Job? = null;
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var CamStatus: CameraTypes = CameraTypes.NULL;
    private var CamClick: CameraTypes = CameraTypes.NULL;
    private var HeigthIncrement: Int = 0;
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private var PermisosCamera: Boolean = false;
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var MyCamera: Camera;


    private val AgregateProductsState: AgregateProductViewModel by viewModels(
        ownerProducer = {
            try {
                requireParentFragment()
            } catch (e: IllegalStateException) {
                requireActivity()
            }
        },
    );

    private val CameraView: CameraViewModel by viewModels(
        ownerProducer = {
            try {
                requireParentFragment()
            } catch (e: IllegalStateException) {
                requireActivity()
            }
        }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context

        }
        this.contextFragment = context
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraQrFastBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        if (PermisosCamera) {
            if (Constrains.REQUIERED_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        baseActivity, it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                startCameraEscaner(cameraProvider, true)
            } else {
                MessageSnackBar(view as View, "Se necesita acceso a la camara...", Color.RED)
                CameraView.CamaraStatus(CameraTypes.NULL, false)
                CameraView.CloseCameraChildren.postValue(true)
            }
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        HeigthIncrement = binding.LayoutCamera.width
        imageAnalisis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()


        binding.BRscanner.setOnClickListener {



//            binding.Vqrline.startAnimation(animation)
//            binding.BRscanner.visibility=View.INVISIBLE
//            if(Corutine!=null) {
//                Corutine!!.cancel()
//                Corutine=null;
//            }

            imageAnalisis.setAnalyzer(
                cameraExecutor, QrCodeAnalyzer(
                    { qrResult ->
                        imageAnalisis.clearAnalyzer()
                        binding.PVCmain.post {
//                                cameraProvider.unbindAll()
//                                startCamera()
//                            Log.d("QRCodeAnalyzer", "Barcode scanned: ${qrResult.text}")
//                            Toast.makeText(
//                                activity,
//                                "Codigo es ${qrResult.text}",
//                                Toast.LENGTH_SHORT
//                            ).show()

                            MessageSnackBar(
                                view,
                                "El codigo es ${qrResult.text}",
                                Color.YELLOW
                            )
                            //Setea parte arriba
//                    binding.TEQR.setText(qrResult.text)

                            AgregateProductsState.AgregateQR(qrResult.text)
                            CameraView.AgregateQR(qrResult.text)

//                    binding.Vqrline.clearAnimation()
//                    binding.Vqrline.visibility=View.INVISIBLE;

                            binding.BRscanner.visibility = View.VISIBLE
                            binding.borderqrxml.pauseAnimation()
                            binding.LAOK.playAnimation()

                            Corutine = GlobalScope.launch(Dispatchers.Main) {
                                val resultKeys = withContext(Dispatchers.IO) {
                                    ConnectToPost.ConnectAndGet(qrResult.text)
                                }
                            }
                        }
                    },
                    baseActivity.resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT
                )
            )

        }

        CameraView.CameraActivate.observe(viewLifecycleOwner, Observer {
            CamClick = CameraTypes.CAMERA;
        })

        CameraView.processCameraProvider.observe(viewLifecycleOwner, Observer {
            cameraProvider = it ?: return@Observer
            PermisosCamera = true;
            if (::cameraProvider.isInitialized && CameraView.GetPermisionsGranted(baseActivity)) {
                binding.LayoutCamera.visibility = View.VISIBLE;
                startCameraEscaner(cameraProvider, true)
            }
        })

//        CameraView.CloseCameraChildren
        CameraView.CloseCameraChildren.observe(viewLifecycleOwner, Observer {
            if (it) {
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


    private fun startCameraEscaner(cameraProvider: ProcessCameraProvider, analizer: Boolean) {

//        val animation = AnimationUtils.loadAnimation(
//            baseActivity,
//            R.anim.animation_lineqr
//        )
//        binding.Vqrline.startAnimation(animation)
//        binding.Vqrline.visibility=View.VISIBLE

        val preview = Preview.Builder().build().also { mPreview ->
            mPreview.setSurfaceProvider(
                binding.PVCmain.surfaceProvider
            )
        }

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        if (analizer) {
            imageAnalisis
                .apply {
                    setAnalyzer(
                        cameraExecutor, QrCodeAnalyzer(
                            { qrResult ->
                                clearAnalyzer()
                                binding.PVCmain.post {
                                    MessageSnackBar(
                                        view as View,
                                        "El codigo es ${qrResult.text}",
                                        Color.YELLOW
                                    )
                                    AgregateProductsState.AgregateQR(qrResult.text)
                                    CameraView.AgregateQR(qrResult.text)
//                                binding.Vqrline.clearAnimation()
//                                binding.Vqrline.visibility=View.INVISIBLE;
                                    binding.BRscanner.visibility = View.VISIBLE
                                    binding.borderqrxml.pauseAnimation()
                                    binding.LAOK.playAnimation()
//                            cameraProvider.unbindAll()
                                    if (ConNet.ComprobationInternet(baseActivity)) {
                                        baseActivity.binding.appBarMain.PBbarList?.visibility =
                                            View.VISIBLE
                                        Corutine = GlobalScope.launch(Dispatchers.Main) {
                                            val resultKeys = withContext(Dispatchers.IO) {
                                                ConnectToPost.ConnectAndGet(qrResult.text)
                                            }
                                            println(resultKeys)
                                        }

                                    } else {
                                        Toast.makeText(
                                            contextFragment,
                                            "Sin internet!!",
                                            Toast.LENGTH_SHORT
                                        ).show();
                                    }
//                                primero.cancel();
                                }

                            },
                            baseActivity.resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT
                        )
                    )

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
                    imageAnalisis
                )


//                cameraProvider.bindToLifecycle(baseActivity,cameraSelector,preview,imageAnalysis)
            MyCamera.cameraControl.cancelFocusAndMetering()
//            focusControllerCamera()
            CameraView.CorrectEnableCamera()
        } catch (e: Exception) {
            Log.e("CAMERA ERROR", "Error al intentar Asignar la camara de Escaneo", e)
        }
        binding.BZoom.setOnClickListener {
            MyCamera.cameraControl.setZoomRatio(200 / 100f)
        }
//        binding.SBzoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                // TODO Auto-generated method stub
//                if (fromUser) {
//                    MyCamera.cameraControl.setZoomRatio(progress.toFloat() / 100f)
//                }
//            }
//        })

        binding.BLinterna.setOnClickListener {
            MyCamera.apply {
                if (MyCamera.cameraInfo.hasFlashUnit()) {
                    MyCamera.cameraControl.enableTorch(binding.BLinterna.isChecked);
                }
            }
        }
    }
}