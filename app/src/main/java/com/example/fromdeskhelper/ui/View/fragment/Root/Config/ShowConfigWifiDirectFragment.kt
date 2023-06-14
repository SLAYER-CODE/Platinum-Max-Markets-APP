package com.example.fromdeskhelper.ui.View.fragment.Root.Config

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.databinding.FragmentShowConfigWifiDirectBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.ClientsRootViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowConfigWifiDirectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowConfigWifiDirectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: ClientsRootViewModel
    private var _binding: FragmentShowConfigWifiDirectBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    private val wifiViewModel: WifiVIewModel by viewModels(ownerProducer = {requireActivity()})
    public lateinit var Listener: WifiP2pManager.PeerListListener;
    val adaptador: P2pClientAdapter = P2pClientAdapter(mutableListOf<WifiP2pDevice>(), null,null);



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowConfigWifiDirectBinding.inflate(inflater, container, false)

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



        binding.SAWifi.setOnClickListener{
            wifiViewModel.ActiveAndDeactiveWifi()
        }

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowConfigWifiDirectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowConfigWifiDirectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}