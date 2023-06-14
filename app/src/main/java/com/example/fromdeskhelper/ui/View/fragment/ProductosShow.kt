package com.example.fromdeskhelper.ui.View.fragment

import Data.listInventarioProductos
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
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
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.*
import com.example.fromdeskhelper.util.TabletPageTransformer
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.fragment_productos_show.*
//import kotlinx.android.synthetic.main.fragment_show_products.view.*
//import kotlinx.android.synthetic.main.item_client_list.view.*
//import kotlinx.android.synthetic.main.item_producto.view.*
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
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    lateinit var daoNew: AppDatabase

    var currentPage:Int=6
    var itemfinal:Boolean=false;
    private var listaProductos = emptyList<listInventarioProductos>()
//    var adaptador: ProductoAdapter = ProductoAdapter(listOf());
    private val UtilsMainModel:UtilsShowMainViewModels by viewModels(ownerProducer = {requireActivity()})

    //Item Productos
    private val MainModel : ShowMainViewModel by viewModels(ownerProducer = {requireActivity()});

    //ADD action bar base activity config
    private val CameraView: CameraViewModel by viewModels(ownerProducer = {
        requireActivity()
    })

    private var CamScannerStatus: Boolean = false;
    private val MainView: MainActiviyViewModel by viewModels()


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
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    override fun onStart() {
        super.onStart()
        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.GONE
        }
     /*   if(binding.toolbarParent.visibility==View.GONE){
           binding.toolbarParent.visibility=View.VISIBLE
        }*/
//        if(baseActivity.binding.appBarMain.fab.visibility==View.GONE){
//            baseActivity.binding.appBarMain.fab.visibility=View.VISIBLE
//        }
//        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.GONE){
//            baseActivity.binding.appBarMain.refreshFab.visibility=View.VISIBLE
//        }
//        (baseActivity as MainActivity).functionFabRefresh(::refreshList)
//        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
//            refreshList()
//        }
        binding.BIShowP.visibility=View.VISIBLE
//        baseActivity.binding.appBarMain.refreshFab.setImageResource(R.drawable.ic_baseline_autorenew_24)

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
//        baseActivity.setRefreshMain()
        MainModel.GetCount().observe(viewLifecycleOwner, onChanged = {
            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        })

        super.onResume()
    }


    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductosShowBinding.inflate(inflater, container, false)

        val navcontroller =findNavController()
        val appbarlayout= AppBarConfiguration(navcontroller.graph)
        binding.toolbar?.setupWithNavController(navcontroller,appbarlayout)


        //baseActivity.setActionBar(binding.toolbar as Toolbar)
//Configuraciones de la barra de herramientas

        //SE TERMINO LA CONECTIVADDD
//        CameraView.CameraActivate.observe(viewLifecycleOwner, Observer {
//            if (it == CameraTypes.SCANER) {
//                binding.BQRScanner?.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)
//                baseActivity.binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)
//                baseActivity.binding.appBarMain.constraintLayout.visibility = View.VISIBLE
//            } else {
//                binding.BQRScanner?.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
//                baseActivity.binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
//                if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    baseActivity.binding.appBarMain.constraintLayout.visibility = View.GONE
//                } else {
//                    baseActivity.binding.appBarMain.constraintLayout.visibility = View.INVISIBLE
//                }
//                CamScannerStatus = false
//            }
//        })

        CameraView.QRBindig.observe(viewLifecycleOwner, Observer {
            Log.i("QRRESIVED", it)
            binding.SVProducts.setQuery(it, false)
            binding.SVProducts.clearFocus()
        })




        var Listener = View.OnClickListener {
            if (!CamScannerStatus) {
                MainView.RestoreCamera()
                val ft: FragmentTransaction =  parentFragmentManager.beginTransaction()
                ft.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                CameraView.CloseInFragment(false)
                CameraView.CamaraStatus(CameraTypes.SCANER, true)
                ft.replace(R.id.FragmentCamera, CameraQrFragment());
                ft.setReorderingAllowed(true)
                ft.commit()
                CamScannerStatus = true;
            } else {
                CameraView.CloseInFragment(true)
                CameraView.CamaraStatus(CameraTypes.NULL, true)
                CamScannerStatus = false;
            }
        }
        binding.BQRScanner.setOnClickListener(Listener)
//        baseActivity.binding.appBarMain.BQRScannerClient.setOnClickListener(Listener)





        //((binding.cordinaterClient.layoutParams) as CoordinatorLayout.LayoutParams).behavior = ClientNavigationBehavior()



        //Desde aca se borro el bloque

        binding.paggerid.setPageTransformer(TabletPageTransformer())



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

        val tablayoutnavigator=TabLayoutMediator(binding.TLMain,binding.paggerid as ViewPager2,false,
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



        return binding.root
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