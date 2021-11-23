package com.example.finalproductos

import Adapters.DetallesAdapterImagen
import Data.AppDatabase
import Data.ImagenesNew
import Data.InventarioProducts
import Data.Producto
import android.content.Context
import android.media.Image
import android.net.IpSecManager
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproductos.databinding.FragmentDetallesProductoBinding
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [DetallesProducto.newInstance] factory method to
 * create an instance of this fragment.
 */

//class Persona(
//    val nombre:String,
//    val apellido:String,
//    val edad:Int,
//){
//    init {
//        println("El nombre de la persona es ${this.nombre} y su apellido es ${this.apellido}")
//    }
//    fun GenerateList():MutableList<String>{
//        return mutableListOf(nombre.toString(),apellido.toString(),edad.toString())
//    }
//}
class DetallesProducto : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database:AppDatabase;
    private lateinit var producto:Producto;
    private lateinit var imagenes:List<ImagenesNew>
    private lateinit var baseActivity: MainActivity
    private lateinit var contextFragment: Context
    private  lateinit var productoLiveData:LiveData<InventarioProducts>;
    private var _binding: FragmentDetallesProductoBinding? = null
    private val binding get() = _binding!!
    private var editable:Boolean=false;
    private lateinit var adapter:Adapters.DetallesAdapterImagen
    var uid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        val animation=TransitionInflater.from(context).inflateTransition(
            android.R.transition.move
        )
        sharedElementReturnTransition=animation
        sharedElementEnterTransition=animation

        arguments?.let {
            uid=it.getInt("uid");
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_detalles, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    protected fun setEditableIcon(menuItem:MenuItem){
        val id:Int=if(editable) R.drawable.ic_baseline_done_all_24 else R.drawable.ic_baseline_edit_24
        menuItem.icon=ContextCompat.getDrawable(baseActivity,id);
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }
    private fun editText(bool:Boolean){
        binding.TTNombre.isEnabled=bool
        binding.TTPrecio.isEnabled=bool
        binding.TTPrecioUn.isEnabled=bool
        binding.TTMarca.isEnabled=bool
        binding.TTCaracteristicas.isEnabled=bool
        binding.TTCategoria.isEnabled=bool
        binding.TTStock.isEnabled=bool
        binding.TTStockU.isEnabled=bool
        binding.TTQR.isEnabled=bool
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.MBSettings->{
                println("SE SELECIONO SETTINGS")
                true
            }
            R.id.MBEditAndSave->{
                editable=!editable
                editText(editable)

                setEditableIcon(item)
                if(!editable){

                            runBlocking {
                                val _name:String=binding.TTNombre.text.toString()
                                val _precio:String=binding.TTPrecio.text.toString()
                                val _precioU:String= binding.TTPrecioUn.text.toString()
                                val _marca:String = binding.TTMarca.text.toString()
                                val _detalles:String = binding.TTCaracteristicas.text.toString()
                                val _categoria:String = binding.TTCategoria.text.toString()
                                val _stock:String= binding.TTStock.text.toString()
                                val _stockU:String=binding.TTStockU.text.toString()
                                val _qr:String=binding.TTQR.text.toString()
                                val _image:ByteArray=  ByteArray(0)

                                val name=_name
                                val precio:Double?=_precio.toDoubleOrNull()
                                val precioU:Double= if (_precioU.isEmpty()) 0.0 else _precioU.toDouble()
                                val marca:String = _marca
                                val detalles:String = _detalles
                                val categoria:String = _categoria
                                val stock:Int = if(_stock.isEmpty()) 1 else _stock.toInt()
                                val stockU:Int = if(_stockU.isEmpty()) 0 else _stockU.toInt()
                                val qr:String=_qr
//                                val image:List<ImagenesNew> = listOf<ImagenesNew>(ImagenesNew(_image, Date()))
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        if(!(name.isEmpty() || precio==null)) {
                                            val NewProducto: Producto = Producto(
                                                Date(), name,
                                                precio,
                                                precioU,
                                                marca,
                                                detalles,
                                                categoria,
                                                stock,
                                                stockU,
                                                qr,
                                                uid
                                            )
                                            database.productosData().update(NewProducto);
                                            baseActivity.runOnUiThread {

                                                Toast.makeText(
                                                    activity,
                                                    "Se actualizo correctamente!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }else{
                                            baseActivity.runOnUiThread {
                                                Toast.makeText(activity,"Tiene que rellenar Precio o nombre",Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }catch (Ex:Exception){
                                        baseActivity.runOnUiThread {
                                            Log.i("update",Ex.toString())
                                            Toast.makeText(activity,"Intente Nuevamente",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                }
                true
            }
            R.id.MBEliminateItem->{

                productoLiveData.removeObservers(baseActivity)
                CoroutineScope(Dispatchers.IO).launch {
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
                    database.productosData().delete(producto)
//                    println("SE ELIMINO")
//                    println(database.productosData().getByImagenesId(producto.uid).toString())
                }

                Toast.makeText(activity,"Se elimino el producto!",Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
                true
            }

            else->super.onOptionsItemSelected(item)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    private  var formatter = SimpleDateFormat("dd|MM|yyyy - h:mm")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        postponeEnterTransition()


        binding.TTNombre.isSelected=true
        database= AppDatabase.getDataBase(baseActivity);
//        productoLiveData=database.productosData().get(uid)
        productoLiveData=database.productosData().getInventarioId(uid)

//        val daoNew = AppDatabase.getDataBase(baseActivity);
//        daoNew.productosData().getTime(uid).observe(viewLifecycleOwner,{
//        })

        productoLiveData.observe(viewLifecycleOwner, Observer {
            producto=it.producto
            activity?.setTitle("${formatter.format(producto.update)}")
            binding.TTNombre.setText(producto.nombre)
            binding.TTPrecio.setText(producto.precioC.toString())
            binding.TTPrecioUn.setText(producto.precioU.toString())
            binding.TTMarca.setText(producto.marca.toString())
            binding.TTCaracteristicas.setText(producto.detalles.toString())
            binding.TTCategoria.setText(producto.categoria)
            binding.TTStock.setText(producto.stockC.toString())
            binding.TTStockU.setText(producto.stockU.toString())
            binding.TTQR.setText(producto.qr.toString())
            binding.RVimages.layoutManager=
                LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL,false)
            baseActivity.runOnUiThread {
                if(!it.imageN.isEmpty()) {
                    adapter = DetallesAdapterImagen(baseActivity, it.imageN)
                    binding.RVimages.adapter = adapter
                }else{
                    binding.RVimages.visibility=View.GONE
                }
            }
            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetallesProductoBinding.inflate(inflater, container, false)

        ((activity as MainActivity).hidefabrefresh())
        //        println(uid);
//        return inflater.inflate(R.layout.fragment_detalles_producto, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ((activity as MainActivity).showfabrefresh())
    }

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