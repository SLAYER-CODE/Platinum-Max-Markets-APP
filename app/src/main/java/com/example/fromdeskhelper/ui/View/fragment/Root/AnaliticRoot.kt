package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentAnaliticRootBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.AnaliticViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_main.*

private const val LOGFRAGMENT: String = "AnaliticFragmentRoot"
@AndroidEntryPoint
class AnaliticRoot : Fragment() {

    companion object {
        fun newInstance() = AnaliticRoot()
    }

    private lateinit var viewModel: AnaliticViewModel
    private var _binding: FragmentAnaliticRootBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnaliticRootBinding.inflate(inflater, container, false)
        activity?.title="Analiticas"
        return binding.root
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
        viewModel = ViewModelProvider(this).get(AnaliticViewModel::class.java)
        // TODO: Use the ViewModel
    }

}