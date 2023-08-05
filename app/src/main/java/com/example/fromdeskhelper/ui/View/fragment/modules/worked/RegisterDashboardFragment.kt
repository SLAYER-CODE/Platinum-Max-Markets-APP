package com.example.fromdeskhelper.ui.View.fragment.modules.worked

import DatePickerFragment
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.android.ex.chips.BaseRecipientAdapter
import com.esafirm.imagepicker.features.ImagePicker
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.model.Controller.imagenController
import com.example.fromdeskhelper.data.model.Helper.MyItemTouchHelperCallback
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.data.model.base.CallBackItemTouch
import com.example.fromdeskhelper.data.model.objects.Constants.Images.REQUEST_CODE_PICKER
import com.example.fromdeskhelper.data.model.objects.Constants.Images.SELECT_FILE_IMAGE_CODE
import com.example.fromdeskhelper.data.model.objects.Form
import com.example.fromdeskhelper.databinding.FragmentRegisterDashboardBinding
import com.example.fromdeskhelper.databinding.ImageNewNewBinding
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import com.example.fromdeskhelper.ui.View.ViewModel.AgregateProductViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.LOG_VIEWMODE
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.ImageAdapter
import com.example.fromdeskhelper.ui.View.fragment.CameraFragment
import com.example.fromdeskhelper.ui.View.fragment.CameraQrFragment
import com.example.fromdeskhelper.ui.View.fragment.Client.TimePickerFragment
import com.example.fromdeskhelper.util.hideKeyboard
import com.example.fromdeskhelper.util.listener.DragAndDropListenerActions
import com.example.fromdeskhelper.util.listener.TouchDropListenerAction
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


// TODO: Argumento que sirve para nombre como registro de este archivo
private const val LOGFRAGMENT: String = "AddProducto"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class RegisterDashboardFragment : Fragment(), CallBackItemTouch {

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
            RegisterDashboardFragment().apply {
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
    private var _binding: FragmentRegisterDashboardBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context

    private var offsetViewItem: Rect = Rect()
    private var offsetViewItemTwo = Rect()


    //    Codigos para el escaneo de QR
    var name: String? = null;
    var imagePath: String? = null;

    //    Adaptardor para la lista del adaptador de las imagenes
    private var adapter = ImageAdapter(mutableListOf())

    //    Variables que definen la posicion con la ultima vista que se seleciono del RecyclerView
    private lateinit var viewHold: View;
    private var position: Int = 0;

    //    Funciones para la selecion multiple de las imagenes
    private lateinit var imagenUri: Uri;

    //    Animaciones procesan solo al ejectuar el codigo
//    private lateinit var AnimationUpCamera:ValueAnimator;
    //    Esta funcion limpi toda la pantalla incluido las imagenes de la lista


    //Distancias para la elimiancion guardado y muestra
    private var RefDeleteViewImage: Int = 1;
    private var RefMediumDistanceImage: Int = 1;
    private lateinit var offsetViewBounds: Rect;
    private var Corutine: Job? = null;


    //Cosas implementadas para mvvm
    private var HeigthIncrement: Int = 0;
    private var recycleviewWidth: Int = -1;

    private val CameraView: CameraViewModel by viewModels()
    private val AgregateProductsState: AgregateProductViewModel by viewModels(ownerProducer = { requireActivity() });

    private var CamClick: CameraTypes = CameraTypes.NULL;
    private var CamScannerStatus: Boolean = false;
    private var CategoryAdapter: MutableList<String> = mutableListOf();

    private var buttonDisable: Boolean = false;

    override fun onStart() {
//        baseActivity.binding.appBarMain.collapsingToolbar?.isTitleEnabled=false
//        baseActivity.binding.appBarMain.collapsingToolbar?.isActivated=false
//        baseActivity.binding.appBarMain.toolbarParent?.setExpanded(false)


        (baseActivity.binding.appBarMain.toolbarParent?.layoutParams as CoordinatorLayout.LayoutParams).behavior =
            null

        Log.i(LOGFRAGMENT, "Se Inicio onStart [*]")
//        (activity as MainActivity).functionFabRefresh(::clearItems);
//        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
//            clearItems()
//        }
//        baseActivity.binding.appBarMain.BIShowP.visibility = View.INVISIBLE
//        baseActivity.binding.appBarMain.refreshFab.setImageResource(R.drawable.ic_baseline_clear_all_24)


        super.onStart()
    }

    private fun addChipToGroup(person: String, chipGroup: ChipGroup, color: Int, Icon: Int) {
        val chip = Chip(context)
        chip.text = person
        chip.chipIcon = ContextCompat.getDrawable(requireContext(), Icon)
        chip.setChipBackgroundColorResource(color)
        chip.isCloseIconEnabled = true

        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//
//        Log.i(LOGFRAGMENT, "La camara es $toogleCamera")
//        AnimationUpCamera = ObjectAnimator.ofFloat(binding.despliegeCamera, "translationY", -80f)
//        AnimationUpCamera.repeatCount = ObjectAnimator.INFINITE
//        AnimationUpCamera.repeatMode = ObjectAnimator.REVERSE
//        AnimationUpCamera.duration = 800
//
//        AnimationUpIntercalate = binding.despliegeCamera.background as AnimationDrawable
//        AnimationUpIntercalate.setEnterFadeDuration(1000)
//        AnimationUpIntercalate.setExitFadeDuration(1000)
//
//        Log.i("CameraStateRegistry", cameraExecutor.isShutdown.toString())


//        Obtiene la instacia de la actividad para la base de datos

//        val daoNew = AppDatabase.getDataBase(baseActivity);


//        Estas Funciones obtiene la distancia del campo de la camara para obtener sus vectores y calcular el movimiento de la camara
//        val animation:Animation=AnimationUtils.loadAnimation(baseActivity,R.anim.slide_in_left)
//        val linearMayor:Int = binding.llinearConstraint.layoutParams.height
//        val linearMediun:Int = binding.linearConstraitButton.layoutParams.height
//        val linearGMayor:Int = binding.SVAPNew.layoutParams.height
//        val linearMenimun:Int=binding.PVCmain.layoutParams.height
//        val linearCamera:Int=binding.LayoutCamera.layoutParams.height


//        Estas son las bases para el incremento de la camara segun la perspectiva del telefono
//          HeigthIncrement = binding.LayoutCamera.layoutParams.height
        if (recycleviewWidth == -1) {
            recycleviewWidth = binding.RVCaptureImages.layoutParams.width
        }
//        imageAnalisis=ImageAnalysis.Builder().setTargetResolution(Size(HeigthIncrement, HeigthIncrement))
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()

//        val FocusEditListener =   View.OnFocusChangeListener { p0, p1 ->
//            if(!p1){
//                hideKeyboardFrom(requireContext(),view)
//            }
//        }


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


        HeigthIncrement = binding.FragmentCamera.width

        CameraView.CloseCamera.observe(viewLifecycleOwner) {
            Log.i("CARLOS", "RW" + it.toString())
            if (it) binding.CapturaLayout.visibility = View.VISIBLE
        }

        AgregateProductsState.ClearFragment.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                for (file: File in context?.cacheDir?.listFiles()!!) {
                    if (file.toUri() in adapter.MyImage) {
                        file.delete()
                    }
                }
            })


        AgregateProductsState.QRBindig.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.i("SERVICIO XD", it.toString())
            binding.TEQR.setText(it);
        })

