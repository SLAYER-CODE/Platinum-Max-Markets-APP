package com.example.finalproductos

import Adapters.ImageAdapter
import Data.AppDatabase
import Data.ImagenesNew
import Data.Producto
import Data.CameraTypes
import Images.imagenController
import QrCodeAnalyzer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.finalproductos.databinding.FragmentAgregateProductsBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.animation.ObjectAnimator
import android.animation.ValueAnimator

import android.view.animation.ScaleAnimation

//import android.R
//import androidx.vectordrawable.R

import android.app.Activity
import android.content.ClipData
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.TextWatcher
import android.transition.*
import android.util.Log
import android.util.Size
import android.view.*
import android.view.Surface.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import kotlinx.android.synthetic.main.fragment_agregate_products.view.*
//import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Rational
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_first.*
import java.io.ByteArrayOutputStream


inline fun View.afterMeasure(crossinline block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregateProducts.newInstance] factory method to
 * create an instance of this fragment.
 */

private var LOGFRAGMENT:String="AddProducto"
class ResizeAnimation(
    private val view: View,
    private val toHeight: Float,
    private val fromHeight: Float,
    private val toWidth: Float,
    private val fromWidth: Float,
    duration: Long
) : Animation() {

    init {
        this.duration = duration
    }

    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation?
    ) {
        val height = (toHeight - fromHeight) * interpolatedTime + fromHeight
        val width = (toWidth - fromWidth) * interpolatedTime + fromWidth
        val layoutParams = view.layoutParams
        layoutParams.height = height.toInt()
        layoutParams.width = width.toInt()
        view.requestLayout()
    }
}

private lateinit var AnimationUpCamera:ValueAnimator;
class AgregateProducts : Fragment() {
    //    Variables de inicialisacion para el fragmento obteniendo el contexto de la base de la activdad
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAgregateProductsBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context

    //    Codigos para el escaneo de QR
    var name: String? = null;
    val REQUEST_CODE_PICKER: Int = 201
    var imagePath: String? = null;
    var SELECT_FILE_IMAGE_CODE:Int=101

    //    Adaptardor para la lista del adaptador de las imagenes
    val adapter = ImageAdapter(listOf<Uri>())

    //    Variables de inicialisacion para la camara y sus movimientos respectivos
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var imageCapture: ImageCapture;
    private var imageCap: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var CamStatus:CameraTypes = CameraTypes.NULL;

//    Funciones para la selecion multiple de las imagenes
    private lateinit var imagenUri:Uri;

    private var toogleCamera: Boolean = false;
    //    Animaciones procesan solo al ejectuar el codigo
//    private lateinit var AnimationUpCamera:ValueAnimator;
    //    Esta funcion limpi toda la pantalla incluido las imagenes de la lista

//    Mas funciones Globales para la camara y sus movimientos
    private var autoTransition: TransitionSet =  AutoTransition().setDuration(250);
    private  var HeigthIncrement: Int = 0;
    private var recycleviewWidth:Int = 0;
    private lateinit var AnimationUpIntercalate:AnimationDrawable;
    fun clearItems() {
        binding.TEQR.setText("")
        binding.TENombre.setText("")
        binding.TECantidadU.setText("")
        binding.TECantidad.setText("")
        binding.TECaracteristicas.setText("")
        binding.TECategoria.setText("")
        binding.TEMarca.setText("")
        binding.TEPrecio.setText("")
        binding.TEPrecioU.setText("")
        adapter.clearimagen()

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
        snackbar.show()
    }

