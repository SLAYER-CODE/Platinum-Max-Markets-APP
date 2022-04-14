package com.example.finalproductos

import com.example.finalproductos.ui.View.adapter.ImageAdapter
import com.example.finalproductos.util.listener.DragAndDropListenerActions
import com.example.finalproductos.data.model.helper.MyItemTouchHelperCallback
import com.example.finalproductos.util.listener.TouchDropListenerAction
import com.example.finalproductos.data.db.AppDatabase
import Data.ImagenesNew
import Data.Producto
import com.example.finalproductos.data.model.controller.imagenController
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import com.example.finalproductos.databinding.FragmentAgregateProductsBinding
import android.animation.ObjectAnimator
import android.animation.ValueAnimator

//import android.R
//import androidx.vectordrawable.R

import android.app.Activity
import android.content.ClipData
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.TextWatcher
import android.transition.*
import android.util.Log
import android.util.Size
import android.view.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
//import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.*
import android.net.Uri
import android.provider.MediaStore
import android.util.Rational
import androidx.core.net.toUri
import androidx.camera.core.Camera
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.example.finalproductos.data.model.base.CallBackItemTouch
import kotlinx.android.synthetic.main.image_new_new.view.*
import java.io.ByteArrayOutputStream
import android.graphics.PorterDuff

import android.graphics.PorterDuffColorFilter
import android.os.Environment

import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

import android.widget.SeekBar

import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.finalproductos.util.ConNet

import com.example.finalproductos.util.ConnectToPost

import com.example.finalproductos.data.model.QrCodeAnalyzer
import com.example.finalproductos.data.model.objects.Constants.Images.REQUEST_CODE_PICKER
import com.example.finalproductos.data.model.objects.Constants.Images.SELECT_FILE_IMAGE_CODE
import com.example.finalproductos.data.model.objects.Constrains
import com.example.finalproductos.data.model.types.CameraTypes
import com.example.finalproductos.ui.View.activity.MainActivity
import com.example.finalproductos.util.hideKeyboardFrom
import kotlinx.coroutines.*
import java.lang.Runnable
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint


// TODO: Argumento que sirve para nombre como registro de este archivo
private const val LOGFRAGMENT:String="AddProducto"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregateProducts.newInstance] factory method to
 * create an instance of this fragment.
 */


@AndroidEntryPoint
class AgregateProducts : Fragment()  , CallBackItemTouch
{

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


    //    Variables de inicialisacion para el fragmento obteniendo el contexto de la base de la activdad
    private var param1: String? = null
    private var param2: String? = null
    private var parametro: String? = null;
    private var _binding: FragmentAgregateProductsBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context

    private lateinit var AnimationUpCamera:ValueAnimator;

    private var offsetViewItem:Rect=Rect()
    private var offsetViewItemTwo=Rect()


    private lateinit var MyCamera:Camera;
    private var PermisosCamera:Boolean=false;

    private lateinit var imageAnalisis:ImageAnalysis;

    //    Codigos para el escaneo de QR
    var name: String? = null;
    var imagePath: String? = null;

    //    Adaptardor para la lista del adaptador de las imagenes
    private val adapter = ImageAdapter(mutableListOf())
    //    Variables que definen la posicion con la ultima vista que se seleciono del RecyclerView
    private lateinit var viewHold:View;
    private var position:Int=0;

    //    Variables de inicialisacion para la camara y sus movimientos respectivos
    private lateinit var cameraProvider: ProcessCameraProvider;
    private lateinit var imageCapture: ImageCapture;
    private var imageCap: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var CamStatus: CameraTypes = CameraTypes.NULL;
    private var CamClick: CameraTypes = CameraTypes.NULL;

    //Variables que de igual
    private lateinit var cameraProviderFuture:ListenableFuture<ProcessCameraProvider>;
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    //    Funciones para la selecion multiple de las imagenes
    private lateinit var imagenUri: Uri;

    private var toogleCamera: Boolean = false;
    //    Animaciones procesan solo al ejectuar el codigo
//    private lateinit var AnimationUpCamera:ValueAnimator;
    //    Esta funcion limpi toda la pantalla incluido las imagenes de la lista

    //    Mas funciones Globales para la camara y sus movimientos
    private var autoTransition: TransitionSet = AutoTransition().setDuration(250);
    private var HeigthIncrement: Int = 0;
    private var recycleviewWidth: Int = -1;