//        AgregateProductsState.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            Log.i("VIEWMODEL SELLAMO","Se llamo al adapter"+it.toString())
////            adapter.clearimagen()
//            adapter = ImageAdapter(it)
//            binding.RVCaptureImages.adapter = adapter
//            comprobateItem()
//        })

        lifecycleScope.launch {
            AgregateProductsState.sharedFlow.collect { url ->

                Log.i(LOG_VIEWMODE, "IMAGECAPTURESEND" + url.toString())

                if (url != null) {
                    runBlocking {
                        adapter.addImage(url);
                        binding.RVCaptureImages?.scrollToPosition(0);
                        comprobateItem()
                    }

                }
                binding.RVCaptureImages.adapter = adapter

            }
        }




        AgregateProductsState.RecivImageItem.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                adapter.addImage(it);
                binding.RVCaptureImages?.scrollToPosition(0);
                comprobateItem()
            })


        CameraView.CameraActivate.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                CamClick = it
                if (it == CameraTypes.NULL) {
                    Log.i("CARLOS", "QC NULL")
                    if (!CamScannerStatus)
                        binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_addproduct)
                    binding.CapturaLayout.visibility = View.VISIBLE
                    comprobateItem()
                } else if (it == CameraTypes.CAMERA) {
                    Log.i("CARLOS", "QC CAMERA")
                    binding.CapturaLayout.visibility = View.GONE
                    binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_addproduct)
                } else if (it == CameraTypes.SCANER) {
                    Log.i("CARLOS", "QC SCANNER")
                    binding.CapturaLayout.visibility = View.VISIBLE
                    binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_close_24)
                }
            })

        CameraView.ScanerStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.i("CARLOS", "QS" + CamScannerStatus.toString())
            Log.i("CARLOS", "QS" + it.toString())
            CamScannerStatus = it;
        })

        binding.TEDescuento.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var descuento = 0f
                var precio = 0f;
                var neto = binding.TEPrecioU.text.toString()
                if (neto == "") {
                    neto = "0.0"
                }

                if (s != null && s.isNotEmpty()) {
                    descuento = s.toString().toFloat()
                }

                if (binding.TEPrecio.text != null && binding.TEPrecio.text!!.isNotEmpty()) {
                    precio = binding.TEPrecio.text.toString().toFloat()
                }

                if ((precio - neto.toFloat()) >= descuento) {
                    binding.TVpreciodisconut.text = (precio - descuento).toString()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }
        )

        binding.TEPrecioU.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var price = binding.TEPrecio.text.toString().replace(" ","")
                if(price!=".") {
                    var price_neto = binding.TEPrecioU.text.toString()
                    if (price == "") {
                        price = "0.0"
                    }
                    if (price_neto == "") {
                        price_neto = "0.0"
                    }
                    var ganace = price
                        .toDouble() - price_neto.toDouble()
                    binding.TVGanancia.text = ("Ganacia: $ganace")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        fun configButtonSend() {
            var check = (binding.CSaveStorage.isChecked or binding.CSaveLocal.isChecked or binding.CSaveServer.isChecked)
            binding.BAgregate.isEnabled = check  && (binding.TENombre.text.toString().replace(" ","") != "" && binding.TEPrecio.text.toString() != "" &&  binding.TEPrecio.text.toString() != "." &&  binding.TEPrecio.text.toString().toFloat().toInt().toString().length <=7 )
            if(check){
                binding.ValidateCheck?.visibility=View.GONE
            }else{
                binding.ValidateCheck?.visibility=View.VISIBLE
            }
        }
        binding.TEPrecio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().replace(" ","") != "" && s.toString()!="." ) {
                    if( (s.toString().toDouble().toInt().toString()).length<=7){
                        binding.priceHelper?.helperText=""
                    }else{
                        binding.priceHelper?.helperText="El precio tiene que ser menor"
                    }
                } else {
                    binding.priceHelper?.helperText="Requerido para su registro"
                }
                configButtonSend()
                binding.TEPrecioU.setText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                var descuento = 0f
                var precio = 0f;
                if (s.toString().replace(" ","") != "" && s.toString()!="." && s!=null ) {
                    precio = s.toString().toFloat()
                    if (binding.TEDescuento.text != null && binding.TEDescuento.text!!.isNotEmpty()) {
                        descuento = binding.TEDescuento.text.toString().toFloat()
                    }
                    binding.TVpreciodisconut.text = (precio - descuento).toString()
                }

            }
        })



        binding.TENombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Cuando preciona la tecla para la ejecucion de los datos
                configButtonSend()
                if (s.toString().replace(" ","") != "") {
                    binding.Toolbar?.title = "$s (Borrador)"
                    binding.nameHelper?.helperText=""
                } else {
                    binding.nameHelper?.helperText="Requerido para su registro"
                    binding.Toolbar?.title = "Agregar Producto"
                }


            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })



        AgregateProductsState.CategoriesGetApp()
        val baseRecipientAdapter =
            BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_EMAIL, baseActivity)


        binding.TECategoria.setOnItemClickListener { parent, arg1, position, arg3 ->
            binding.TECategoria?.text = null
            val selected = parent.getItemAtPosition(position) as String
            addChipToGroup(
                selected,
                binding.chipGroup2!!,
                R.color.md_blue_500,
                R.drawable.ic_baseline_spellcheck_24
            )
        }

        binding.TECategoria.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (binding.TECategoria.text.toString() in CategoryAdapter) {
                        addChipToGroup(
                            binding.TECategoria.text.toString(), binding.chipGroup2!!,
                            R.color.md_blue_500, R.drawable.ic_baseline_recommend_addproducto
                        )
                    } else {
                        addChipToGroup(
                            binding.TECategoria.text.toString(),
                            binding.chipGroup2!!,
                            R.color.md_red_200,
                            R.drawable.ic_baseline_close_24
                        )
                    }
                    binding.TECategoria.setText("");
                    return true
                }
                return false
            }
        })
        binding.TEMarca.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (binding.TEMarca.text.toString() in CategoryAdapter) {
                        addChipToGroup(
                            binding.TEMarca.text.toString(), binding.ChipGroupMarca!!,
                            R.color.md_blue_500, R.drawable.ic_baseline_recommend_addproducto
                        )
                    } else {
                        addChipToGroup(
                            binding.TEMarca.text.toString(),
                            binding.ChipGroupMarca!!,
                            R.color.md_red_200,
                            R.drawable.ic_baseline_close_24
                        )
                    }
                    binding.TEMarca.setText("");
                    return true
                }
                return false
            }
        })

        AgregateProductsState.ListCategories.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                CategoryAdapter.clear()
                for (x in it.categoria) {
                    CategoryAdapter.add(x.category_name)
                }
                val adapterchip = ArrayAdapter<String>(
                    baseActivity,
                    android.R.layout.simple_dropdown_item_1line, CategoryAdapter
                )
                binding.TECategoria?.setAdapter<ArrayAdapter<String>>(adapterchip)
            }
        )

        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d/M/yyyy H:m") // Make sure user insert date into edittext in this format.


        fun buttonAnimation(saved: Boolean) {
            val transtion = AutoTransition().setDuration(350);
            val lpHide = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            )

            val lpShow = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            if (saved) {

                binding.LLDAvalide?.layoutParams = lpShow
                binding.BAgregate.layoutParams = lpHide
            } else {
                binding.LLDAvalide?.layoutParams = lpHide
                binding.BAgregate.layoutParams = lpShow
            }

            TransitionManager.beginDelayedTransition(binding.contentButtonAddLinear, transtion)
            binding.contentButtonAddLinear?.requestLayout()
        }

        fun buttonAnimationEditEnable(edit: Boolean) {
            val transtion = AutoTransition().setDuration(350);
            val lpHide = LinearLayout.LayoutParams(
                0,
                0,
                )
            val lpShow = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            lpShow.weight=1f
            lpShow.setMargins(15)
            if (edit) {

//                binding.LLDAvalide?.layoutParams = lpShow
                val drawable =
                    ContextCompat.getDrawable(baseActivity, R.drawable.ic_baseline_save_24)
                binding.BEditOldProduct?.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
                binding.BEditOldProduct?.text = getText(R.string.addEditarUltimoLive)
                binding.BAgregateNewProduct?.layoutParams = lpHide

            } else {
                val drawable =
                    ContextCompat.getDrawable(baseActivity, R.drawable.ic_baseline_edit_24)
                binding.BEditOldProduct?.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
                binding.BEditOldProduct?.text = getText(R.string.addEditarUltimo)
                binding.BAgregateNewProduct?.layoutParams = lpShow
//                binding.BEditOldProduct?.layoutParams = lpShow
            }

            TransitionManager.beginDelayedTransition(binding.LLDAvalide, transtion)
            binding.LLDAvalide?.requestLayout()
        }


        var count = 0
        AgregateProductsState.AgregateState.observe(viewLifecycleOwner, Observer {

            val iter =
                (if (binding.CSaveStorage.isChecked) 1 else 0) +
                        (if (binding.CSaveServer.isChecked) 1 else 0) +
                        (if (binding.CSaveLocal.isChecked) 1 else 0)

            count += if (it.state) 1 else 0
            statustems(false)



























            
            buttonAnimation(true)
            count = 0
        })


        binding.BAgregateNewProduct?.setOnClickListener {
            ObjectAnimator.ofInt(
                binding.SVAPNew,
                "scrollY",
                binding.SVAPNew.scrollY, 0
            ).setDuration(250).start().apply {
                clearItems()
            }
            statustems(true)
            buttonAnimation(false)
        }

        binding.BEditOldProduct?.setOnClickListener {
            AgregateProductsState.editing()
        }

        AgregateProductsState.EditingState.observe(viewLifecycleOwner, Observer {
            if(it) {
                ObjectAnimator.ofInt(
                    binding.SVAPNew,
                    "scrollY",
                    binding.SVAPNew.scrollY, 0
                ).setDuration(250).start()
            }

            statustems(it)
            buttonAnimationEditEnable(it)

        })


        AgregateProductsState.State.observe(viewLifecycleOwner, Observer {
            binding.TEQR.setText(it.qr)
            binding.TENombre.setText(it.name)
            binding.TECantidad.setText(it.stockcantidad)
            binding.TECaracteristicas.setText(it.detalles)
            binding.TECategoria.setText("")
            binding.TEMarca.setText("")

            binding.TEPrecio.setText(it.precio)
            binding.TEPrecioU.setText(it.precioNeto)


            binding.TEDescuento.setText(it.disconut)
            binding.CEnvios?.isChecked = it.available_shipment
            binding.CAhora?.isChecked = it.available_now

            if (it.available_date != null) {

                var date = it.available_date
                binding.etPlannedDate?.setText(date.dayOfMonth.toString() + "/" + date.month.toString() + "/" + date.year.toString())
                binding.etTimePicker?.setText(date.hour.toString() + "/" + date.minute.toString())
                binding.CDespues?.isChecked = true
            }

            binding.SPCantidad.setSelection(it.stockC_attr)
            binding.TEContenido.setText(it.stockU)
            binding.SPTipo.setSelection(it.stockU_attr)


            binding.TePeso?.setText(it.peso)
            binding.SPpeso.setSelection(it.peso_attr)

            binding.SPCActual.setSelection(it.stock_attr)


//        binding.BEscaner.isEnabled

//        binding.BCaptura.isEnabled = status
//        binding.BAgregateFile.isEnabled = status

            binding.CSaveStorage.isChecked = it.Storage
            binding.CSaveServer.isChecked = it.SServer
            binding.CSaveLocal.isChecked = it.SLocal

            binding.RVCaptureImages.adapter = ImageAdapter(it.mutableList)
            comprobateItem()
        })

