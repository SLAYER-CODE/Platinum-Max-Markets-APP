package com.example.fromdeskhelper.ui.View.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
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
import com.example.fromdeskhelper.databinding.FragmentCameraBaseQrBinding
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
 * Use the [CameraBaseQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CameraBaseQrFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    private var _binding: FragmentCameraBaseQrBinding? = null
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
//            try {
//                requireParentFragment()
//            } catch (e: IllegalStateException) {
                requireActivity()
//            }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBaseQrBinding.inflate(inflater, container, false)
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
//                CameraView.CamaraStatus(CameraTypes.NULL, false)
//                CameraView.CloseCameraChildren.postValue(true)
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
//            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()



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
                CamStatus = CameraTypes.NULL
                CamClick = CameraTypes.NULL
                binding.LayoutCamera.visibility = View.GONE;
                cameraProvider.unbindAll()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }


    private fun startCameraEscaner(cameraProvider: ProcessCameraProvider, analizer: Boolean) {
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
                                binding.PVCmain.post {
                                    MessageSnackBar(
                                        view as View,
                                        "Capturado... ${qrResult.text}",
                                        Color.YELLOW
                                    )
                                    AgregateProductsState.AgregateQR(qrResult.text)
                                    CameraView.AgregateQR(qrResult.text)
//                                    binding.borderqrxml.pauseAnimation()
//                                    binding.BRscanner.visibility = View.VISIBLE
//                                    binding.BRscannerAnimation.playAnimation()
                                    if (ConNet.ComprobationInternet(baseActivity)) {
//                                        baseActivity.binding.appBarMain.PBbarList?.visibility =
//                                            View.VISIBLE
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
                                }
                            },
                            baseActivity.resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT
                        )
                    )
                }
        }
        try {

            cameraProvider.unbindAll()
            MyCamera =
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalisis
                )

            MyCamera.cameraControl.cancelFocusAndMetering()
//            focusControllerCamera()
//            CameraView.CorrectEnableCamera()
        } catch (e: Exception) {
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
    }
}