    //Distancias para la elimiancion guardado y muestra
    private var RefDeleteViewImage: Int = 1;
    private var RefMediumDistanceImage: Int = 1;
    private lateinit var offsetViewBounds: Rect;
    private lateinit var AnimationUpIntercalate: AnimationDrawable;
    private var Corutine:Job? = null;

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

    override fun onStart() {
        Log.i(LOGFRAGMENT, "Se Inicio onStart [*]")
        (activity as MainActivity).functionFabRefresh(::clearItems);
        baseActivity.returnbinding().BIShowP.isVisible=false
        (activity as MainActivity).returnbinding().refreshFab.setImageResource(R.drawable.ic_baseline_clear_all_24)
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i(LOGFRAGMENT, "La camara es $toogleCamera")
        AnimationUpCamera = ObjectAnimator.ofFloat(binding.despliegeCamera, "translationY", -80f)
        AnimationUpCamera.repeatCount = ObjectAnimator.INFINITE
        AnimationUpCamera.repeatMode = ObjectAnimator.REVERSE
        AnimationUpCamera.duration = 800

        AnimationUpIntercalate = binding.despliegeCamera.background as AnimationDrawable
        AnimationUpIntercalate.setEnterFadeDuration(1000)
        AnimationUpIntercalate.setExitFadeDuration(1000)


        Log.i("CameraStateRegistry", cameraExecutor.isShutdown.toString())
//        if (CamStatus == CameraTypes.CAMERA) {
//            binding.LayoutCamera.visibility = View.VISIBLE
//            openCamera()
//        } else if (CamStatus == CameraTypes.SCANER) {
//            print("Si esta funcionando "+ Corutine.isActive)
//            binding.LayoutCamera.visibility = View.VISIBLE
//            if(!Corutine.isActive){
//                openCameraScanner(view)
//            }
//        }

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
        if (recycleviewWidth == -1){
            recycleviewWidth = binding.RVCaptureImages.layoutParams.width
        }
        imageAnalisis=ImageAnalysis.Builder().setTargetResolution(Size(HeigthIncrement, HeigthIncrement))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val FocusEditListener =   View.OnFocusChangeListener { p0, p1 ->
            if(!p1){
                hideKeyboardFrom(requireContext(),view)
            }
        }


//        binding.TEDescuento.onFocusChangeListener = FocusEditListener
//        binding.TENombre.onFocusChangeListener = FocusEditListener
//        binding.TEPrecio.onFocusChangeListener = FocusEditListener
//        binding.TEPrecioU.onFocusChangeListener = FocusEditListener
//        binding.TECantidad.onFocusChangeListener = FocusEditListener
//        binding.TECantidadU.onFocusChangeListener = FocusEditListener
//        binding.TECaracteristicas.onFocusChangeListener = FocusEditListener
//        binding.TECategoria.onFocusChangeListener = FocusEditListener
//        binding.TECategoria.onFocusChangeListener = FocusEditListener
//        binding.TEQR.onFocusChangeListener = FocusEditListener






        binding.TEDescuento.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                var descuento = 0f
                var precio=0f;
                if(s!=null&& s.isNotEmpty()) {
                    descuento = s.toString().toFloat()
                }

                if(binding.TEPrecio.text!=null&&binding.TEPrecio.text!!.isNotEmpty()){
                    precio = binding.TEPrecio.text.toString().toFloat()
                }
                binding.TVpreciodisconut.text = (precio-descuento).toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }
        )


        binding.TEPrecio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                var TPrecio=binding.TEPrecioU.text
//                if(TPrecio==null||TPrecio.isEmpty()||TPrecio.toString()==""||TPrecio.toString()=="0"){
                    binding.TEPrecioU.setText(s.toString())
