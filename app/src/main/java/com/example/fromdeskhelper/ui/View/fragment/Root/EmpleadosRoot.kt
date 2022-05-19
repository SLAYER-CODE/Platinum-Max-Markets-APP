package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentEmpleadosRootBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.EmpleadosRootViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

private const val LOGFRAGMENT: String = "EmpleadosFragmentRoot"
@AndroidEntryPoint
class EmpleadosRoot : Fragment() {

    companion object {
        fun newInstance() = EmpleadosRoot()
    }

    private lateinit var viewModel: EmpleadosRootViewModel
    private var _binding: FragmentEmpleadosRootBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmpleadosRootBinding.inflate(inflater, container, false)
        activity?.title="Empleados del local"
        return binding.root
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EmpleadosRootViewModel::class.java)
        // TODO: Use the ViewModel
    }

}