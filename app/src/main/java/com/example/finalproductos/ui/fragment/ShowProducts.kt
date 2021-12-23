package com.example.finalproductos.ui.fragment

import com.example.finalproductos.ui.adapter.ClientAdapter
import com.example.finalproductos.ui.adapter.ProductoAdapter
import Data.*
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproductos.databinding.FragmentShowProductsBinding
import com.example.finalproductos.ui.MainActivity
import android.graphics.Color
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.finalproductos.R
import com.example.finalproductos.Util.listener.InfiniteScrollListener
import com.example.finalproductos.Util.listener.RecyclerItemClickListener
import com.example.finalproductos.io.db.AppDatabase
import com.example.finalproductos.model.objects.Constants
import kotlinx.android.synthetic.main.item_producto.view.*
import java.util.*


class ShowProducts : Fragment() {

    private var _binding: FragmentShowProductsBinding? = null
    private val binding get() = _binding!!
    private var listaProductos = emptyList<listInventarioProductos>()
    private var listaClientes = emptyList<ClientList>()
    private lateinit var baseActivity: MainActivity
    private lateinit var contextFragment: Context
    var adapter: ProductoAdapter = ProductoAdapter(listOf());
    var currentPage:Int=6
    var itemfinal:Boolean=false;
    var clientcount:Int=1;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowProductsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    fun createAleatorieList():ClientList{
        val NewClient =ClientList(Date(),Color.argb(200, (50..255).random() , (25..255).random() , (25..255).random()),clientcount)
        clientcount+=1
        return NewClient;
    }
    fun refreshList(){
        binding.LVMylist.startLayoutAnimation()
    }

    override fun onStart() {
        super.onStart()
        (baseActivity as MainActivity).functionFabRefresh(::refreshList)
        (activity as MainActivity).returnbinding().refreshFab.setImageResource(R.drawable.ic_baseline_autorenew_24)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        val  daoNew = AppDatabase.getDataBase(baseActivity);

//        Con esta funcion se llama cuando el scroll es movido y carga los datos de la funcion loadData con los parametros que tenemos mas antes.

        binding.LVMylist.apply {
            daoNew.productosData().getByInventarioProductos(currentPage, 0)
                .observe(viewLifecycleOwner, Observer {
                    listaProductos = it
                    adapter = ProductoAdapter(listaProductos);
                    binding.LVMylist.adapter = adapter
                    view.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                })

        }


        fun loadData(final:Boolean) {

//            Animaciones para la carga de datos usando la barra de carga que se encuentra arriba del recicleview
            itemfinal=final
            println(currentPage.toString()+"Se ejecuta una vez"+final)
            if(!itemfinal) {
//                val progressAnimatorCero =
//                    ObjectAnimator.ofInt(baseActivity.returnbinding().PBbarList, "progress", 100, 0).setDuration(800)
//                val progressAnimator =
//                    ObjectAnimator.ofInt(baseActivity.returnbinding().PBbarList, "progress", 0, 100).setDuration(800)
//                progressAnimator.start()
                baseActivity.returnbinding().PBbarList.visibility=View.VISIBLE


//            println(listaProductos)
                daoNew.productosData().getByInventarioProductos(
                    Constants.ListExtract.LIMIT,
                    currentPage
                ).observe(
                    viewLifecycleOwner, Observer {
                        listaProductos += it
                        adapter.addNewListCurrent(it)
                    }
                )
                currentPage += Constants.ListExtract.OFFSET

//            println("Suma" + currentPage)
//                Executors.newSingleThreadExecutor().execute {
//
//            }

//            daoNew.productosData().getAllInventarioTime(Constants.LIMIT,currentPage*Constants.OFFSET).observe(
//                baseActivity, Observer {
//                    listaProductos+=it
//                    adapter.addNewListCurrent(it)
//                })

                baseActivity.returnbinding().PBbarList.visibility=View.GONE
//                progressAnimatorCero.start()
            }
        }

        daoNew.productosData().getCount().observe(viewLifecycleOwner,onChanged = {
            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.LVMylist.layoutManager=gridLayoutManager
            binding.LVMylist.addOnScrollListener(InfiniteScrollListener(::loadData,gridLayoutManager,it,itemfinal))
        })

//        Executors.newSingleThreadExecutor().execute {
//            listaProductos =  daoNew.productosData().getByInventarioProductos(Constants.LIMIT, 0)
//            Runnable {
//                baseActivity.runOnUiThread {
//                    Runnable {
//                        adapter = ProductoAdapter(listaProductos);
//                        binding.LVMylist.adapter = adapter
//                    }
//                }
//            }
//        }
//

//            println(binding.LVMylist.findViewHolderForLayoutPosition(position))
//            println(binding.LVMylist.findViewHolderForAdapterPosition(position))
//            println(binding.LVMylist.computeVerticalScrollRange())
//            println(binding.LVMylist.computeVerticalScrollOffset())
//            println(binding.LVMylist.computeVerticalScrollExtent())
//            println("ScrollMain")
//            println(binding.LVMylist.computeScroll())
//            println(position)
        binding.LVMylist.affectOnItemClicks(onLongClick = { position, view: View ->
            val bundle = Bundle()
            bundle.putInt("uid", listaProductos[position].uid)
//            findNavController().navigate(R.id.action_FirstFragment_to_detallesProducto,bundle)
            val extras = FragmentNavigatorExtras(view.IVimagenItem to "transtionexit")
            findNavController().navigate(
                R.id.action_FirstFragment_to_detallesProducto,
                bundle,
                null,
                extras
            )
        })

//        binding.ButtonAddClient.setOnClickListener(){
//            val extras = FragmentNavigatorExtras(binding.imageunique to "image_big")
//            findNavController().navigate(
//                R.id.action_FirstFragment_to_SecondFragment,
//                null,
//                null,
//                extras
//            )
//        }

        val adapterClient= ClientAdapter(mutableListOf<ClientList>())
        binding.addclientitems.layoutManager=LinearLayoutManager(baseActivity,LinearLayoutManager.HORIZONTAL,false)
        binding.addclientitems.adapter=adapterClient

        adapterClient.addClient(createAleatorieList())
        adapterClient.addClient(createAleatorieList())

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

//        adapter.setItemAction {
//            println(it.producto.uid)
//            val bundle = Bundle()
////            bundle.putInt("uid", listaProductos[position].producto.uid)
//            bundle.putInt("uid",it.producto.uid)
//            findNavController().navigate(R.id.action_FirstFragment_to_detallesProducto,bundle)
//        }



//        daoNew.productosData().getAllTime().observe(viewLifecycleOwner, Observer {
//            listaProductos=it
//            val adapter = ProductoAdapter(baseActivity,listaProductos);
//            binding.LVMylist.adapter=adapter
//        })


//        binding.LVMylist.setOnItemClickListener { parent, view, position, id ->
//            val bundle = Bundle()
//            bundle.putInt("uid", listaProductos[position].producto.uid)
//
//            findNavController().navigate(R.id.action_FirstFragment_to_detallesProducto,bundle)
//        }

//        println("SE CREO NUEVAMENTE EL FRAGMENTO")

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        println("Se destruyo la vista")
        _binding = null
    }
}

@JvmOverloads
fun RecyclerView.affectOnItemClicks(onClick: ((position: Int, view: View) -> Unit)? = null, onLongClick: ((position: Int, view: View) -> Unit)? = null) {
    this.addOnChildAttachStateChangeListener(RecyclerItemClickListener(this, onClick, onLongClick))
}