//                }
            }

            override fun afterTextChanged(s: Editable?) {
                var descuento = 0f
                var precio=0f;
                if(s!=null&& s.isNotEmpty()) {
                    precio = s.toString().toFloat()
                }
                if(binding.TEDescuento.text!=null&&binding.TEDescuento.text!!.isNotEmpty()){
                    descuento = binding.TEDescuento.text.toString().toFloat()
                }
                binding.TVpreciodisconut.text = (precio-descuento).toString()
            }
        })

        binding.TENombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Cuando preciona la tecla para la ejecucion de los datos
                if(s.toString() != ""){
                    activity?.setTitle(s.toString() + " (Borrador N° $parametro)")
                }else{
                    activity?.setTitle("Agregar Producto [$parametro]")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
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
                 lifecycleScope.launch {
                     val resultKeys = withContext(Dispatchers.IO) {
                         try {
                             pwd = daoNew.productosData().insertAll(producto)
                             if (adapter.MyImage.isNotEmpty()) {
                                 for (path: Uri in adapter.MyImage) {
//                            La imagen se rota por que cuando se carga dentro de un URI ESTA TIENE LOS DATOS EXIF
//                            PERO CUANDO SE CARGA COMO MATRIS DE BYTES ENTONCES PIERDE TODA LA INFORMACION DE EXIF
//                            Con esta funcion podemos crear el reguistro para la imagen del producto
                                     val imagen = MediaStore.Images.Media.getBitmap(
                                         baseActivity.contentResolver,
                                         path
                                     )
//                            println(path.path)
                                     val orientedBitmap: Bitmap =
                                         ExifUtil.rotateBitmap(path.path!!, imagen)
//                            val inputData = baseActivity.contentResolver.openInputStream(path)?.readBytes()
                                     val flujo = ByteArrayOutputStream()
                                     orientedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, flujo)
                                     val imageInByte: ByteArray = flujo.toByteArray()
                                     val imagenSave = ImagenesNew(Date(), pwd.toInt(), imageInByte)
                                     CoroutineScope(Dispatchers.IO).launch {
                                         daoNew.productosData().insertAllImages(imagenSave)
                                         imagen.recycle()
                                     }
                                 }
                             }
                            true
                         }catch (ex:Exception){
                             false
                         }
                     }
                    if(resultKeys) {
                        messageSnackBar(view, text = "Agregado '${name}'", Color.GREEN)
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


        binding.DeleteDrag.setOnDragListener(DragAndDropListenerActions(baseActivity));
        binding.SaveDrag.setOnDragListener(DragAndDropListenerActions(baseActivity));

        binding.DeleteDrag.setOnTouchListener(TouchDropListenerAction())
        binding.SaveDrag.setOnTouchListener(TouchDropListenerAction())


        var adapterSPeso = ArrayAdapter<String>(requireContext(),R.layout.item_spiner_peso,resources.getStringArray(R.array.dlagsPeso))
        binding.SPpeso.adapter=adapterSPeso

        var adapterSCantidad = ArrayAdapter<String>(requireContext(),R.layout.item_spiner_peso,resources.getStringArray(R.array.dlagsCantidad))
        binding.SPCantidad.adapter=adapterSCantidad

        binding.SPCantidad.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (position){
                    0->{
                        binding.SPpeso.setSelection(5)
                    }
                    1->{
                        binding.SPpeso.setSelection(3)
                    }
                    2->{
                        binding.SPpeso.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        binding.SPCantidad.setSelection(1)
        binding.SBzoom.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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
//        Bindig para el recyclerview de las imagenes
        binding.RVCaptureImages.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)


        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                println("CARLOSS")
            }
        })

        binding.RVCaptureImages.adapter = adapter
        var RVCallback:ItemTouchHelper.Callback = MyItemTouchHelperCallback(this);
        var RVTouchHelper:ItemTouchHelper = ItemTouchHelper(RVCallback);
        RVTouchHelper.attachToRecyclerView(binding.RVCaptureImages);

        binding.SVAPNew.isSmoothScrollingEnabled = false

        binding.BRscanner.setOnClickListener {

            val animation = AnimationUtils.loadAnimation(
                baseActivity,
                R.anim.animation_lineqr
            )

            binding.Vqrline.startAnimation(animation)
            binding.BRscanner.visibility=View.INVISIBLE
            if(Corutine!=null) {
                Corutine!!.cancel()
                Corutine=null;
            }

            imageAnalisis.setAnalyzer(cameraExecutor,QrCodeAnalyzer{qrResult->
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
                    binding.TEQR.setText(qrResult.text)
                    binding.Vqrline.clearAnimation()
                    binding.BRscanner.visibility = View.VISIBLE
                    Corutine = GlobalScope.launch(Dispatchers.Main) {
                        val resultKeys = withContext(Dispatchers.IO) {
                            ConnectToPost.ConnectAndGet(qrResult.text)
                        }
                        println(resultKeys)
                    }
                }
            })

            println("SE SETEO")
        }

        binding.despliegeCamera.setOnClickListener {
//            baseActivity.returnbinding().PBbarList.visibility=View.VISIBLE
            CamStatus = CameraTypes.NULL
            CamClick = CameraTypes.NULL
            if (toogleCamera) {
                AnimationUpCamera.start()
                //Animacion de recorido del la camara layout
                val objectAnimator =
                    ObjectAnimator.ofInt(
                        binding.SVAPNew,
                        "scrollY",
                        binding.SVAPNew.scrollY,
                        binding.SVAPNew.height-HeigthIncrement-(binding.SVAPNew.height-binding.SVAPNew.scrollY)
                    ).setDuration(300)
                objectAnimator.setAutoCancel(true)

                binding.LayoutCamera.post(Runnable {
                    binding.LayoutCamera.visibility = View.GONE
                    objectAnimator.start()
                    AnimationUpIntercalate.stop()
                    AnimationUpCamera.pause()
                })

//                Animacion de recorrido en la camara


                binding.linearConstraitButton.requestLayout()
                binding.BCaptura.isEnabled = true
                toogleCamera = false
                comprobateItem()
                TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)

            }
        }