    override fun onStart() {
        Log.i(LOGFRAGMENT,"Se Inicio onStart [*]")
        (activity as MainActivity).functionFabRefresh(::clearItems);
        (activity as MainActivity).returnbinding().refreshFab.setImageResource(R.drawable.ic_baseline_clear_all_24)

        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i(LOGFRAGMENT,"La camara es $toogleCamera")

        AnimationUpCamera=ObjectAnimator.ofFloat(binding.despliegeCamera, "translationY", -80f)
        AnimationUpCamera.repeatCount=ObjectAnimator.INFINITE
        AnimationUpCamera.repeatMode=ObjectAnimator.REVERSE
        AnimationUpCamera.duration=800


        AnimationUpIntercalate = binding.despliegeCamera.background as AnimationDrawable
        AnimationUpIntercalate.setEnterFadeDuration(1000)
        AnimationUpIntercalate.setExitFadeDuration(1000)
//        Obtiene la instacia de la actividad para la base de datos
        val daoNew = AppDatabase.getDataBase(baseActivity);
//        Estas Funciones obtiene la distancia del campo de la camara para obtener sus vectores y calcular el movimiento de la camara
//        val animation:Animation=AnimationUtils.loadAnimation(baseActivity,R.anim.slide_in_left)
//        val linearMayor:Int = binding.llinearConstraint.layoutParams.height
//        val linearMediun:Int = binding.linearConstraitButton.layoutParams.height
//        val linearGMayor:Int = binding.SVAPNew.layoutParams.height
//        val linearMenimun:Int=binding.PVCmain.layoutParams.height
//        val linearCamera:Int=binding.LayoutCamera.layoutParams.height

//        Estas son las bases para el incremento de la camara segun la perspectiva del telefono
        HeigthIncrement = binding.LayoutCamera.layoutParams.height
        recycleviewWidth = binding.RVCaptureImages.layoutParams.width

        binding.TENombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //Despues de que precione una tecla
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Antes de que efectue la tecla
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Cuando preciona la tecla para la ejecucion de los datos
                activity?.setTitle(s.toString() + " (Borrador)")
            }
        })

        binding.BAgregate.setOnClickListener {

            val _name: String = binding.TENombre.text.toString()
            val _precio: String = binding.TEPrecio.text.toString()
            val _precioU: String = binding.TEPrecioU.text.toString()
            val _marca: String = binding.TEMarca.text.toString()
            val _detalles: String = binding.TECaracteristicas.text.toString()
            val _categoria: String = binding.TECategoria.text.toString()
            val _stock: String = binding.TECantidad.text.toString()
            val _stockU: String = binding.TECantidadU.text.toString()
            val _qr: String = ""
//            val _image:ByteArray= ByteArray(0)

            val name = _name
            val precio: Double? = _precio.toDoubleOrNull()
            val precioU: Double = if (_precioU.isEmpty()) 0.0 else _precioU.toDouble()
            val marca: String = _marca
            val detalles: String = _detalles
            val categoria: String = _categoria
            val stock: Int = if (_stock.isEmpty()) 1 else _stock.toInt()
            val stockU: Int = if (_stockU.isEmpty()) 0 else _stockU.toInt()
            val qr: String = _qr

//            val image:List<ImagenesNew> = listOf(ImagenesNew(_image,Date()))
            val timeUpdate: Date = Date()
            var producto: Producto
            if (name.isEmpty() || precio == null) {
                messageSnackBar(view, "Se requiere el precio y el nombre del producto", Color.RED)
            } else {
                var pwd: Long
                producto = Producto(
                    timeUpdate,
                    name,
                    precio,
                    precioU,
                    marca,
                    detalles,
                    categoria,
                    stock,
                    stockU,
                    qr
                )
//                Funcion que permite guardar las imagenes dentro del cache para luego ser importadas a la base de datos
                CoroutineScope(Dispatchers.IO).launch {
                    pwd = daoNew.productosData().insertAll(producto)
                    if (adapter.MyImage.isNotEmpty()) {
                        for (path: Uri in adapter.MyImage) {
//                            La imagen se rota por que cuando se carga dentro de un URI ESTA TIENE LOS DATOS EXIF
//                            PERO CUANDO SE CARGA COMO MATRIS DE BYTES ENTONCES PIERDE TODA LA INFORMACION DE EXIF
//                            Con esta funcion podemos crear el reguistro para la imagen del producto
                            val imagen = MediaStore.Images.Media.getBitmap(baseActivity.contentResolver,path)
//                            println(path.path)
                            val orientedBitmap: Bitmap = ExifUtil.rotateBitmap(path.path!!, imagen)
//                            val inputData = baseActivity.contentResolver.openInputStream(path)?.readBytes()
                            val flujo = ByteArrayOutputStream()
                            orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
                            val imageInByte: ByteArray = flujo.toByteArray()
                            val imagenSave = ImagenesNew(Date(), pwd.toInt(),imageInByte)
                            CoroutineScope(Dispatchers.IO).launch {
                                daoNew.productosData().insertAllImages(imagenSave)
                                imagen.recycle()
                            }
                            messageSnackBar(view,text = "Se agrego '${name}'",Color.GREEN)
                        }
                    }
                }
            }
        }

        binding.BAgregateFile.setOnClickListener {
//            Agregar imagen usando pikaso pero no funciona en dispositvos android como el que tengo yo android 10 crakeado
//            ImagePicker.create(this).start(REQUEST_CODE_PICKER)     // Activity or Fragment
//            Asi que se uso esta actividad para tener un enfoque global de todas las demas
            imagenController.selectFromPothoGallery(this,SELECT_FILE_IMAGE_CODE)
            Log.d(LOGFRAGMENT,"Se preciono la selecion de imagenes")
        }