//        Botton que agrega los productos
        binding.BAgregate.setOnClickListener {

            val _name: String = binding.TENombre.text.toString()
            val _precio: String = binding.TEPrecio.text.toString()

            var _precioNeto: String = binding.TEPrecioU.text.toString()
            var _detalles: String = binding.TECaracteristicas.text.toString()
            var _qr: String = binding.TEQR.text.toString()

            var _discount: String = binding.TEDescuento.text.toString()

            var _stock: String = binding.TECantidad.text.toString()
            var _stock_valname: Int = binding.SPCantidad.selectedItemPosition

            var _stockU: String = binding.TEContenido.text.toString()
            var _stockU_valname: Int = binding.SPTipo.selectedItemPosition

            var peso: String = binding.TePeso?.text.toString()
            var peso_valname: Int = binding.SPpeso.selectedItemPosition

            var StockDiposed: Int = binding.SPCActual.selectedItemPosition


//            Disponibilidad
            val CEnvios: Boolean = binding.CEnvios?.isChecked == true;
            val CAhora: Boolean = binding.CAhora?.isChecked == true;
            val CDespues: Boolean = binding.CDespues?.isChecked == true;


            //Formateando datos para ser enviados

//            if(_precioNeto==""){
//                _precioNeto=null
//            }
//            if(_detalles==""){
//                _detalles=null
//            }
//
//            if(_qr==""){
//                _qr=null
//            }
//            if(_discount==""){
//                _discount=null
//            }
//            if(_stock==""){
//                _stock=null
//            }
//            if(_stockU==""){
//                _stockU=null
//            }
//            if(peso==""){
//                peso=null
//            }

            var formaterDate: LocalDateTime? = null
            if (CDespues) {
                var fechaItem = binding.etPlannedDate?.text.toString()
                var time = binding.etTimePicker?.text.toString()

                if (fechaItem == "") {
                    formaterDate = null
                } else {
                    if (time == "") {
                        time = "00:00"
                    }
                    formaterDate = LocalDateTime.parse((fechaItem + " " + time), formatter)
                }
            }

            var uid = UUID.randomUUID().toString()


//            Log.i("FORMATERADD",_discount.toString())
//            Log.i("FORMATERADD",_stock_valname.toString())
//            Log.i("FORMATERADD",_stockU_valname.toString())
//            Log.i("FORMATERADD",peso.toString())
//            Log.i("FORMATERADD",peso_valname.toString())

            var caTegorys = mutableListOf<CategoriesInput>()
            var marCas = mutableListOf<BrandsInput>()

            binding.chipGroup2.children.toList().forEach {
                caTegorys.add(CategoriesInput((it as Chip).text.toString()))
            }
            binding.ChipGroupMarca.children.toList().forEach {
                marCas.add(BrandsInput((it as Chip).text.toString()))
            }

            AgregateProductsState.SaveCamp(
                Form(
                    _name,
                    _precio,
                    _precioNeto,
                    _discount,

                    _stock,
                    _stock_valname,

                    _stockU,
                    _stockU_valname,
                    peso,
                    peso_valname,

                    StockDiposed,
                    _detalles,
                    _qr,
                    CEnvios,
                    CAhora,
                    formaterDate,
                    uid,
                    caTegorys,
                    marCas,
                    adapter.MyImage,
                    binding.CSaveStorage.isChecked,
                    binding.CSaveServer.isChecked,
                    binding.CSaveLocal.isChecked
                )
            )


        }