//      Start camera ESCANNER()
        binding.BEscaner.setOnClickListener {

            if(CamStatus==CameraTypes.SCANER){
                binding.LayoutCamera.visibility=View.GONE
                toogleCamera=false
                CamStatus = CameraTypes.NULL
                binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_addproduct)

            }else {
                binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_close_24)
                openCameraScanner(view,true)
            }
        }
//        startCamera()
        binding.BCaptura.setOnClickListener {
            openCamera()
        }
//        binding.BRscanner.setOnClickListener {
//            binding.BRscanner.visibility=View.INVISIBLE
//            openCameraScanner(view);
//        }

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
                            binding.RVCaptureImages.scrollToPosition(0);
                            comprobateItem()
//                                cameraProvider.unbindAll()
                        }
                    }
                })
        }
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun comprobateItem(){
        var Value:Boolean=adapter.MyImage.size==0
        var lp:LinearLayout.LayoutParams= binding.TNotItems.layoutParams as LinearLayout.LayoutParams
        var lprV:LinearLayout.LayoutParams
        if(toogleCamera && CamStatus==CameraTypes.CAMERA){


            if(Value){

                lprV = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0f
                )

            }else{
                lprV = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0f
                )
                lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,0f
                )
            }
        }else{
            if(Value){
                lprV = LinearLayout.LayoutParams(
                    recycleviewWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0f
                )
                lp = LinearLayout.LayoutParams(
                    recycleviewWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT,2.2f
                )
            }else{
                lprV = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0f
                )
                lp = LinearLayout.LayoutParams(
                    recycleviewWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT,0f
                )
            }
        }
        binding.TNotItems.layoutParams=lprV
        binding.RVCaptureImages.layoutParams=lp

    }

    override fun itemTouchMode(oldPosition: Int, newPosition: Int) {
        position=newPosition
        adapter.MyImage.add(oldPosition,adapter.MyImage.removeAt(newPosition))
        adapter.notifyItemMoved(oldPosition,newPosition);
        comprobateItem()
//        super.itemTouchMode(oldPosition, newPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {

}

    override fun onDrop(eliminated: Boolean, position: Int) {
        if(eliminated){
            adapter.removeImagen(position)
        }

    }

    override fun prepareViews(views: Boolean, viewholds: RecyclerView.ViewHolder?) {
        if(viewholds!=null) {
            viewHold=viewholds.itemView.viewParent
            position=viewholds.absoluteAdapterPosition
            offsetViewBounds = Rect()
            binding.DeleteDrag.getDrawingRect(offsetViewBounds);
            binding.linearConstraitButton.offsetDescendantRectToMyCoords(
                binding.DeleteDrag,
                offsetViewBounds
            )
            binding.SaveDrag.getDrawingRect(offsetViewItemTwo)
            binding.linearConstraitButton.offsetDescendantRectToMyCoords(
                binding.SaveDrag,
                offsetViewItemTwo
            )
            viewHold.getDrawingRect(offsetViewItem)
            binding.linearConstraitButton.offsetDescendantRectToMyCoords(viewHold, offsetViewItem)
        }
        val transtion =  AutoTransition().setDuration(350);
        if(views) {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            binding.DeletAndSaveParent.layoutParams=lp
            binding.LEandI.layoutParams=lp
        }else{
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            )
            binding.DeletAndSaveParent.layoutParams=lp
            val lpDat = LinearLayout.LayoutParams(
                0,
                0
            )
            binding.LEandI.layoutParams=lpDat
        }
        TransitionManager.beginDelayedTransition(binding.linearConstraitButton, transtion)