//        Bindig para el recyclerview de las imagenes
        binding.RVCaptureImages.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.RVCaptureImages.adapter = adapter

        binding.SVAPNew.isSmoothScrollingEnabled = false

        binding.despliegeCamera.setOnClickListener {
            CamStatus = CameraTypes.NULL
            if (toogleCamera) {
                AnimationUpCamera.start()
                //Animacion de recorido del la camara layout
                TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)
                val objectAnimator =
                    ObjectAnimator.ofInt(
                        binding.SVAPNew,
                        "scrollY",
                        binding.SVAPNew.scrollY,
                        binding.SVAPNew.height
                    ).setDuration(500)
                objectAnimator.setAutoCancel(true)
                binding.LayoutCamera.post(Runnable {
                    binding.LayoutCamera.visibility = View.GONE
                })
                AnimationUpIntercalate.stop()
                AnimationUpCamera.pause()

//                Animacion de recorrido en la camara
                val lp = LinearLayout.LayoutParams(
                    recycleviewWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                binding.RVCaptureImages.layoutParams = lp
                binding.linearConstraitButton.requestLayout()
                binding.BCaptura.isEnabled = true
                toogleCamera = false
            }
        }






        binding.BEscaner.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)
            binding.BcaptureImage.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(
                baseActivity,
                com.example.finalproductos.R.anim.animation_lineqr
            )
            binding.Vqrline.startAnimation(animation)
            binding.constraintqr.visibility = View.VISIBLE
            if (!toogleCamera) {
                baseActivity.runOnUiThread {
                    val objectAnimator =
                        ObjectAnimator.ofInt(
                            binding.SVAPNew,
                            "scrollY",
                            binding.SVAPNew.scrollY,
                            binding.SVAPNew.scrollY + HeigthIncrement
                        ).setDuration(400)
                    objectAnimator.setAutoCancel(true)
//                    objectAnimator.startDelay = 1000

                    objectAnimator.start()
                    binding.LayoutCamera.post(Runnable {
                        binding.LayoutCamera.visibility = View.VISIBLE
                    })
                }
//                val lp = LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                binding.recycleviewid.layoutParams=lp
                toogleCamera = true
                startCameraEscaner(view)
            } else {
                if (CamStatus == CameraTypes.CAMERA) {

                    val lp = LinearLayout.LayoutParams(
                        recycleviewWidth,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    binding.RVCaptureImages.layoutParams = lp
                    binding.BCaptura.isEnabled = true
                    startCameraEscaner(view)
                }
            }
            binding.linearConstraitButton.requestLayout()
            CamStatus = CameraTypes.SCANER
        }
