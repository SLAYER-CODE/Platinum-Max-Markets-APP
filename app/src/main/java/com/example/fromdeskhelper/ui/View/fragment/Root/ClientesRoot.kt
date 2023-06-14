package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentClientsRootBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.ClientsRootViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerRootClientAdapter
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.TabletPageTransformer
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


private const val LOGFRAGMENT: String = "ClientsFragmentRoot"
@AndroidEntryPoint
class ClientesRoot : Fragment() {

    companion object {
        fun newInstance() = ClientesRoot()
    }

    private lateinit var viewModel: ClientsRootViewModel
    private var _binding: FragmentClientsRootBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    private val wifiViewModel:WifiVIewModel by viewModels(ownerProducer = {requireActivity()})
    public lateinit var Listener:WifiP2pManager.PeerListListener;
    val adaptador:P2pClientAdapter = P2pClientAdapter(mutableListOf<WifiP2pDevice>(), null,null);

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientsRootBinding.inflate(inflater, container, false)
        activity?.title=baseActivity.resources.getString(R.string.menu_CallFinalUser)

//        Listener=wifiViewModel.WifiListenerPers()
        return binding.root
    }
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        class itemClickListener : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                TODO("Not yet implemented")
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        wifiViewModel.onCreate()
//        Listener=wifiViewModel.WifiListenerPers();

        val adapter by lazy { ViewPagerRootClientAdapter(baseActivity) }
        binding.paggeclientid.adapter=adapter;
        binding.paggeclientid.setPageTransformer(TabletPageTransformer())


        TabLayoutMediator(binding.TLMainRootClient,binding.paggeclientid,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text=baseActivity.resources.getString(R.string.home_tab_menus_client_server)
                        tab.setIcon(R.drawable.baseline_person_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=77
                        Badged.isVisible=true
                    }
                    1->{
                        tab.text=baseActivity.resources.getString(R.string.home_tab_menus_client_local)
                        tab.setIcon(R.drawable.ic_baseline_broadcast_on_personal_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=120
                        Badged.isVisible=true

                    }

                }
            }).attach()


        wifiViewModel.WifiModel.observe(viewLifecycleOwner, Observer { ResutWifi->
            Log.i("WIFIRES",ResutWifi.author)
        })




        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    MessageSnackBar(view,"Se concedio Permisos de Ubicacion",Color.LTGRAY)
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    MessageSnackBar(view,"Se concedio Permisos de Ubicacion En la zona",Color.CYAN)
                } else -> {
                    MessageSnackBar(view,"No se concedieron los permisos ",Color.RED)
                // No location access granted.
            }
            }
        }
        var resultado=locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))
        Log.i(LOGFRAGMENT,resultado.toString())

        val mPermissionResult = registerForActivityResult(
            RequestPermission()
        ) { result ->
            if (result) {
                Log.e("PERMISSIONS", "onActivityResult: PERMISSION GRANTED")
            } else {
                Log.e("PERMISSIONS", "onActivityResult: PERMISSION DENIED")
            }
        }


        wifiViewModel.WifiActivateBroadcast.observe(viewLifecycleOwner, Observer {
            val ViewDrawable = DrawableCompat.wrap(binding.appBarLayout.background).mutate();
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.GREEN)
                        binding.appBarLayout.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.RED)
                        binding.appBarLayout.background = ViewDrawable
                    }
                }
                Configuration.UI_MODE_NIGHT_NO->{
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.CYAN)
                        binding.appBarLayout.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.GRAY)
                        binding.appBarLayout.background = ViewDrawable
                    }
                }
            }
        })




        //binding.SAWifi.setOnClickListener{
        //    wifiViewModel.ActiveAndDeactiveWifi()
        //}

    }

    override fun onStart() {
        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.GONE
        }
        if(baseActivity.binding.appBarMain.toolbarParent?.visibility==View.GONE){
            baseActivity.binding.appBarMain.toolbarParent?.visibility=View.VISIBLE
        }
//        if(baseActivity.binding.appBarMain.fab.visibility==View.GONE){
//            baseActivity.binding.appBarMain.fab.visibility=View.VISIBLE
//        }
//        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.GONE){
//            baseActivity.binding.appBarMain.refreshFab.visibility=View.VISIBLE
//        }
//        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
//            wifiViewModel.onCreate()
//        }
        super.onStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
//        TODO("Not yet implemented")
//        if(p0!=null) {
//            peers.clear()
//            peers.addAll(p0.deviceList ?: listOf())
//            var index = 0
//            for (device: WifiP2pDevice in p0?.deviceList){
//                Log.i("P2PRECONECT",device.deviceName)
//            }
//        }else{
//            Log.i("P2PRECONECT","NOT FOUNT ITEM")
//        }
//    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ClientsRootViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}