//        binding.DeletAndSaveParent.requestLayout()
    }

    override fun isItemSelected(posX: Float, posY: Float): Int{
        println("Segundo $posX")
        println("Segundo $posY")
        var pwd = 0
        if(!toogleCamera){
            pwd+=330
        }
        var MMDImagenY = posX+(viewHold.width/2)
        var TABCalc=(offsetViewBounds.top-50)<=((offsetViewItem.top+posY)+(viewHold.width/2)) && (offsetViewBounds.bottom+100)>=((offsetViewItem.top+posY)+(viewHold.width/2))
        if(TABCalc && offsetViewBounds.left-pwd <= MMDImagenY && offsetViewBounds.right-pwd>=MMDImagenY) {
        val Final:Uri=adapter.MyImage.get(position)
        //back que del item que se elimina para que pueda ser cancelado
        val indexDelete:Int =position;
        //removimiento el item del reciclerview
        adapter.removeImagen(indexDelete)
        var RestoreSnackbar : Snackbar = Snackbar.make(requireView(),".../"+Final.path,Snackbar.LENGTH_LONG)
        RestoreSnackbar.setAction("Cancelar",View.OnClickListener {
            adapter.restoreItem(Final,indexDelete);
        })
        RestoreSnackbar.setActionTextColor(Color.RED)
        val snackbarView = RestoreSnackbar.view
        snackbarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.RED)
        textView.textSize = 17f
        RestoreSnackbar.show()
            comprobateItem()
            return 1;
        }
        if(TABCalc && offsetViewItemTwo.left-pwd <= MMDImagenY && offsetViewItemTwo.right-pwd>=MMDImagenY){
            val filepath = Environment.getExternalStorageDirectory().absolutePath
            val externalPath:String="/MhImagenes/"+adapter.MyImage[position].lastPathSegment

            return 0;
        }

        return -1;
    }
    override fun CalculateArea(posX: Float, posY: Float){
        RefMediumDistanceImage=offsetViewBounds.top
        var diferencia=(RefMediumDistanceImage-(posY+offsetViewItem.top))
        var dato = (diferencia*32)/(RefMediumDistanceImage-offsetViewItem.top)
        var scale=dato * 0.031f
        if(scale>0.35 && scale<1.40) {
            viewHold.scaleX = scale
            viewHold.scaleY = scale
        }

//        println("POST $posY")
//        println(posY)
//        println(dato)
//        println("xdddddddddd")
//        println(viewHold.width)
//        println(posY)
//        println(HeigthIncrement)
//        println("$posX $posY $HeigthIncrement ${offsetViewBounds.top}")
//        println(offsetViewItem.top)
//        println(offsetViewItem.bottom)
//        println(offsetViewItem.left)
//        println(offsetViewItem.right)
//        println(posX+ abs(posX-offsetViewItem.left))
//        println(offsetViewBounds.left)

        var pwd = 0
        if(!toogleCamera){
            pwd+=330
        }
        var MMDImagenY = posX+(viewHold.width/2)
        var TABCalc=(offsetViewBounds.top-50)<=((offsetViewItem.top+posY)+(viewHold.width/2)) && (offsetViewBounds.bottom+100)>=((offsetViewItem.top+posY)+(viewHold.width/2))
        val ColorSave = Color.rgb(72,134,255)
        val ColorDelete =Color.rgb(171,0,0)
        if(TABCalc && offsetViewBounds.left-pwd <= MMDImagenY && offsetViewBounds.right-pwd>=MMDImagenY) {
            binding.DeleteDrag.setTextColor(Color.RED)
            binding.DeleteDrag.setShadowLayer(40f, -6f, 0f, Color.RED)
            binding.DeleteDrag.compoundDrawables[2].colorFilter= PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            binding.DeleteDrag.compoundDrawables[2].bounds=Rect(0,0,100,100)
        }
        else{
            binding.DeleteDrag.compoundDrawables[2].bounds=Rect(0,0,70,70)
            binding.DeleteDrag.compoundDrawables[2].colorFilter= PorterDuffColorFilter(ColorDelete, PorterDuff.Mode.SRC_IN)
            binding.DeleteDrag.setTextColor(ColorDelete)
        }
        if(TABCalc && offsetViewItemTwo.left-pwd <= MMDImagenY && offsetViewItemTwo.right-pwd>=MMDImagenY){
            binding.SaveDrag.setTextColor(Color.CYAN)
            binding.SaveDrag.setShadowLayer(40f, -6f, 0f, Color.CYAN)
            binding.SaveDrag.compoundDrawables[0].colorFilter= PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN)
            binding.SaveDrag.compoundDrawables[0].bounds=Rect(0,0,100,100)
        }
        else{
            binding.SaveDrag.compoundDrawables[0].bounds=Rect(0,0,70,70)
            binding.SaveDrag.compoundDrawables[0].colorFilter= PorterDuffColorFilter(ColorSave, PorterDuff.Mode.SRC_IN)
            binding.SaveDrag.setTextColor(ColorSave)
        }
    }




