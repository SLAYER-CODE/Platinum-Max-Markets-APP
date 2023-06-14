package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentClientesConfigBinding
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
 * Use the [ConfigurationRoot.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigurationRoot : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null


    private lateinit var viewModel: ClientsRootViewModel
    private var _binding: FragmentClientesConfigBinding? = null
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
        _binding = FragmentClientesConfigBinding.inflate(inflater, container, false)
        activity?.title=baseActivity.resources.getString(R.string.menu_cofiguration)

//        Listener=wifiViewModel.WifiListenerPers()


        //binding.statusItem.setOnClickListener {
        //    if(binding.settingsItem.visibility==View.GONE){
        //        binding.settingsItem.visibility=View.VISIBLE
        //    }else{
        //        binding.settingsItem.visibility=View.GONE
        //    }
        //}



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
         * @return A new instance of fragment ClientesConfig.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConfigurationRoot().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}