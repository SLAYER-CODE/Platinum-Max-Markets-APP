package com.example.fromdeskhelper.ui.View.fragment.Client

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentShowMainClientBinding
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerMainAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerMainAdapterClient
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowMainClient.newInstance] factory method to
 * create an instance of this fragment.
 */


private const val LOGFRAGMENT: String = "SHOWMAINCLIENTFRAGMENT"
@AndroidEntryPoint
class ShowMainClient : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentShowMainClientBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart() {
        if(baseActivity.binding.appBarMain.toolbarParent.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParent.visibility=View.GONE
        }
        if(baseActivity.binding.appBarMain.fab.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.fab.visibility=View.GONE
        }
        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.refreshFab.visibility=View.GONE
        }

        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.GONE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.VISIBLE
        }
        baseActivity.binding.appBarMain.TitleClient?.text="Cliente Productos"
        super.onStart()
    }

    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View =  activity?.window!!.decorView
//        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view.setupWith(binding.PaggerIdClient)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(baseActivity))
            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowMainClientBinding.inflate(inflater, container, false)
        val adapter by lazy { ViewPagerMainAdapterClient(baseActivity) }
        binding.PaggerIdClient.adapter=adapter;
        val tablayoutnavigator= TabLayoutMediator(binding.TLMainCLient,binding.PaggerIdClient,
            TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
                when(position){
                    0->{
                        tab.text="P2P"
                        tab.setIcon(R.drawable.ic_baseline_surround_sound_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity,R.color.md_green_400)
                        Badged.number=77
                        Badged.isVisible=true
                    }
                    1->{
                        tab.text="Public"
                        tab.setIcon(R.drawable.ic_baseline_list_alt_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity,R.color.md_green_400)
                        Badged.number=120
                        Badged.isVisible=true

                    }
                    2->{
                        tab.text="Asistent"
                        tab.setIcon(R.drawable.ic_baseline_assignment_ind_24)
                        val Badged: BadgeDrawable =tab.orCreateBadge
                        Badged.backgroundColor= ContextCompat.getColor(baseActivity,R.color.md_green_400)
                        Badged.number=12
                        Badged.isVisible=true
                    }
                }
            })
        tablayoutnavigator.attach()
        blurbackground(binding.blurTabLayout)
        return binding.root
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
         * @return A new instance of fragment ShowMainClient.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowMainClient().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}