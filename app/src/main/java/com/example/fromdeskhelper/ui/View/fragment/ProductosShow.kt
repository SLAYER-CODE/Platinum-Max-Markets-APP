package com.example.fromdeskhelper.ui.View.fragment

import Data.ClientList
import Data.ClientListGet
import Data.listInventarioProductos
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.collection.arraySetOf
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.db.AppDatabase
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.databinding.FragmentProductosShowBinding
import com.example.fromdeskhelper.ui.View.ViewModel.*
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.*
import com.example.fromdeskhelper.ui.View.fragment.Root.Clients.ClientItemShowFragment
import com.example.fromdeskhelper.util.TabletPageTransformer
import com.example.fromdeskhelper.util.listener.RecyclerViewItemClickListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.fragment_productos_show.*
import kotlinx.android.synthetic.main.fragment_show_products.view.*
import kotlinx.android.synthetic.main.item_client_list.view.*
import kotlinx.android.synthetic.main.item_producto.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProductos.newInstance] factory method to
 * create an instance of this fragment.
 */

open class animadroNotificaciones : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    }

    override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
        super.onAnimationFinished(viewHolder!!)
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return super.animateMove(holder, fromX, fromY, toX, toY)
    }
}

@AndroidEntryPoint
class ProductosShow : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProductosShowBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    lateinit var daoNew: AppDatabase

    var currentPage:Int=6
    var itemfinal:Boolean=false;
    var clientcount:Int=1;
    private var listaProductos = emptyList<listInventarioProductos>()
//    var adaptador: ProductoAdapter = ProductoAdapter(listOf());
    private val UtilsMainModel:UtilsShowMainViewModels by viewModels(ownerProducer = {requireActivity()})
    private var contadorImagen = 1;

    //Item Productos
    private val MainModel : ShowMainViewModel by viewModels(ownerProducer = {requireActivity()});

    private val ClienModel:ClientAddShortViewModel by viewModels()

    private val ClienLocalModel: ClientLocalViewModel by viewModels(ownerProducer = {
        requireActivity()
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
//        daoNew = AppDatabase.getDataBase(baseActivity)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    override fun onStart() {
        super.onStart()
        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.GONE
        }
        if(baseActivity.binding.appBarMain.toolbarParent.visibility==View.GONE){
            baseActivity.binding.appBarMain.toolbarParent.visibility=View.VISIBLE
        }
        if(baseActivity.binding.appBarMain.fab.visibility==View.GONE){
            baseActivity.binding.appBarMain.fab.visibility=View.VISIBLE
        }
        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.GONE){
            baseActivity.binding.appBarMain.refreshFab.visibility=View.VISIBLE
        }
//        (baseActivity as MainActivity).functionFabRefresh(::refreshList)
//        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
//            refreshList()
//        }
        baseActivity.binding.appBarMain.BIShowP.visibility=View.VISIBLE
        baseActivity.binding.appBarMain.refreshFab.setImageResource(R.drawable.ic_baseline_autorenew_24)

    }



//    fun refreshList(){
//        binding.paggerid.LVMylist.startLayoutAnimation()
//    }


    override fun onResume() {
//        daoNew.productosData().getCount().observe(viewLifecycleOwner,onChanged = {
//            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
//            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
//            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
//        })
//
//
        baseActivity.setRefreshMain()
        MainModel.GetCount().observe(viewLifecycleOwner, onChanged = {
            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        })

        super.onResume()
    }
    fun createAleatorieList(): ClientList {
        val NewClient = ClientList(
            fecha = Date(),
            color =Color.argb(200, (50..255).random(), (25..255).random(), (25..255).random()),
            number = clientcount
        )
        clientcount += 1
        return NewClient;
    }
    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            startScale, endScale,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 200
        v.startAnimation(anim)
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }
    val FREQ = 3f
    val DECAY = 2f
    val decayingSineWave = TimeInterpolator { input ->
        val raw = Math.sin(FREQ * input * 2 * Math.PI)
        (raw * Math.exp((-input * DECAY).toDouble())).toFloat()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductosShowBinding.inflate(inflater, container, false)

        var adapterClient = ClientAdapter(mutableListOf<ClientListGet>())

        binding.addclientitems.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.BAddclient.setOnClickListener {
            var createClient=createAleatorieList()
            ClienModel.agregateItem(createClient)
            binding.BAddclient.animate().rotation(180F * (contadorImagen++)).setDuration(200).start()
        }

        binding.BAddclient.setOnLongClickListener {

            true
        }

        adapterClient= ClientAdapter(mutableListOf())
        binding.addclientitems.adapter=adapterClient
         ClienModel.getItemClients().observe(viewLifecycleOwner, Observer {
             adapterClient.Clients=it.reversed().toMutableList()
             adapterClient.notifyItemInserted(0)
             binding.addclientitems.scrollToPosition(0);
         })

        binding.paggerid.setPageTransformer(TabletPageTransformer())


        ClienLocalModel.closeView.observe(viewLifecycleOwner, Observer {
            binding.ClientFragmnetShow.visibility = View.GONE
        })
        binding.addclientitems.addOnItemTouchListener(RecyclerViewItemClickListener(
            context,binding.addclientitems,object : ShowProducts.ClickListener {
                override fun onClick(view: View?, position: Int) {
                    val ViewDrawable=DrawableCompat.wrap(binding.TLMain.background);
                    var client= adapterClient.Clients[position]
                    DrawableCompat.setTint(ViewDrawable,client.color)
                    Log.i("SETOCO",position.toString())

                    for (x in 0.. adapterClient.Clients.size){

                        if(x == position){
                            scaleView(view!!,1f,1.2f)
                        }else{
                            binding.addclientitems.adapter?.notifyItemChanged(x)
                        }
                    }
                    ClienLocalModel.SendselectItem(client)
                    Log.i("SETOCOFINAL",adapterClient.toString())
                }
                override fun onLongClick(view: View?, position: Int) {
                    if (view != null) {
                        view?.animate()
                            .yBy(-60f).xBy(-20f)
                            .setInterpolator(decayingSineWave)
                            .setDuration(200)
                            .start();
                        val ft: FragmentTransaction =
                            baseActivity.supportFragmentManager.beginTransaction()
                        ft.setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out,
                        )
                        var bundle=Bundle()
                        bundle.putInt("uid", adapterClient.Clients[position].uid)
                        ft.replace(R.id.ClientFragmnetShow, ClientItemShowFragment()::class.java,bundle,"");
                        ft.setReorderingAllowed(true)
                        ft.commit()
                        binding.ClientFragmnetShow.visibility = View.VISIBLE
                    }
                }
            }
        ))



