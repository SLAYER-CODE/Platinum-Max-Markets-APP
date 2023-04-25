package com.example.fromdeskhelper.ui.View.fragment.Client

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentShowMainClientBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Client.FactureProduct
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ShowMainClientViewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.MessageAdapter
import com.example.fromdeskhelper.ui.View.adapter.ShopingContrateProductAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerMainAdapter
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerMainAdapterClient
import com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments.affectOnItemClicks
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
    private val ShowMainClientModel : ShowMainClientViewModel by viewModels(ownerProducer = {requireActivity()})
    private var OpenItem:Boolean=true
    private var Facture= mutableListOf<MutableList<FactureProduct>>()
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

        binding.ClientFactures.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL,true)
        binding.ClientFacturesItems.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,true)
//        ShowMainClientModel.ItemsRecivedTransmited.postValue(mutableListOf())


        ShowMainClientModel.ItemsRecivedTransmited.observe(viewLifecycleOwner, Observer {
            Facture=it
            binding.ClientFactures.adapter=MessageAdapter((0 until it.size).toMutableList())
        })


        binding.ClientFactures.affectOnItemClicks(onClick = { position, view: View ->
            if(OpenItem) {
                scaleView(view, 1f, 1.3f)
                binding.ClientFacturesItems.adapter=ShopingContrateProductAdapter(Facture[position])
                var tem=0.0
                for ( s in Facture[position]){
                    tem+=s.price
                }
                binding.TEPrecioTotal.text=tem.toString()
                OpenItem=false
                binding.DetallesSHOW.visibility=View.VISIBLE
            }else{
                scaleView(view, 1f, 1f)
                OpenItem=true
                binding.DetallesSHOW.visibility=View.GONE
            }
        })

        blurbackground(binding.blurTabLayout)
        return binding.root
    }
    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            startScale, endScale,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 200
        v.startAnimation(anim)
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