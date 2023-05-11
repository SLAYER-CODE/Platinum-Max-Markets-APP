package com.example.fromdeskhelper.ui.View.fragment

import Data.ClientList
import Data.ClientListGet
import Data.Producto
import Data.listInventarioProductos
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentShowProductsBinding
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.ClientLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsStoreViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.ShowLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.UitlsMainShowViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.UtilsShowMainViewModels
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments.affectOnItemClicks
import com.example.fromdeskhelper.util.listener.RecyclerViewItemClickListener
import com.example.fromdeskhelper.util.listener.recyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_producto.view.*
import kotlinx.coroutines.launch

//@JvmOverloads
//fun RecyclerView.affectOnItemLong(onClick: ((position: Int, view: View) -> Unit)? = null, onLongClick: ((position: Int, view: View) -> Unit)? = null) {
//    this.addOnChildAttachStateChangeListener(recyclerItemClickListener(this, onClick, onLongClick))
//}

@AndroidEntryPoint
class ShowProducts : Fragment() {

    private var _binding: FragmentShowProductsBinding? = null
    private val binding get() = _binding!!
    private var listaProductos = emptyList<listInventarioProductos>()
    private var listaClientes = emptyList<ClientList>()
    private lateinit var baseActivity: MainActivity
    private lateinit var contextFragment: Context
    var currentPage: Int = 6
    var itemfinal: Boolean = false;
    var clientcount: Int = 1;
    var adapter:ProductoAdapter= ProductoAdapter(mutableListOf(),null,0,null)

    //    lateinit var daoNew:AppDatabase
//    private val MainModel: ShowMainViewModel by viewModels(ownerProducer = { requireActivity() });

    private val StoreSendViewModel: SendProductsStoreViewModel by viewModels(ownerProducer = { requireActivity() })

    private val UtilsViewProduct: UitlsMainShowViewModel by viewModels(ownerProducer = {
        requireActivity()
    });

    private val UtilsView: UtilsShowMainViewModels by viewModels(ownerProducer = {
        requireActivity()
    });

    private val ClienLocalModel: ClientLocalViewModel by viewModels(ownerProducer = {
        requireActivity()
    })

