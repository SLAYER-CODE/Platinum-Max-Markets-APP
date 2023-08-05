package com.example.fromdeskhelper.ui.View.fragment

import com.example.fromdeskhelper.ui.View.adapter.DetallesAdapterImagen
import Data.ImagenesProduct
import Data.InventarioProducts
import Data.Producto
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentDetallesProductsBinding
import com.example.fromdeskhelper.databinding.FragmentProductosShowBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.util.MessageSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class DetallesProducto : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //    private lateinit var database: AppDatabase;
    private lateinit var producto: Producto;
    private lateinit var imagenes: List<ImagenesProduct>
    private lateinit var baseActivity: EmployedMainActivity
    private lateinit var contextFragment: Context
    private var _binding: FragmentDetallesProductsBinding? = null
    private lateinit var productoLiveData: LiveData<InventarioProducts>;
    private val binding get() = _binding!!
    private var editable: Boolean = false;
    private lateinit var adapter: DetallesAdapterImagen
    private var uid: Int = -1;
    private var animation: Int = 0;
    private val MainModel: ShowMainViewModel by viewModels();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        arguments?.let {
            uid = it.getInt("uid");
            animation = it.getInt("animation")
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        if (animation == 1) {
            val animation = TransitionInflater.from(context).inflateTransition(
                android.R.transition.move
            )
            sharedElementReturnTransition = animation
            sharedElementEnterTransition = animation
        }

    }
//
//    override fun onCreateContextMenu(
//        menu: ContextMenu,
//        v: View,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//    }

    //    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        menu.clear()