//        startCamera()
        binding.BCaptura.setOnClickListener {
            openCamera()
        }

        var contadorImagen = 1
        binding.BcaptureImage.setOnClickListener {

            val imageFile = createTempFile("anImage", ".jpg")
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

                        Log.e(LOGFRAGMENT,"Ubo un error $error")
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        baseActivity.runOnUiThread {

                            adapter.addImage(outputFileResults.savedUri.toString())
//                                cameraProvider.unbindAll()
                        }
                    }
                })
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openCamera() {
        if (allPermisionGranted()) {
            TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)
            binding.BcaptureImage.visibility = View.VISIBLE
            binding.constraintqr.visibility = View.INVISIBLE
            if (!toogleCamera || CamStatus == CameraTypes.CAMERA) {
                if(!toogleCamera) {
                    val DesplazeAnimation =
                        ObjectAnimator.ofInt(
                            binding.SVAPNew,
                            "scrollY",
                            binding.SVAPNew.scrollY,
                            binding.SVAPNew.height + HeigthIncrement
                        ).setDuration(500)
                    DesplazeAnimation.setAutoCancel(true)
                    DesplazeAnimation.startDelay = 250
                    DesplazeAnimation.start()
                }
                binding.LayoutCamera.post(Runnable {
                    binding.LayoutCamera.visibility = View.VISIBLE
                })
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                binding.RVCaptureImages.layoutParams = lp

                binding.BCaptura.isEnabled = false
                toogleCamera = true
                contextFragment.runCatching {
                    startCamera()
                }
                if(AnimationUpCamera.isPaused){
                    AnimationUpCamera.resume()
                }else {
                    AnimationUpCamera.start()
                }
                AnimationUpIntercalate.start()
            } else {
                if (CamStatus == CameraTypes.SCANER) {
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    binding.RVCaptureImages.layoutParams = lp
                    contextFragment.runCatching {
                        cameraProvider.unbindAll()
                        startCamera()
                    }
                } else {
                    Log.d(LOGFRAGMENT,"La camara ya esta activa eso dicen los estados y la apertura de la camara ... ")
                    Log.i(LOGFRAGMENT,"Toogle camara es {$toogleCamera}")
                    Log.i(LOGFRAGMENT,"Tipo de la camara es {$CamStatus}")
                }
            }
            binding.linearConstraitButton.requestLayout()
            CamStatus = CameraTypes.CAMERA
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(LOGFRAGMENT,"Se apreto Agregar producto")
        return  when(item.itemId){
            R.id.MBitemSettings->{
                baseActivity.runOnUiThread {
                    Log.d(LOGFRAGMENT,"Se apreto Agregar producto")
                    val navBuilder = NavOptions.Builder()
                    baseActivity.navController.navigate(R.id.SecondFragment,null,navBuilder.build())
                }
                true
            }
            else->{
                Log.d(LOGFRAGMENT,"No se seleciono nada")
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i(LOGFRAGMENT,"Se Ejecuta OnCreate [*]")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregateProductsBinding.inflate(inflater, container, false)
        activity?.setTitle("Agregar Producto")
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    private fun focusControllerCamera(MyCamera: Camera) {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Get the camera's current zoom ratio
                val currentZoomRatio = MyCamera.cameraInfo.zoomState.value?.zoomRatio ?: 200F

                // Get the pinch gesture's scaling factor
                val delta = detector.scaleFactor

                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                MyCamera.cameraControl.setZoomRatio(currentZoomRatio * delta)

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
            newpressanimation.setEnterFadeDuration(100)
            newpressanimation.setExitFadeDuration(1000)
            binding.PVCmain.setOnTouchListener { _, event ->
                scaleGestureDetector.onTouchEvent(event)
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.i(LOGFRAGMENT,"${event.x}   ${event.y} ${event.rawX} ${event.rawY}")
                        val relativeParams =
                            binding.punteroFocus.layoutParams as ConstraintLayout.LayoutParams
                        relativeParams.setMargins(
                            event.x.toInt() - (binding.punteroFocus.height / 2),
                            event.y.toInt() - (binding.punteroFocus.width / 2),
                            0,
                            0
                        )
                        binding.punteroFocus.layoutParams = relativeParams

                        newpressanimation.start()
                        binding.punteroFocus.visibility = View.VISIBLE
                        //                                val trancicion  =
                        binding.punteroFocus.postDelayed(Runnable {
                            binding.punteroFocus.setVisibility(
                                View.INVISIBLE
                            )
                            newpressanimation.stop()
                        }, 600)

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

                        binding.punteroFocus.requestLayout()
                        true
                    }
                    MotionEvent.ACTION_UP -> {

                        val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            binding.PVCmain.width.toFloat(), binding.PVCmain.height.toFloat()
                        )
                        val autoFocusPoint = factory.createPoint(event.x, event.y)
                        try {
                            MyCamera.cameraControl.startFocusAndMetering(
                                FocusMeteringAction.Builder(
                                    autoFocusPoint,
                                    FocusMeteringAction.FLAG_AF
                                ).apply {
                                    //focus only when the user tap the preview

                                    disableAutoCancel()
                                }.build()
                            )

                        } catch (e: CameraInfoUnavailableException) {
                            Log.d("ERROR", "cannot access camera", e)
                        }
                        true
                    }
                    else -> false // Unhandled event.
                }

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i(LOGFRAGMENT,"Se Guardo la instancia onSaveInstanceState [*]")
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(baseActivity)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(
                    binding.PVCmain.surfaceProvider
                )
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(Size(binding.PVCmain.width, binding.PVCmain.height))
                    .build()
                imageCapture.setCropAspectRatio(Rational(200, 200))
                val MyCamera = cameraProvider.bindToLifecycle(
                    baseActivity,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                MyCamera.cameraControl.cancelFocusAndMetering()
                focusControllerCamera(MyCamera)
            } catch (e: Exception) {
                Log.e(LOGFRAGMENT, "Error al asignar la camara", e)
            }
        }, ContextCompat.getMainExecutor(contextFragment))
    }

    private fun startCameraEscaner(MyView:View) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(baseActivity)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().apply {
                setTargetResolution(Size(binding.PVCmain.width, binding.PVCmain.height))
            }.build().also { mPreview ->
                mPreview.setSurfaceProvider(

                    binding.PVCmain.surfaceProvider

                )
            }