//        var CheckListener = object :
        binding.CSaveStorage?.setOnCheckedChangeListener { buttonView, isChecked ->
            configButtonSend()
        }
        binding.CSaveLocal?.setOnCheckedChangeListener { buttonView, isChecked ->
            configButtonSend()
        }
        binding.CSaveServer?.setOnCheckedChangeListener { buttonView, isChecked ->
            configButtonSend()
        }

        AgregateProductsState.ButtonAgregateState.observe(
            viewLifecycleOwner, Observer {
                binding.BAgregate.isEnabled = it
            })

        binding.BAgregateFile.setOnClickListener {
//            Agregar imagen usando pikaso pero no funciona en dispositvos android como el que tengo yo android 10 crakeado
//            ImagePicker.create(this).start(REQUEST_CODE_PICKER)     // Activity or Fragment
//            Asi que se uso esta actividad para tener un enfoque global de todas las demas
            imagenController.selectFromPothoGallery(this, SELECT_FILE_IMAGE_CODE)
            Log.d(LOGFRAGMENT, "Se preciono la selecion de imagenes")
        }


        binding.DeleteDrag.setOnDragListener(DragAndDropListenerActions(baseActivity));
        binding.SaveDrag.setOnDragListener(DragAndDropListenerActions(baseActivity));
        binding.DeleteDrag.setOnTouchListener(TouchDropListenerAction())
        binding.SaveDrag.setOnTouchListener(TouchDropListenerAction())


        var adapterSPeso = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spiner_peso,
            resources.getStringArray(R.array.dlagsPeso)
        )
        binding.SPpeso.adapter = adapterSPeso

        var adapterSCantidad = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spiner_peso,
            resources.getStringArray(R.array.dlagsCantidad)
        )
        binding.SPCantidad.adapter = adapterSCantidad

        var adapterSType = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spiner_peso,
            resources.getStringArray(R.array.dlagsType)
        )

        binding.SPTipo.adapter = adapterSType

        var adapterSParts = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spiner_peso,
            resources.getStringArray(R.array.dlagsParts)
        )

        binding.SPCActual.adapter = adapterSParts

        binding.SPCantidad.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.SPpeso.setSelection(5)
                    }

                    1 -> {
                        binding.SPpeso.setSelection(3)
                    }

                    2 -> {
                        binding.SPpeso.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })


//        binding.SPCantidad.setSelection(1)


//        Bindig para el recyclerview de las imagenes
        binding.RVCaptureImages.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)


        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                println("CARLOSS")
            }
        })

        var RVCallback: ItemTouchHelper.Callback = MyItemTouchHelperCallback(this);
        var RVTouchHelper: ItemTouchHelper = ItemTouchHelper(RVCallback);
        RVTouchHelper.attachToRecyclerView(binding.RVCaptureImages);

//        binding.SVAPNew.isSmoothScrollingEnabled = true


//      Start camera ESCANNER()
        binding.BEscaner.setOnClickListener {
            view?.let { activity?.hideKeyboard(it) }

            if (!CamScannerStatus) {
                val ft: FragmentTransaction = childFragmentManager.beginTransaction()

//                ft.setCustomAnimations(
//                    android.R.anim.fade_in,
//                    android.R.anim.fade_out,
//                )

                val DesplazeAnimation =
                    ObjectAnimator.ofInt(
                        binding.SVAPNew,
                        "scrollY",
                        binding.SVAPNew.scrollY,
                        binding.SVAPNew.height + HeigthIncrement
                    ).setDuration(650)
//            DesplazeAnimation.setAutoCancel(true)
                CameraView.CloseInFragment(false)
                ft.replace(R.id.FragmentCamera, CameraQrFragment());
                ft.commitAllowingStateLoss()
                if (baseActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    (baseActivity.binding.appBarMain.fab.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
//                        0,
//                        0,
//                        dpToPx(360),
//                        dpToPx(20)
//                    )
//                    (baseActivity.binding.appBarMain.toolbarParent?.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
//                        0,
//                        0,
//                        dpToPx(340),
//                        dpToPx(0)
//                    )
                } else {
                    if (CamClick == CameraTypes.NULL) DesplazeAnimation.start()
                }
//                CameraView.ActivateCamera()
//                CamClick=CameraTypes.SCANER
                CameraView.CamaraStatus(CameraTypes.SCANER, true)
            } else {
                Log.i("CARLOS", "SE LLAMO A CERRAR EL FRAGMENTO")
                CameraView.CamaraStatus(CameraTypes.NULL, false)
                CameraView.CloseInFragment(true)
//                binding.CapturaLayout.visibility=View.VISIBLE
            }
        }



        fun onDateSelected(day: Int, month: Int, year: Int) {
            binding.etPlannedDate?.setText("$day/$month/$year")
        }

        fun showDatePickerDialog() {
            val datePicker =
                DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
            datePicker.show(parentFragmentManager, "datePicker")
        }

        fun onTimeSelected(time: String) {
            binding.etTimePicker?.setText("$time")
        }

        fun showTimePickerDialog() {
            val timePicker = TimePickerFragment { onTimeSelected(it) }
            timePicker.show(parentFragmentManager, "timePicker").apply {
                showDatePickerDialog()
            }
        }
        binding.CAhora?.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.CDespues?.text =
                    getString(R.string.agregate_product_check_available_despues_new)
            } else {
                binding.CDespues?.text =
                    getString(R.string.agregate_product_check_available_despues)
            }
        }

        binding.CDespues?.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                if (binding.etTimePicker?.text.toString() == "H:M" && binding.etPlannedDate?.text.toString() == "DD/MM/AAAA") {
                    showTimePickerDialog()
                }
            } else {
                binding.etTimePicker?.setText("H:M")
                binding.etPlannedDate?.setText("DD/MM/AAAA")
            }
        }