//        Finall


//        daoNew.productosData().getByInventarioProductos(currentPage, 0)
//            .observe(viewLifecycleOwner, Observer {
//                listaProductos = it
//                adaptador= ProductoAdapter(listaProductos)
//                binding.paggerid.LVMylist.adapter = adaptador
//                view?.doOnPreDraw {
//                    startPostponedEnterTransition()
//                }
//            })

//        fun loadData(final:Boolean) {
//
////            Animaciones para la carga de datos usando la barra de carga que se encuentra arriba del recicleview
//            itemfinal=final
//            println(currentPage.toString()+"Se ejecuta una vez"+final)
//            if(!itemfinal) {
////                val progressAnimatorCero =
////                    ObjectAnimator.ofInt(baseActivity.returnbinding().PBbarList, "progress", 100, 0).setDuration(800)
////                val progressAnimator =
////                    ObjectAnimator.ofInt(baseActivity.returnbinding().PBbarList, "progress", 0, 100).setDuration(800)
////                progressAnimator.start()
////            baseActivity.returnbinding().PBbarList.visibility=View.VISIBLE
////            println(listaProductos)
//
//                daoNew.productosData().getByInventarioProductos(
//                    Constants.ListExtract.LIMIT,
//                    currentPage
//                ).observe(
//                    viewLifecycleOwner, Observer {
//                        listaProductos += it
//                        adaptador.addNewListCurrent(it)
//                    }
//                )
//                currentPage += Constants.ListExtract.OFFSET
////            daoNew.productosData().getAllInventarioTime(Constants.LIMIT,currentPage*Constants.OFFSET).observe(
////                baseActivity, Observer {
////                    listaProductos+=it
////                    adapter.addNewListCurrent(it)
////                })
//
////            baseActivity.returnbinding().PBbarList.visibility=View.GONE
////                startPostponedEnterTransition()
////                progressAnimatorCero.start()
//            }
//        }
//
        //ProductList



        val adapter by lazy { ViewPagerMainAdapter(baseActivity) }
//        var primero = ViewPagerMainAdapter()
//        var fragmento= ViewPagerAdapter(activity?.supportFragmentManager)
//        fragmento.addFragment(ShowProducts(),"ShowProducts")
//        fragmento.addFragment(ShowProducts(),"ShowProducts")
//        fragmento.addFragment(ShowProducts(),"ShowProducts")

//
//        var fragmento = ViewMainAdapter(requireActivity().supportFragmentManager, lifecycle)
//        fragmento.addFragment(ShowProducts())
//        fragmento.addFragment(ShowProductsServer())
//        fragmento.addFragment(ShowProductsDatabase())
        binding.paggerid.adapter=adapter;
//        binding.paggerid.adapter=fragmento

        val tablayoutnavigator=TabLayoutMediator(binding.TLMain,binding.paggerid as ViewPager2,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text="Storage"
                        tab.setIcon(R.drawable.ic_baseline_sd_storage_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=0
                        Badged.isVisible=false
                    }
                    1->{
                        tab.text="Server"
                        tab.setIcon(R.drawable.ic_baseline_cloud_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=0
                        Badged.isVisible=false
                    }
                    2->{
                        tab.text="Local"
                        tab.setIcon(R.drawable.ic_baseline_store_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=0
                        Badged.isVisible=false
                    }
                }
        })

        tablayoutnavigator.attach()
        UtilsMainModel.GetCountItemsServer()
        UtilsMainModel.LocalCountObserver.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                var item = binding.TLMain.getTabAt(2)?.badge
                item?.number = it
                item?.isVisible=true
            }
        })
//        UtilsMainModel.GetStorageCount().observe(viewLifecycleOwner, onChanged =  {
//            if(it!=null){
//                var item = binding.TLMain.getTabAt(0)?.badge
//                item?.number = it
//                item?.isVisible=true
//            }
//        })

        UtilsMainModel.StorageCountObserver.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                var item = binding.TLMain.getTabAt(0)?.badge
                item?.number = it
                item?.isVisible=true
            }
        })


        UtilsMainModel.ServerCountObserver.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                var item = binding.TLMain.getTabAt(1)?.badge
                item?.number = it
                item?.isVisible=true
            }
        })



        blurbackground(binding.blurTabLayout)
        return binding.root
    }

    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View =  activity?.window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view.setupWith(binding.MainRootShowProduct)
            .setFrameClearDrawable(binding.blurTabLayout.background)
            .setBlurAlgorithm(RenderScriptBlur(context))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(false)
            .setHasFixedTransformationMatrix(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowProductos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductosShow().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}