//        inflater.inflate(R.menu.menu_detalles, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
    fun setEditableIcon(menuItem: AppCompatButton) {
        val id: Int = if (editable) R.drawable.ic_baseline_done_all_24 else R.drawable.ic_baseline_edit_24
        menuItem.setCompoundDrawables(baseActivity.getDrawable(id),null,null,null)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    private fun editText(bool: Boolean) {
        binding.TTNombre.isEnabled = bool
        binding.TTPrecio.isEnabled = bool
        binding.TTPrecioUn.isEnabled = bool
        binding.TTMarca.isEnabled = bool
        binding.TTCaracteristicas.isEnabled = bool
        binding.TTCategoria.isEnabled = bool
        binding.TTStock.isEnabled = bool
        binding.TTStockU.isEnabled = bool
        binding.TTQR.isEnabled = bool
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.MBSettings -> {
                true
            }

            R.id.MBEditAndSave -> {
                editable = !editable
                editText(editable)

//                setEditableIcon(item)
                if (!editable) {

                    runBlocking {
                        val _name: String = binding.TTNombre.text.toString()
                        val _precio: String = binding.TTPrecio.text.toString()
                        val _precioU: String = binding.TTPrecioUn.text.toString()
                        val _marca: String = binding.TTMarca.text.toString()
                        val _detalles: String = binding.TTCaracteristicas.text.toString()
                        val _categoria: String = binding.TTCategoria.text.toString()
                        val _stock: String = binding.TTStock.text.toString()
                        val _stockU: String = binding.TTStockU.text.toString()
                        val _qr: String = binding.TTQR.text.toString()
                        val _image: ByteArray = ByteArray(0)

                        val name = _name
                        val precio: Double? = _precio.toDoubleOrNull()
                        val precioU: Double = if (_precioU.isEmpty()) 0.0 else _precioU.toDouble()
                        val marca: String = _marca
                        val detalles: String = _detalles
                        val categoria: String = _categoria
                        val stock: Int = if (_stock.isEmpty()) 1 else _stock.toInt()
                        val stockU: Int = if (_stockU.isEmpty()) 0 else _stockU.toInt()
                        val qr: String = _qr
//                                val image:List<ImagenesNew> = listOf<ImagenesNew>(ImagenesNew(_image, Date()))
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                if (!(name.isEmpty() || precio == null)) {
                                    val NewProducto: Producto = Producto(
                                        Date(), name,
                                        precio,
                                        precioU,
                                        null,
                                        null,
                                        null,
                                        stock,
                                        stockU,
                                        null,
                                        null,
                                        null,
                                        null,
                                        qr,
                                        null,
                                        null,
                                        null,
                                        ""
                                    )
//                                            MainModel.productosData().update(NewProducto);
                                    MainModel.updateProduct(NewProducto)
//                                            baseActivity.runOnUiThread {
                                    MessageSnackBar(
                                        view as View,
                                        "Se actualizo Correctamente",
                                        Color.GREEN
                                    )
//
//                                                Toast.makeText(
//                                                    activity,
//                                                    "Se actualizo correctamente!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
                                } else {
                                    MessageSnackBar(
                                        view as View,
                                        "Tiene que rellenar precio o nombre",
                                        Color.GREEN
                                    )
//                                            baseActivity.runOnUiThread {
//                                                Toast.makeText(activity,"Tiene que rellenar Precio o nombre",Toast.LENGTH_SHORT).show()
//                                            }
                                }
                            } catch (Ex: Exception) {
                                MessageSnackBar(view as View, "Intente Nuevamente", Color.GREEN)

//                                        baseActivity.runOnUiThread {
//                                            Log.i("update",Ex.toString())
//                                            Toast.makeText(activity,"Intente Nuevamente",Toast.LENGTH_SHORT).show()
//                                        }
                            }
                        }
                    }
                }
                true
            }

            R.id.MBEliminateItem -> {

                productoLiveData.removeObservers(baseActivity)
                CoroutineScope(Dispatchers.IO).launch {
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
//                    MainModel.productosData().delete(producto)
                    MainModel.deteProduct(producto)
//                    println("SE ELIMINO")
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
                }

                Toast.makeText(activity, "Se elimino el producto!", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    private var formatter = SimpleDateFormat("dd|MM|yyyy - h:mm")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.TTNombre.isSelected = true
//        database= AppDatabase.getDataBase(baseActivity);
//        productoLiveData=database.productosData().get(uid)
//        productoLiveData=database.productosData().getInventarioId(uid)
        productoLiveData = MainModel.getProductId(uid)
//        val daoNew = AppDatabase.getDataBase(baseActivity);
//        daoNew.productosData().getTime(uid).observe(viewLifecycleOwner,{
//        })
        binding.TTNombre.transitionName = "headertitle"
        productoLiveData.observe(viewLifecycleOwner, Observer {
            producto = it.producto
            activity?.setTitle("${formatter.format(producto.update)}")
            binding.TTNombre.setText(producto.nombre)
            binding.TTPrecio.setText(producto.precioC.toString())
            binding.TTPrecioUn.setText(producto.precioNeto.toString())
//            binding.TTMarca.setText(producto.marca.toString())
            binding.TTCaracteristicas.setText(producto.detalles.toString())
//            binding.TTCategoria.setText(producto.categoria)
            binding.TTStock.setText(producto.stockC.toString())
            binding.TTStockU.setText(producto.stockU.toString())
            binding.TTQR.setText(producto.qr.toString())

            baseActivity.runOnUiThread {
                if (it.imageN.isNotEmpty()) {
                    adapter = DetallesAdapterImagen(baseActivity, it.imageN)
                    binding.RVimages.adapter = adapter
                } else {
                    binding.RVimages.visibility = View.GONE
                }
            }
            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })

        binding.MBEliminateItem.setOnClickListener {
            productoLiveData.removeObservers(baseActivity)
            CoroutineScope(Dispatchers.IO).launch {
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
//                    MainModel.productosData().delete(producto)
                MainModel.deteProduct(producto)
//                    println("SE ELIMINO")
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
            }

            Toast.makeText(activity, "Se elimino el producto!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
        binding.MBEditAndSave.setOnClickListener {
            editable = !editable
            editText(editable)
            val id: Int = if (editable) R.drawable.ic_baseline_done_all_24 else R.drawable.ic_baseline_edit_24
            binding.MBEditAndSave.background=(ContextCompat.getDrawable(baseActivity,id))
            if (!editable) {

                runBlocking {
                    val _name: String = binding.TTNombre.text.toString()
                    val _precio: String = binding.TTPrecio.text.toString()
                    val _precioU: String = binding.TTPrecioUn.text.toString()
                    val _marca: String = binding.TTMarca.text.toString()
                    val _detalles: String = binding.TTCaracteristicas.text.toString()
                    val _categoria: String = binding.TTCategoria.text.toString()
                    val _stock: String = binding.TTStock.text.toString()
                    val _stockU: String = binding.TTStockU.text.toString()
                    val _qr: String = binding.TTQR.text.toString()
                    val _image: ByteArray = ByteArray(0)

                    val name = _name
                    val precio: Double? = _precio.toDoubleOrNull()
                    val precioU: Double = if (_precioU.isEmpty()) 0.0 else _precioU.toDouble()
                    val marca: String = _marca
                    val detalles: String = _detalles
                    val categoria: String = _categoria
                    val stock: Int = if (_stock.isEmpty()) 1 else _stock.toInt()
                    val stockU: Int = if (_stockU.isEmpty()) 0 else _stockU.toInt()
                    val qr: String = _qr
//                                val image:List<ImagenesNew> = listOf<ImagenesNew>(ImagenesNew(_image, Date()))
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            if (!(name.isEmpty() || precio == null)) {
                                val NewProducto: Producto = Producto(
                                    Date(), name,
                                    precio,
                                    precioU,
                                    null,
                                    null,
                                    null,
                                    stock,
                                    stockU,
                                    null,
                                    null,
                                    null,
                                    null,
                                    qr,
                                    null,
                                    null,
                                    null,
                                    ""
                                )
//                                            MainModel.productosData().update(NewProducto);
                                MainModel.updateProduct(NewProducto)
//                                            baseActivity.runOnUiThread {
                                MessageSnackBar(
                                    view as View,
                                    "Se actualizo Correctamente",
                                    Color.GREEN
                                )
//
//                                                Toast.makeText(
//                                                    activity,
//                                                    "Se actualizo correctamente!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
                            } else {
                                MessageSnackBar(
                                    view as View,
                                    "Tiene que rellenar precio o nombre",
                                    Color.GREEN
                                )
//                                            baseActivity.runOnUiThread {
//                                                Toast.makeText(activity,"Tiene que rellenar Precio o nombre",Toast.LENGTH_SHORT).show()
//                                            }
                            }
                        } catch (Ex: Exception) {
                            MessageSnackBar(view as View, "Intente Nuevamente", Color.GREEN)

//                                        baseActivity.runOnUiThread {
//                                            Log.i("update",Ex.toString())
//                                            Toast.makeText(activity,"Intente Nuevamente",Toast.LENGTH_SHORT).show()
//                                        }
                        }
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetallesProductsBinding.inflate(inflater, container, false)

        val navcontroller = findNavController()
        val appbarlayout = AppBarConfiguration(navcontroller.graph)
        binding.toolbar.setupWithNavController(navcontroller, appbarlayout)

        //        println(uid);
//        return inflater.inflate(R.layout.fragment_detalles_producto, container, false)
        return binding.root
    }

    override fun onStart() {
//        baseActivity.binding.appBarMain.refreshFab.visibility=View.GONE
        super.onStart()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        ((activity as MainActivity).showfabrefresh())
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetallesProducto.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetallesProducto().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}