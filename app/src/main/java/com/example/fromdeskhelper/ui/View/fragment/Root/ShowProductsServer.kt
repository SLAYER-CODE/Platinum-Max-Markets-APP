package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentShowProductsDatabaseBinding
import com.example.fromdeskhelper.databinding.FragmentShowProductsServerBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ShowServerViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProductsServer.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class ShowProductsServer : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentShowProductsServerBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private val ServerModel: ShowServerViewModel by viewModels()
    private var adaptador: ServerAdapter = ServerAdapter(listOf());


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
        _binding = FragmentShowProductsServerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
            ServerModel.GetProductsAllPreview()
        }
        super.onResume()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.LVMylist.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)
        ServerModel.GetProductsAllPreview()
        ServerModel.ProductsAllPreview.observe(viewLifecycleOwner, Observer {
            adaptador=ServerAdapter(it?.productos!!.reversed())
            binding.LVMylist.adapter=adaptador
            comprobateList(it?.productos.size)
            binding.LVMylist.startLayoutAnimation()
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


}