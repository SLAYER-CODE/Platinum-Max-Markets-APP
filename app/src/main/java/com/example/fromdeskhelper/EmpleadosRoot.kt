package com.example.fromdeskhelper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EmpleadosRoot : Fragment() {

    companion object {
        fun newInstance() = EmpleadosRoot()
    }

    private lateinit var viewModel: EmpleadosRootViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.empleados_root_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EmpleadosRootViewModel::class.java)
        // TODO: Use the ViewModel
    }

}