//            preview.targetRotation=ROTATION_0

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(binding.PVCmain.width, binding.PVCmain.height))
//            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrResult ->
                        binding.PVCmain.post {
//                            Log.d("QRCodeAnalyzer", "Barcode scanned: ${qrResult.text}")
//                            Toast.makeText(
//                                activity,
//                                "Codigo es ${qrResult.text}",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            messageSnackBar(MyView,"Coideog es ${qrResult.text}",Color.YELLOW)
                            binding.TEQR.setText(qrResult.text)
                            cameraProvider.unbindAll()
//                            cameraProvider.unbindAll()
                        }
                    })
                }
            binding.PVCmain.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
//            preview.targetRotation= ROTATION_9

            try {
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                cameraProvider.unbindAll()
                val MyCamera =
                    cameraProvider.bindToLifecycle(baseActivity, cameraSelector, preview, imageAnalysis)
                MyCamera.cameraControl.cancelFocusAndMetering()
                focusControllerCamera(MyCamera)

            } catch (e: Exception) {
                Log.e(LOGFRAGMENT, "Error al intentar Asignar la camara de Escaneo",e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun allPermisionGranted():Boolean {
        val dat = Constrains.REQUIERED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseActivity, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if(!dat){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            val images =
                ImagePicker.getImages(data) as ArrayList
            imagePath = images[0].path;

            var dato: List<String> = listOf()
            val pwd = dato.toMutableList()
            images.forEach { image ->
                adapter.addImage(image.path)
            }
            dato = pwd.toList()
        }

        if(requestCode==SELECT_FILE_IMAGE_CODE && resultCode==Activity.RESULT_OK) {
            var clipdata: ClipData? = data?.clipData
            if (clipdata == null) {
                imagenUri = data?.data as Uri;
                adapter.addImage(imagenUri)
            } else {
                for (i:Int in 0 .. clipdata.itemCount-1){
                    adapter.addImage(clipdata.getItemAt(i).uri)
                }
            }
        }
        if(requestCode==SELECT_FILE_IMAGE_CODE && resultCode==Activity.RESULT_CANCELED) {
        }


        return

        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, "Se cancelo", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    activity,
                    "Se entro Correctamente el codigo ${result.contents}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        if (CamStatus == CameraTypes.CAMERA) {
            binding.LayoutCamera.visibility=View.VISIBLE
            openCamera()
        } else if (CamStatus == CameraTypes.SCANER) {
            binding.LayoutCamera.visibility=View.VISIBLE
            startCameraEscaner(View(contextFragment))
        }


        super.onResume()
    }

    override fun onPause() {
        Log.i(LOGFRAGMENT,"Se pause onPause [*]")
        super.onPause()
    }

    override fun onStop() {
        Log.i(LOGFRAGMENT,"Se paro onStop [*]")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(LOGFRAGMENT,"Se destruyo el fragmento onDestroy [*]")
        for (file: File in context?.cacheDir?.listFiles()!!) {
//            println(file.path.toString())
//            println(file.name)
//            println(file.absolutePath)
//            println(file.absoluteFile)
//            println(file.canonicalPath)
            if (file.toUri() in adapter.MyImage) {
                file.delete()
            }
        }
        if (CamStatus != CameraTypes.NULL) {
            cameraProvider.unbindAll()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {

        Log.i(LOGFRAGMENT,"Se destruyo la vista onDestroyView [*]")
        super.onDestroyView()
//        println("SE DESTRUYO LA VISTA")
//         for (file:File in context?.cacheDir?.listFiles()!!){
//             file.delete()
//         }
        cameraProvider.unbindAll()
        _binding = null
    }

    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            1f, 1f,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 1000
        v.startAnimation(anim)
    }

    fun getSpringAnimation(
        view: View,
        springAnimationType: FloatPropertyCompat<View>,
        finalPosition: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, springAnimationType)
        // create a spring with desired parameters
        val spring = SpringForce()
        spring.finalPosition = finalPosition
        spring.stiffness = SpringForce.STIFFNESS_VERY_LOW // optional
        spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY // optional
        // set your animation's spring
        animation.spring = spring
        return animation
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregateProducts.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregateProducts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