    private val LocalModel: ShowLocalViewModel by viewModels(ownerProducer = {
        requireActivity()
    })




    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        daoNew = AppDatabase.getDataBase(baseActivity)
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState)
    }

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

    fun comprobateList(Size: Int) {
        if (Size == 0) {
            binding.TNotItems.visibility = View.VISIBLE
        } else {
            binding.TNotItems.visibility = View.INVISIBLE
        }
    }

    fun setClipView(view: View?, clip: Boolean): View? {
        if (view != null) {
            val parent = view.parent
            if (parent is ViewGroup) {
                val viewGroup = view.parent as ViewGroup
                viewGroup.clipChildren = clip
                setClipView(viewGroup, clip)
            }
        }
        return view
    }

    val FREQ = 3f
    val DECAY = 2f
    val decayingSineWave = TimeInterpolator { input ->
        val raw = Math.sin(FREQ * input * 2 * Math.PI)
        (raw * Math.exp((-input * DECAY).toDouble())).toFloat()
    }


    fun setFunction(value: Boolean, client: ClientListGet?) {
        Log.i("SE APRETO LONG","FINALITEM")
        adapter.setOnItemListenerListener(object:ProductoAdapter.OnItemListener{
            override fun OnItemClickListener(view: View?, position: Int) {
                if (findNavController().currentDestination?.id == R.id.FirstFragment) {
                    val bundle = Bundle()
                    val navBuilder = NavOptions.Builder()
                    navBuilder.setEnterAnim(android.R.anim.fade_in)
                        .setExitAnim(android.R.anim.fade_out)
                        .setPopExitAnim(android.R.anim.fade_out)
                    Log.i("POSICION",position.toString())
                    bundle.putInt("uid", listaProductos[position].uid)
                    var animation = 0
                    var extras: FragmentNavigator.Extras = FragmentNavigatorExtras()

                    if (view?.IVimagenItem != null) {
                        animation = 1
                        extras = FragmentNavigatorExtras(view.IVimagenItem to "image_big")
                    }

                    bundle.putInt("animation", animation)
                    findNavController().navigate(
                        resId = R.id.action_FirstFragment_to_detallesProducto,
                        args = bundle,
                        navOptions = navBuilder.build(),
                        extras
                    )
                }
            }

            override fun OnItemLongClickListener(view: View?, position: Int) {
                if (view!=null&&value && client != null) {


                    ClienLocalModel.setRelationSelect(
                        client.uid,
                        listaProductos[position].uid
                    )
                    view.animate()
                        .yBy(-50f).xBy(-25f)
                        .setInterpolator(decayingSineWave)
                        .setDuration(200).withEndAction {
                            val ViewDrawable =
                                DrawableCompat.wrap(view.ItemBackground.background);
                            DrawableCompat.setTint(ViewDrawable.mutate(), client.color)
                        }

                        .start();

                }            }

        })

//        binding.LVMylist.affectOnItemClicks(onClick = { position, view: View ->
//
//            if (findNavController().currentDestination?.id == R.id.FirstFragment) {
//                val bundle = Bundle()
//                val navBuilder = NavOptions.Builder()
//                navBuilder.setEnterAnim(android.R.anim.fade_in)
//                    .setExitAnim(android.R.anim.fade_out)
//                    .setPopExitAnim(android.R.anim.fade_out)
//
//                bundle.putInt("uid", listaProductos[position].uid)
//                var animation = 0
//                var extras: FragmentNavigator.Extras = FragmentNavigatorExtras()
//
//                if (view?.IVimagenItem != null) {
//                    animation = 1
//                    extras = FragmentNavigatorExtras(view.IVimagenItem to "image_big")
//                }
//
//                bundle.putInt("animation", animation)
//                findNavController().navigate(
//                    resId = R.id.action_FirstFragment_to_detallesProducto,
//                    args = bundle,
//                    navOptions = navBuilder.build(),
//                    extras
//                )
//
//
//            }
//        }, onLongClick = { position, view ->
//
//
//        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


//        loadData(false)
//        binding.LVMylist.adapter=adaptador

//        binding.LVMylist.layoutManager=GridLayoutManager(baseActivity,2)


//        binding.LVMylist.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                var Manayer=binding.LVMylist.layoutManager
//
//            }
//        })



//        Configuraciones de la barra personalizada



        ClienLocalModel.itemClientItem.observe(viewLifecycleOwner, Observer {
            setFunction(true, it)
        })

        StoreSendViewModel.Items.observe(viewLifecycleOwner, Observer {
            listaProductos = it.MyImage
            adapter = it
            setFunction(false,null)
            comprobateList(it.itemCount)


            lifecycleScope.launch {
                binding.LVMylist.adapter=adapter
                adapter.notifyDataSetChanged()
                binding.LVMylist.startLayoutAnimation()
            }
//            Log.i("adapterproductcall",it.toString())

        })

        binding.swipe.setOnRefreshListener {
            binding.LVMylist.startLayoutAnimation()
            binding.swipe.isRefreshing=false
        }
//        UtilsViewProduct.RequestLayoutItem.observe(viewLifecycleOwner, Observer {
//            binding.LVMylist.adapter = listaProductos
//            binding.LVMylist.apply {
//                layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.item_animation_layout_into)
//                layoutAnimationListener = object : Animation.AnimationListener {
//                    override fun onAnimationStart(animation: Animation?) {
//                        layoutManager?.findViewByPosition(1)?.clearAnimation()
//
//                    }
//                    override fun onAnimationEnd(animation: Animation?) { }
//                    override fun onAnimationRepeat(animation: Animation?) { }
//                }
//                adapter?.notifyDataSetChanged()
//                scheduleLayoutAnimation()
//                adapter?.notifyDataSetChanged()
//                startLayoutAnimation()

//            recyclerView.scheduleLayoutAnimation();

//        })

//        UtilsViewProduct.ItemFilterView.observe(viewLifecycleOwner, Observer {
//            adaptador.filter.filter(it)
//        })

        UtilsViewProduct.DesingItemListVIew.observe(viewLifecycleOwner, Observer {
//            ListViewDesingLayout
            if (it == 1) {
                binding.LVMylist.layoutManager =
                    LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
            } else if (it == 2) {
                binding.LVMylist.layoutManager = GridLayoutManager(baseActivity, 2)
            } else if (it == 0) {
                binding.LVMylist.layoutManager =
                    LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
            } else if (it == 3) {
                binding.LVMylist.layoutManager = GridLayoutManager(baseActivity, 2)
            }
            binding.LVMylist.adapter?.notifyDataSetChanged()
        })
        UtilsView.StorageCountObserver.observe(viewLifecycleOwner, Observer {
            comprobateList(it?.toInt() ?: 0)
        })

//        ************************************************************************************************

//        binding.LVMylist.startLayoutAnimation()


//        Con esta funcion se llama cuando el scroll es movido y carga los datos de la funcion loadData con los parametros que tenemos mas antes.


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


//        binding.ButtonAddClient.setOnClickListener(){
//            val extras = FragmentNavigatorExtras(binding.imageunique to "image_big")
//            findNavController().navigate(
//                R.id.action_FirstFragment_to_SecondFragment,
//                null,
//                null,
//                extras
//            )
//        }


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
//        postponeEnterTransition()
    }

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.DetailsChecked->{
//                UtilsViewProduct.NormalDesingLayout()
//                Log.i("LISTIVEWWITEM","PRIMERO")
//            }
//            R.id.ListChecked->{
//                UtilsViewProduct.ListViewDesingLayout()
//            }
//            R.id.GridDetalles->{
//                UtilsViewProduct.GirdLayout()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
    override fun onDestroyView() {
        super.onDestroyView()
//        println("Se destruyo la vista")
        _binding = null
    }
}
