package com.example.fromdeskhelper.ui.View.fragment.Root

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.databinding.FragmentClientsRootBinding
import com.example.fromdeskhelper.domain.WifiDirectBroadcastReceived
import com.example.fromdeskhelper.ui.View.ViewModel.Root.ClientsRootViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.listener.recyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_clients_root.view.*
import kotlinx.android.synthetic.main.item_client_list.view.*
import kotlinx.android.synthetic.main.item_device.view.*
import java.util.jar.Manifest


private const val LOGFRAGMENT: String = "ClientsFragmentRoot"
@AndroidEntryPoint
class ClientesRoot : Fragment() {

    companion object {
        fun newInstance() = ClientesRoot()
    }

    private lateinit var viewModel: ClientsRootViewModel
    private var _binding: FragmentClientsRootBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private val wifiViewModel:WifiVIewModel by viewModels(ownerProducer = {requireActivity()})
    public lateinit var Listener:WifiP2pManager.PeerListListener;
    val adaptador:P2pClientAdapter = P2pClientAdapter(mutableListOf<WifiP2pDevice>(), null,null);

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientsRootBinding.inflate(inflater, container, false)
        activity?.title="Clientes"
//        Listener=wifiViewModel.WifiListenerPers()
        return binding.root
    }

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
        wifiViewModel.onCreate()
//        Listener=wifiViewModel.WifiListenerPers();
        wifiViewModel.WifiModel.observe(viewLifecycleOwner, Observer { ResutWifi->
            Log.i("WIFIRES",ResutWifi.author)
        })
        binding.LVMylistClient.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)
//        binding.LVMylistClient.affectOnItemClicks { position, v ->
//            MessageSnackBar(view,"Se iso click",Color.GREEN)
//        }
        wifiViewModel.refreshsendListP2P( )
        wifiViewModel.DeviceListObserver.observe(viewLifecycleOwner, Observer {
            binding.LVMylistClient.adapter=P2pClientAdapter(
                it?.deviceList?: mutableListOf(),
                wifiViewModel.RetunrManger(),
                wifiViewModel.ReturnChangel())
        })

        binding.statusItem.setOnClickListener {
            if(binding.settingsItem.visibility==View.GONE){
                binding.settingsItem.visibility=View.VISIBLE
            }else{
                binding.settingsItem.visibility=View.GONE
            }
        }

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.TWifiConnect.text="Wifi ON"
                binding.SAWifi.isChecked=true
                Log.i("WIFIRESFINAL","SE ACTIVO")
            }else{
            if(wifiViewModel.VerifyWifi()){
                binding.TWifiConnect.text="Wifi ON"
                binding.SAWifi.isChecked=true
                Log.i("WIFIRESFINAL","SE ACTIVO")
            }else{
                binding.TWifiConnect.text="Wifi OFF"
                binding.SAWifi.isChecked=false
                Log.i("WIFIRESFINAL","SE DESACTIVO")
            }
            }
        }
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


        wifiViewModel.WifiActivate.observe(viewLifecycleOwner, Observer { t ->
            if(!t) {
//                val panelIntent = Intent(Settings.Panel.ACTION_WIFI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(panelIntent);
                resultLauncher.launch(Intent(Settings.Panel.ACTION_WIFI))
            }
            Log.i("WIFIRESFINAL",t.toString())
            if(wifiViewModel.VerifyWifi()){
                binding.TWifiConnect.text="Wifi ON"
                binding.SAWifi.isChecked=true
                Log.i("WIFIRESFINAL","SE ACTIVO")
                wifiViewModel.DiscoveryActivate(contextFragment)
            }else{
                binding.TWifiConnect.text="Wifi OFF"
                binding.SAWifi.isChecked=false
                Log.i("WIFIRESFINAL","SE DESACTIVO")
            }
        })

        wifiViewModel.WifiActivateBroadcast.observe(viewLifecycleOwner, Observer {
            val ViewDrawable = DrawableCompat.wrap(binding.statusItem.background);
            if(it){
                DrawableCompat.setTint(ViewDrawable,Color.GREEN)
                binding.statusItem.background=ViewDrawable
            }else{
                DrawableCompat.setTint(ViewDrawable,Color.RED)
                binding.statusItem.background=ViewDrawable
            }
        })




        binding.SAWifi.setOnClickListener{
            wifiViewModel.ActiveAndDeactiveWifi()
        }

    }

    override fun onStart() {
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
        super.onStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
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