//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
//        val Final:Uri=adapter.MyImage.get(viewHolder.absoluteAdapterPosition)
//        //back que del item que se elimina para que pueda ser cancelado
//        val indexDelete:Int =viewHolder.absoluteAdapterPosition;
//        //removimiento el item del reciclerview
//        adapter.removeImagen(indexDelete)
//        var RestoreSnackbar : Snackbar = Snackbar.make(requireView(),"..."+Final.path?.substring(0,-10),Snackbar.LENGTH_LONG)
//        RestoreSnackbar.setAction("CANCELAR",View.OnClickListener {
//            adapter.restoreItem(Final,indexDelete);
//        })
//        RestoreSnackbar.setActionTextColor(Color.RED)
//        val snackbarView = RestoreSnackbar.view
//        snackbarView.setBackgroundColor(Color.BLACK)
//        val textView =
//            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//        textView.setTextColor(Color.RED)
//        textView.textSize = 17f
//        RestoreSnackbar.show()
//        super.onSwiped(viewHolder, position)
//    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(LOGFRAGMENT,"Se apreto Agregar producto")
        return  when(item.itemId){
            R.id.MBSettings->{
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
//        val animation= TransitionInflater.from(context).inflateTransition(
//            android.R.transition.move
//        )
//        sharedElementEnterTransition=animation

        Log.i(LOGFRAGMENT,"Se Ejecuta OnCreate [*]")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            parametro=it.getString("titlenumber")

        }
//        Corutine= null;
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregateProductsBinding.inflate(inflater, container, false)
        activity?.setTitle("Agregar Producto [$parametro]")
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i(LOGFRAGMENT,"Se Guardo la instancia onSaveInstanceState [*]")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(LOGFRAGMENT,"Codigo iniciado")
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            val images =
                ImagePicker.getImages(data) as ArrayList
            imagePath = images[0].path;

            var dato: List<String> = listOf()
            val pwd = dato.toMutableList()
            images.forEach { image ->
                adapter.addImage(image.path)
                binding.RVCaptureImages.scrollToPosition(0);
                comprobateItem()
            }
            dato = pwd.toList()
        }

        if(requestCode==SELECT_FILE_IMAGE_CODE && resultCode==Activity.RESULT_OK) {
            var clipdata: ClipData? = data?.clipData
            if (clipdata == null) {
                imagenUri = data?.data as Uri;
                adapter.addImage(imagenUri)
            } else {
                for (i: Int in 0..clipdata.itemCount - 1) {
                    adapter.addImage(clipdata.getItemAt(i).uri)
                }
            }
        }
        comprobateItem()
        return
    }
    override fun onResume() {
        Log.i(LOGFRAGMENT,"Resumiendo la actividad")
        println(PermisosCamera)
        println(CamClick)

        if(PermisosCamera) {
            if (Constrains.REQUIERED_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        baseActivity, it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                    if(CamClick==CameraTypes.SCANER){
                        binding.LayoutCamera.visibility = View.VISIBLE
                        binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_close_24)

//                        startCameraEscaner(view as View)
                        if(null==Corutine) {
                            openCameraScanner(view as View,true)
//                            binding.constraintqr.visibility=View.VISIBLE
//                            binding.BRscanner.visibility = View.VISIBLE
                        }else{
                            openCameraScanner(view as View,false)
                            binding.Vqrline.clearAnimation()
                            binding.BRscanner.visibility = View.VISIBLE
                            imageAnalisis.clearAnalyzer()
                        }
//                        startCameraEscaner(view as View)

                    }
                    if(CamClick==CameraTypes.CAMERA){
                        binding.LayoutCamera.visibility = View.VISIBLE
                        openCamera()
                    }
                Log.i(LOGFRAGMENT, "Permisos de camera")
            }else{
                messageSnackBar(view as View,"Se necesita acceso a la camara...",Color.RED)
            }
//            PermisosCamera=true
        }

        super.onResume()
    }

    override fun onPause() {
        Log.i("CameraStateRegistry","Pauseando la acitividad")
        Log.i("CameraStateRegistry",cameraExecutor.isTerminated.toString())
//        cameraExecutor.shutdown()
        Log.i(LOGFRAGMENT,"Se pause onPause [*]")
        if (CamStatus != CameraTypes.NULL) {
            cameraProvider.unbindAll()
        }
        super.onPause()
    }

    override fun onStop() {
        Log.i(LOGFRAGMENT,"Se paro onStop [*]")
        super.onStop()
    }

    override fun onDestroy() {

        Log.i(LOGFRAGMENT,"Se destruyo el fragmento onDestroy [*]")
        baseActivity.datasize-=1

        Corutine?.cancel()
//        println(Corutine)

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
//        if(Corutine!=null){
//            if(Corutine!!.isActive){
//                println("Esta activo")
//                Corutine!!.cancel("Se Cancelo")
//            }
//        }
        if (CamStatus != CameraTypes.NULL) {
            Log.i("CameraStateRegistry","Se cerro la camara al destruir la vista [*]")
//            cameraProvider.shutdown()
//            cameraProvider.unbindAll()
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
//        cameraProvider.unbindAll()

        _binding = null
    }

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

            binding.PVCmain.setOnTouchListener {_:View, event ->
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
    private fun openCameraScanner(view: View,analizer:Boolean){
        binding.despliegeCamera.visibility=View.GONE
        CamClick=CameraTypes.SCANER
        PermisosCamera=true
        if(allPermisionGranted()){

            binding.BcaptureImage.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(
                baseActivity,
                R.anim.animation_lineqr
            )
            binding.Vqrline.startAnimation(animation)
            binding.constraintqr.visibility = View.VISIBLE

            if (!toogleCamera || CamStatus == CameraTypes.SCANER) {

                if(!toogleCamera){

                    binding.LayoutCamera.post(Runnable {
                        binding.LayoutCamera.visibility = View.VISIBLE
                    })

                    val DesplazeAnimation =
                        ObjectAnimator.ofInt(
                            binding.SVAPNew,
                            "scrollY",
                            binding.SVAPNew.scrollY,
                            binding.SVAPNew.height + HeigthIncrement - 600
                        ).setDuration(250)

                    DesplazeAnimation.setAutoCancel(true)
                    DesplazeAnimation.start()

                    toogleCamera=true


                }

//                val lp = LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                binding.recycleviewid.layoutParams=lp
            }

            if (CamStatus == CameraTypes.CAMERA) {
                val lp = LinearLayout.LayoutParams(
                    recycleviewWidth,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                binding.RVCaptureImages.layoutParams = lp
                binding.TNotItems.layoutParams=lp
                binding.BCaptura.isEnabled = true
            }

            if(AnimationUpCamera.isPaused){
                AnimationUpCamera.resume()
            }else {
                if(!AnimationUpCamera.isRunning){
                    AnimationUpCamera.start()
                }
            }
            if(!AnimationUpIntercalate.isRunning)
            {
                AnimationUpIntercalate.start()
            }

            CamStatus = CameraTypes.SCANER
            contextFragment.runCatching {
                startCameraEscaner(view,analizer)
            }
//            binding.linearConstraitButton.requestLayout()
        }
        comprobateItem()
        hideKeyboardFrom(contextFragment,view)
        TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)
    }

    private fun openCamera() {
        binding.despliegeCamera.visibility=View.VISIBLE

        PermisosCamera=true;
        CamClick=CameraTypes.CAMERA

        if (allPermisionGranted()) {

            binding.BcaptureImage.visibility = View.VISIBLE
            if (!toogleCamera || CamStatus == CameraTypes.CAMERA) {
                if(!toogleCamera) {
                    binding.constraintqr.visibility = View.GONE
                    binding.LayoutCamera.post(Runnable {
                        binding.LayoutCamera.visibility = View.VISIBLE
                    })
                    val DesplazeAnimation =
                        ObjectAnimator.ofInt(
                            binding.SVAPNew,
                            "scrollY",
                            binding.SVAPNew.scrollY,
                            binding.SVAPNew.height + HeigthIncrement - 600
                        ).setDuration(300)

                    DesplazeAnimation.setAutoCancel(true)
                    DesplazeAnimation.start()

                }

//                val lp = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT
//                )
//                binding.RVCaptureImages.layoutParams = lp
//                binding.TNotItems.layoutParams=lp

                toogleCamera = true
                contextFragment.runCatching {
                    startCamera()
                }

            } else {
                if (CamStatus == CameraTypes.SCANER) {
                    binding.constraintqr.visibility = View.INVISIBLE
                    binding.BRscanner.visibility=View.INVISIBLE
                    binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_addproduct)
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    binding.RVCaptureImages.layoutParams = lp
                    binding.TNotItems.layoutParams=lp

                    contextFragment.runCatching {
//                        cameraProvider.unbindAll()
                        startCamera()
                    }
                } else {
                    Log.d(LOGFRAGMENT,"La camara ya esta activa eso dicen los estados y la apertura de la camara ... ")
                    Log.i(LOGFRAGMENT,"Toogle camara es {$toogleCamera}")
                    Log.i(LOGFRAGMENT,"Tipo de la camara es {$CamStatus}")
                }
            }
            if(AnimationUpCamera.isPaused){
                AnimationUpCamera.resume()
            }else {
                AnimationUpCamera.start()
            }
            if(!AnimationUpIntercalate.isRunning)
            {
                AnimationUpIntercalate.start()
            }
            binding.BCaptura.isEnabled=false
//            println("SSSSSSSSSSSSSSSSSSSSS")
            CamStatus = CameraTypes.CAMERA
        }
        comprobateItem()
        hideKeyboardFrom(contextFragment,view as View)
        TransitionManager.beginDelayedTransition(binding.SVAPNew, autoTransition)

//                binding.SVAPNew.requestLayout()

    }

    private fun startCamera() {
        cameraProviderFuture=ProcessCameraProvider.getInstance(baseActivity)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(
                    binding.PVCmain.surfaceProvider
                )
            }
            try {
                cameraProvider.unbindAll()
                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(Size(binding.PVCmain.width, binding.PVCmain.height))
                    .build()
                imageCapture.setCropAspectRatio(Rational(200, 200))
                MyCamera = cameraProvider.bindToLifecycle(
                    baseActivity,
                    cameraSelector,
                    preview,
                    imageCapture
                )

//                Esto obtiene la camara que necesitamos para obtener los datos

                MyCamera.cameraControl.cancelFocusAndMetering()

                focusControllerCamera()
            } catch (e: Exception) {
                Log.e(LOGFRAGMENT, "Error al asignar la camara", e)
            }
        }, ContextCompat.getMainExecutor(baseActivity))
    }


    private fun startCameraEscaner(MyView:View,analizer: Boolean) {
        cameraProviderFuture=ProcessCameraProvider.getInstance(baseActivity)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

//            val preview = Preview.Builder().apply {
//                setTargetResolution(Size(binding.PVCmain.width, binding.PVCmain.height))
//            }.build().also { mPreview ->
//                mPreview.setSurfaceProvider(
//                    binding.PVCmain.surfaceProvider
//                )
//            }


            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(
                    binding.PVCmain.surfaceProvider
                )
            }
