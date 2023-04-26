package com.example.fromdeskhelper.ui.View.fragment.Root

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentAnaliticRootBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Root.AnaliticViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerRootAnaliticAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerRootClientAdapter
import com.example.fromdeskhelper.util.TabletPageTransformer
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
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


        val adapter by lazy { ViewPagerRootAnaliticAdapter(baseActivity) }
        binding.paggeclientid.adapter=adapter;
        binding.paggeclientid.setPageTransformer(TabletPageTransformer())


        TabLayoutMediator(binding.TLMainRootClient,binding.paggeclientid,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text="Clientes"
                        tab.setIcon(R.drawable.baseline_face_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=77
                        Badged.isVisible=true
                    }
                    1->{
                        tab.text="Productos"
                        tab.setIcon(R.drawable.ic_baseline_shopping_cart_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity, R.color.md_green_400)
                        Badged.number=120
                        Badged.isVisible=true

                    }

                }
            }).attach()

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