//        startCamera()
        binding.BCaptura.setOnClickListener {
            //Children
//            openCamera()
            view?.let { activity?.hideKeyboard(it) }
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()

//            ft.setCustomAnimations(
//                android.R.anim.fade_in,
//                android.R.anim.fade_out,
//            )

            val DesplazeAnimation =
                ObjectAnimator.ofInt(
                    binding.SVAPNew,
                    "scrollY",
                    binding.SVAPNew.scrollY,
                    binding.SVAPNew.height + HeigthIncrement
                ).setDuration(650)
//            DesplazeAnimation.setAutoCancel(true)
            CameraView.CloseInFragment(false)
            ft.replace(R.id.FragmentCamera, CameraFragment());
//            ft.setReorderingAllowed(true)
            ft.commitAllowingStateLoss()
            if (baseActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                (baseActivity.binding.appBarMain.fab.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
//                    0,
//                    0,
//                    dpToPx(360),
//                    dpToPx(20)
//                )
//                (baseActivity.binding.appBarMain.toolbarParent?.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
//                    0,
//                    0,
//                    dpToPx(340),
//                    dpToPx(0)
//                )
            } else {
                if (CamClick == CameraTypes.NULL) DesplazeAnimation.start()
            }
//            CameraView.ActivateCamera()
            CamClick = CameraTypes.CAMERA
            CameraView.CamaraStatus(CameraTypes.CAMERA, false)
            //Sirve para animar la camara al abrir

        }

//        binding.BRscanner.setOnClickListener {
//            binding.BRscanner.visibility=View.INVISIBLE
//            openCameraScanner(view);
//        }


//        view.doOnPreDraw {
//            startPostponedEnterTransition()
//        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun removeAllChildFragments() {
        repeat(childFragmentManager.backStackEntryCount) {
            childFragmentManager.popBackStack()
        }
    }

    fun dpToPx(dp: Int): Int {
        val scale = baseActivity.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun comprobateItem() {
        if (adapter.MyImage.size == 0) {
            binding.TNotItems.visibility = View.VISIBLE;
        } else {
            binding.TNotItems.visibility = View.INVISIBLE;
        }
    }

    override fun itemTouchMode(oldPosition: Int, newPosition: Int) {
        position = newPosition
        adapter.MyImage.add(oldPosition, adapter.MyImage.removeAt(newPosition))
        adapter.notifyItemMoved(oldPosition, newPosition);
        comprobateItem()
//        super.itemTouchMode(oldPosition, newPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onDrop(eliminated: Boolean, position: Int) {
        if (eliminated) {
            adapter.removeImagen(position)
        }

    }

    override fun prepareViews(views: Boolean, viewholds: RecyclerView.ViewHolder?) {
        if (viewholds != null) {
//            image_new_new
            viewHold = ImageNewNewBinding.bind(viewholds.itemView).viewParent
            position = viewholds.absoluteAdapterPosition
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

        val transtion = AutoTransition().setDuration(350);
        if (views) {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            binding.DeletAndSaveParent.layoutParams = lp
//            binding.LEandI.layoutParams = lp
        } else {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            )
            binding.DeletAndSaveParent.layoutParams = lp
//            val lpDat = LinearLayout.LayoutParams(
//                0,
//                0
//            )
//            binding.LEandI.layoutParams = lpDat
        }

        TransitionManager.beginDelayedTransition(binding.linearConstraitButton, transtion)
        binding.DeletAndSaveParent.requestLayout()
    }

    override fun isItemSelected(posX: Float, posY: Float): Int {
        println("Segundo $posX")
        println("Segundo $posY")
        var pwd = 0
//        ESTO TIENE QUE IR AFUERA POR QUE ES PARTE DEL LISTVIEW
//        if(!toogleCamera){
//            pwd+=330
//        }


        var MMDImagenY = posX + (viewHold.width / 2)
        var TABCalc =
            (offsetViewBounds.top - 50) <= ((offsetViewItem.top + posY) + (viewHold.width / 2)) && (offsetViewBounds.bottom + 100) >= ((offsetViewItem.top + posY) + (viewHold.width / 2))
        if (TABCalc && offsetViewBounds.left - pwd <= MMDImagenY && offsetViewBounds.right - pwd >= MMDImagenY) {
            val Final: Uri = adapter.MyImage.get(position)
            //back que del item que se elimina para que pueda ser cancelado
            val indexDelete: Int = position;
            //removimiento el item del reciclerview
            adapter.removeImagen(indexDelete)
            var RestoreSnackbar: Snackbar =
                Snackbar.make(requireView(), ".../" + Final.path, Snackbar.LENGTH_LONG)
            RestoreSnackbar.setAction("Cancelar", View.OnClickListener {
                adapter.restoreItem(Final, indexDelete);
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
        if (TABCalc && offsetViewItemTwo.left - pwd <= MMDImagenY && offsetViewItemTwo.right - pwd >= MMDImagenY) {
            val filepath = Environment.getExternalStorageDirectory().absolutePath
            val externalPath: String = "/MhImagenes/" + adapter.MyImage[position].lastPathSegment

            return 0;
        }

        return -1;
    }

    override fun CalculateArea(posX: Float, posY: Float) {
        RefMediumDistanceImage = offsetViewBounds.top
        var diferencia = (RefMediumDistanceImage + (posY + offsetViewItem.bottom))
        var dato = (diferencia * 32) / (RefMediumDistanceImage + offsetViewItem.bottom)
        var scale = dato * 0.031f

        if (scale > 0.35 && scale < 1.40) {
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
        var MMDImagenY = posX + (viewHold.width / 2)
        var TABCalc =
            (offsetViewBounds.top - 50) <= ((offsetViewItem.top + posY) + (viewHold.width / 2)) && (offsetViewBounds.bottom + 100) >= ((offsetViewItem.top + posY) + (viewHold.width / 2))
        val ColorSave = Color.rgb(72, 134, 255)
        val ColorDelete = Color.rgb(171, 0, 0)
        if (TABCalc && offsetViewBounds.left - pwd <= MMDImagenY && offsetViewBounds.right - pwd >= MMDImagenY) {
            binding.DeleteDrag.setTextColor(Color.RED)
            binding.DeleteDrag.setShadowLayer(40f, -6f, 0f, Color.RED)
//            binding.DeleteDrag.compoundDrawables[2].colorFilter =
//                PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
//            binding.DeleteDrag.compoundDrawables[2].bounds = Rect(0, 0, 100, 100)
        } else {
//            binding.DeleteDrag.compoundDrawables[2].bounds = Rect(0, 0, 70, 70)
//            binding.DeleteDrag.compoundDrawables[2].colorFilter =
//                PorterDuffColorFilter(ColorDelete, PorterDuff.Mode.SRC_IN)
            binding.DeleteDrag.setTextColor(ColorDelete)
        }
        if (TABCalc && offsetViewItemTwo.left - pwd <= MMDImagenY && offsetViewItemTwo.right - pwd >= MMDImagenY) {
            binding.SaveDrag.setTextColor(Color.CYAN)
            binding.SaveDrag.setShadowLayer(40f, -6f, 0f, Color.CYAN)
//            binding.SaveDrag.compoundDrawables[0].colorFilter =
//                PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN)
//            binding.SaveDrag.compoundDrawables[0].bounds = Rect(0, 0, 100, 100)
        } else {
//            binding.SaveDrag.compoundDrawables[0].bounds = Rect(0, 0, 70, 70)
//            binding.SaveDrag.compoundDrawables[0].colorFilter =
//                PorterDuffColorFilter(ColorSave, PorterDuff.Mode.SRC_IN)
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
        Log.d(LOGFRAGMENT, "Se apreto Agregar producto")
        return when (item.itemId) {
            R.id.MBSettings -> {
                baseActivity.runOnUiThread {
                    Log.d(LOGFRAGMENT, "Se apreto Agregar producto")
                    val navBuilder = NavOptions.Builder()
                    baseActivity.navController.navigate(
                        R.id.SecondFragment,
                        null,
                        navBuilder.build()
                    )
                }
                true
            }

            else -> {
                Log.d(LOGFRAGMENT, "No se seleciono nada")
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

        Log.i(LOGFRAGMENT, "Se Ejecuta OnCreate [*]")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            parametro = it.getString("titlenumber")

        }
//        Corutine= null;
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterDashboardBinding.inflate(inflater, container, false)
        activity?.setTitle("Agregar Producto [$parametro]")

        val navcontroller = findNavController()
        val appbarlayout = AppBarConfiguration(navcontroller.graph)
        binding.Toolbar?.setupWithNavController(navcontroller, appbarlayout)
//        baseActivity.setSupportActionBar(binding.Toolbar)
        Log.i("Añandiendo instrucciones", "se Cambio la barra")

        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i(LOGFRAGMENT, "Se Guardo la instancia onSaveInstanceState [*]")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(LOGFRAGMENT, "Codigo iniciado")
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

        if (requestCode == SELECT_FILE_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
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
        Log.i(LOGFRAGMENT, "Resumiendo la actividad")
        if (CamClick == CameraTypes.CAMERA) {
            Log.i(LOGFRAGMENT, "La camra es TYCAMERA")
            binding.CapturaLayout.visibility = View.GONE
            comprobateItem()
        } else if (CamClick == CameraTypes.SCANER) {
            comprobateItem()
        }
//        println(PermisosCamera)
//        println(CamClick)
//
//        if(PermisosCamera) {
//            if (Constrains.REQUIERED_PERMISSIONS.all {
//                    ContextCompat.checkSelfPermission(
//                        baseActivity, it
//                    ) == PackageManager.PERMISSION_GRANTED
//                }) {
//                    if(CamClick==CameraTypes.SCANER){
//                        binding.LayoutCamera.visibility = View.VISIBLE
//                        binding.BEscaner.setBackgroundResource(R.drawable.ic_baseline_close_24)
//
////                        startCameraEscaner(view as View)
//                        if(null==Corutine) {
//                            openCameraScanner(view as View,true)
////                            binding.constraintqr.visibility=View.VISIBLE
////                            binding.BRscanner.visibility = View.VISIBLE
//                        }else{
//                            openCameraScanner(view as View,false)
//                            binding.Vqrline.clearAnimation()
//                            binding.BRscanner.visibility = View.VISIBLE
//                            imageAnalisis.clearAnalyzer()
//                        }
////                        startCameraEscaner(view as View)
//
//                    }
//                    if(CamClick==CameraTypes.CAMERA){
//                        binding.LayoutCamera.visibility = View.VISIBLE
//                        openCamera()
//                    }
//                Log.i(LOGFRAGMENT, "Permisos de camera")
//            }else{
//                messageSnackBar(view as View,"Se necesita acceso a la camara...",Color.RED)
//            }
////            PermisosCamera=true
//        }

        super.onResume()
    }

    override fun onPause() {
        Log.i("CameraStateRegistry", "Pauseando la acitividad")

        //LOGGER PARA APAGAR LA CAMARA
//        Log.i("CameraStateRegistry",cameraExecutor.isTerminated.toString())
        Log.i(LOGFRAGMENT, "Se pause onPause [*]")

//        //ESTO TIENE QUE IR DENTRO DE LA CAMARA
//        if (CamStatus != CameraTypes.NULL) {
//            cameraProvider.unbindAll()
//        }
        super.onPause()
    }

    override fun onStop() {
        Log.i(LOGFRAGMENT, "Se paro onStop [*]")
        super.onStop()
    }

    override fun onDestroy() {

        Log.i(LOGFRAGMENT, "Se destruyo el fragmento onDestroy [*]")
        baseActivity.datasize -= 1

        Corutine?.cancel()


//      Se traslado al Viewmodel :v
//        for (file: File in context?.cacheDir?.listFiles()!!) {
////            println(file.path.toString())
////            println(file.name)
////            println(file.absolutePath)
////            println(file.absoluteFile)
////            println(file.canonicalPath)
//            if (file.toUri() in adapter.MyImage) {
//                file.delete()
//            }
//        }


//        if(Corutine!=null){
//            if(Corutine!!.isActive){
//                println("Esta activo")
//                Corutine!!.cancel("Se Cancelo")
//            }
//        }

//        Esto va si dentro pro que la camara se destruyo
//
//
//        if (CamStatus != CameraTypes.NULL) {
//            Log.i("CameraStateRegistry","Se cerro la camara al destruir la vista [*]")
////            cameraProvider.shutdown()
////            cameraProvider.unbindAll()
//        }
        super.onDestroy()
    }

    override fun onDestroyView() {

        Log.i(LOGFRAGMENT, "Se destruyo la vista onDestroyView [*]")
        super.onDestroyView()
//        println("SE DESTRUYO LA VISTA")
//         for (file:File in context?.cacheDir?.listFiles()!!){
//             file.delete()
//         }
//        cameraProvider.unbindAll()

        _binding = null
    }


    private fun clearItems() {
        binding.TEQR.setText("")
        binding.TENombre.setText("")
        binding.TECantidad.setText("")
        binding.TECaracteristicas.setText("")
        binding.TECategoria.setText("")
        binding.TEMarca.setText("")
        binding.TEPrecio.setText("")
        binding.TEPrecioU.setText("")


        binding.TEDescuento.setText("")
        binding.CEnvios?.isChecked = false
        binding.CAhora?.isChecked = false

        binding.CDespues?.isChecked = false

        binding.SPCantidad.setSelection(0)
        binding.TEContenido.setText("")
        binding.SPTipo.setSelection(0)


        binding.TePeso?.setText("")
        binding.SPpeso.setSelection(0)

        binding.SPCActual.setSelection(0)
//        binding.BEscaner.isEnabled

//        binding.BCaptura.isEnabled = status
//        binding.BAgregateFile.isEnabled = status

        binding.CSaveStorage.isChecked = true
        binding.CSaveServer.isChecked = false
        binding.CSaveLocal.isChecked = false



        binding.RVCaptureImages.requestLayout()
        adapter.clearimagen()
        comprobateItem()
    }

    private fun statustems(status: Boolean) {
        binding.TEQR.isEnabled = status
        binding.TENombre.isEnabled = status
        binding.TECantidad.isEnabled = status
        binding.TECaracteristicas.isEnabled = status
        binding.TECategoria.isEnabled = status
        binding.TEMarca.isEnabled = status
        binding.TEPrecio.isEnabled = status
        binding.TEPrecioU.isEnabled = status


        binding.TEDescuento.isEnabled = status
        binding.CEnvios?.isEnabled = status
        binding.CAhora?.isEnabled = status
        binding.CDespues?.isEnabled = status

        binding.SPCantidad.isEnabled = status
        binding.TEContenido.isEnabled = status
        binding.SPTipo.isEnabled = status

        binding.TePeso?.isEnabled = status
        binding.SPpeso.isEnabled = status

        binding.SPCActual.isEnabled = status
        binding.BEscaner.isEnabled = status

        binding.BCaptura.isEnabled = status
        binding.BAgregateFile.isEnabled = status

        binding.CSaveStorage.isEnabled = status
        binding.CSaveServer.isEnabled = status
        binding.CSaveLocal.isEnabled = status
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