//            preview.targetRotation=ROTATION_0
            Log.i(LOGFRAGMENT,"El dato es"+binding.PVCmain.width.toString())
            if(analizer) {
                imageAnalisis
                    .apply {
                        setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrResult ->

                            clearAnalyzer()
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
                                    MyView,
                                    "El codigo es ${qrResult.text}",
                                    Color.YELLOW
                                )
                                binding.TEQR.setText(qrResult.text)
                                binding.Vqrline.clearAnimation()
                                binding.BRscanner.visibility = View.VISIBLE

//                            cameraProvider.unbindAll()
                                if(ConNet.ComprobationInternet(baseActivity)) {
                                    baseActivity.returnbinding().PBbarList.visibility=View.VISIBLE
                                    Corutine = GlobalScope.launch(Dispatchers.Main) {
                                        val resultKeys = withContext(Dispatchers.IO) {
                                            ConnectToPost.ConnectAndGet(qrResult.text)
                                        }
                                        println(resultKeys)
                                    }

                                }else{
                                    Toast.makeText(contextFragment,"Sin internet!!",Toast.LENGTH_SHORT).show();
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
                    cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview,imageAnalisis)

//                cameraProvider.bindToLifecycle(baseActivity,cameraSelector,preview,imageAnalysis)
                MyCamera.cameraControl.cancelFocusAndMetering()

                focusControllerCamera()
            } catch (e: Exception) {
                Log.e(LOGFRAGMENT, "Error al intentar Asignar la camara de Escaneo",e)
            }
        }, ContextCompat.getMainExecutor(baseActivity))
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

    private fun clearItems() {
        binding.TEQR.setText("")
        binding.TENombre.setText("")
        binding.TECantidadU.setText("")
        binding.TECantidad.setText("")
        binding.TECaracteristicas.setText("")
        binding.TECategoria.setText("")
        binding.TEMarca.setText("")
        binding.TEPrecio?.setText("")
        binding.TEPrecioU.setText("")
        adapter.clearimagen()
        comprobateItem()
    }


}


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
