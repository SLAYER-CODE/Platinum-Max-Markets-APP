package com.example.fromdeskhelper.ui.View.fragment

import Data.HomeListClient
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentDetallesProductsBinding
import com.example.fromdeskhelper.databinding.FragmentHomeBinding
import com.example.fromdeskhelper.databinding.FragmentLoginBinding
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.Home.HomeAdapter
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var baseActivity: MainActivity
    private lateinit var contextFragment: Context
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var adapter: HomeAdapter = HomeAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val navcontroller =findNavController()
        val appbarlayout= AppBarConfiguration(navcontroller.graph)
        binding.toolbar?.setupWithNavController(navcontroller,appbarlayout)

        adapter= HomeAdapter(mutableListOf<HomeListClient>(HomeListClient("Registrar un producto","Inserte la informacion de su producto, si este fue registrado sera notificado")))
        binding.RVMenuHome.adapter=adapter
        binding.RVMenuHome.layoutManager = GridLayoutManager(baseActivity,2, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}