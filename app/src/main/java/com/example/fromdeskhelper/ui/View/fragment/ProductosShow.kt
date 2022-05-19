package com.example.fromdeskhelper.ui.View.fragment

import Data.listInventarioProductos
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.db.AppDatabase
import com.example.fromdeskhelper.data.model.objects.Constants
import com.example.fromdeskhelper.databinding.FragmentNotificationsUsersBinding
import com.example.fromdeskhelper.databinding.FragmentProductosShowBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerMainAdapter
import com.example.fromdeskhelper.util.listener.infiniteScrollListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_show_products.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProductos.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    private val MainModel : ShowMainViewModel by viewModels(ownerProducer = {requireActivity()});



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
        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
            refreshList()
        }
        baseActivity.returnbinding().appBarMain.BIShowP.visibility=View.VISIBLE
        (activity as MainActivity).returnbinding().appBarMain.refreshFab.setImageResource(R.drawable.ic_baseline_autorenew_24)
    }



    fun refreshList(){
        binding.paggerid.LVMylist.startLayoutAnimation()
    }


    override fun onResume() {
//        daoNew.productosData().getCount().observe(viewLifecycleOwner,onChanged = {
//            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
//            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
//            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
//        })
//
//
        MainModel.GetCount().observe(viewLifecycleOwner, onChanged = {
            activity?.setTitle(if(it==0)"Inicio" else ("${it} Productos en Total"))
            val gridLayoutManager = GridLayoutManager(baseActivity, 1)
            gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        })

        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductosShowBinding.inflate(inflater, container, false)
        val adapter by lazy { ViewPagerMainAdapter(baseActivity) }
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


        binding.paggerid.adapter=adapter;
        val tablayoutnavigator=TabLayoutMediator(binding.TLMain,binding.paggerid,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text="Storage"
                        tab.setIcon(R.drawable.ic_baseline_sd_storage_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=10
                        Badged.isVisible=true
                    }
                    1->{
                        tab.text="Server"
                        tab.setIcon(R.drawable.ic_baseline_cloud_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=2220
                        Badged.isVisible=true
                    }
                    2->{
                        tab.text="Local"
                        tab.setIcon(R.drawable.ic_baseline_store_24)
                        val Badged:BadgeDrawable=tab.orCreateBadge
                        Badged.backgroundColor=ContextCompat.getColor(baseActivity,R.color.md_red_400)
                        Badged.number=20
                        Badged.isVisible=true
                    }
                }
        })

        tablayoutnavigator.attach()
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