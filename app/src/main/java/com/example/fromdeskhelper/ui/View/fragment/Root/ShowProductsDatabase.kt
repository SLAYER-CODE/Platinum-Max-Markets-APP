package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentNotificationsUsersBinding
import com.example.fromdeskhelper.databinding.FragmentShowProductsDatabaseBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ShowLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.ShowServerViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.LocalAdapter
import com.example.fromdeskhelper.util.MessageSnackBar
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProductsDatabase.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val LOGFRAGMENT: String = "SHOWFRAGMENTDATABASE"
@AndroidEntryPoint
class ShowProductsDatabase : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentShowProductsDatabaseBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private val LocalModel: ShowLocalViewModel by viewModels()
    private var adapter :LocalAdapter=LocalAdapter(listOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
    fun comprobateList(Size:Int){
        if(Size==0) {
            binding.TNotItems.visibility=View.VISIBLE
        }else{
            binding.TNotItems.visibility=View.INVISIBLE
        }
    }

    override fun onResume() {
        baseActivity.binding.appBarMain.refreshFab.setOnClickListener {
            LocalModel.GetAllProducts()
        }
        super.onResume()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LocalModel.ComprobateConnect()
        LocalModel.GetAllProducts()
        binding.LVMylist.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)
        LocalModel.AdapterSend.observe(viewLifecycleOwner, Observer {
            adapter=LocalAdapter(it.reversed())
            binding.LVMylist.adapter=adapter
            comprobateList(it.size)
//            Log.i(LOGFRAGMENT,"Se llamo"+it)
            MessageSnackBar(view,"Se actualizo!",Color.CYAN)
            binding.LVMylist.startLayoutAnimation()
        })

        LocalModel.ConnectLocalCorrect.observe(viewLifecycleOwner, Observer {
            if(it){
                MessageSnackBar(view,"SE CONECTO CORRECTAMENTE",Color.YELLOW)
            }else{
                MessageSnackBar(view,"NO SE CONECTO",Color.RED)
            }
        })
        super.onViewCreated(view, savedInstanceState)
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
        _binding = FragmentShowProductsDatabaseBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowProductsDatabase.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowProductsDatabase().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}