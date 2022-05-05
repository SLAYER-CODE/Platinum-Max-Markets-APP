package com.example.fromdeskhelper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ClientesRoot : Fragment() {

    companion object {
        fun newInstance() = ClientesRoot()
    }

    private lateinit var viewModel: FragmentClientsRootViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clients_root_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentClientsRootViewModel::class.java)
        // TODO: Use the ViewModel
    }

}