package com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentPaymentsClientBinding
import com.example.fromdeskhelper.databinding.FragmentShowAsistentP2PBinding
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.Client.P2pClientConectedAdapter
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.util.listener.recyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowAsistentP2PFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@JvmOverloads
fun RecyclerView.affectOnItemClicks(onClick: ((position: Int, view: View) -> Unit)? = null, onLongClick: ((position: Int, view: View) -> Unit)? = null) {
    println("SE APRETO CADA ITEM")
    var dato=recyclerItemClickListener(this, onClick, onLongClick)
    this.clearOnChildAttachStateChangeListeners()
    this.addOnChildAttachStateChangeListener(dato)
}




private const val LOGFRAGMENT: String = "FRAGMENTASISTENTFRAGMENT"
@AndroidEntryPoint
class ShowAsistentP2PFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentShowAsistentP2PBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context


    private val wifiViewModel: WifiVIewModel by viewModels(ownerProducer = {requireActivity()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        baseActivity.binding.appBarMain.TitleClient?.text="Asistentes Encontrados"
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowAsistentP2PBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        wifiViewModel.addGetDeviceConectItems()
        binding.LVMylist.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)

        wifiViewModel.DeviceListConectObserver.observe(viewLifecycleOwner, Observer {
            binding.LVMylist.adapter= P2pClientConectedAdapter(
                it,
                wifiViewModel.RetunrManger(),
                wifiViewModel.ReturnChangel())
            comprobateList(it.size)
            Log.i("ITEMS ENCOTRADO EN EL FRAGMENTO ASISTENTE",it.toString())

        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun comprobateList(Size:Int){
        if(Size==0) {
            binding.TNotItems.visibility=View.VISIBLE
        }else{
            binding.TNotItems.visibility=View.INVISIBLE
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
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
         * @return A new instance of fragment ShowAsistentP2PFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowAsistentP2PFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}