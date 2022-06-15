package com.example.fromdeskhelper.ui.View.fragment.Root.Clients

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.databinding.FragmentClientItemShowBinding
import com.example.fromdeskhelper.ui.View.ViewModel.ClientLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.View.adapter.ShopingProductAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClientItemShowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val LOGFRAGMENT: String = "CLIENTOPENITEM"
@AndroidEntryPoint
class ClientItemShowFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    protected lateinit var baseActivity: MainActivity
    protected lateinit var contextFragment: Context
    private var _binding: FragmentClientItemShowBinding? = null
    private val binding get() = _binding!!
    private var uid:Int=-1;
    private val ClienLocalModel: ClientLocalViewModel by viewModels(ownerProducer = {
        requireActivity()
    })
    private val wifiViewModel: WifiVIewModel by viewModels(ownerProducer = {requireActivity()})
    private val ModelSendModel: ShowMainViewModel by viewModels(ownerProducer = {requireActivity()})


    private var CategoryAdapter: MutableList<String> = mutableListOf();

    private var adapter:ShopingProductAdapter= ShopingProductAdapter(mutableListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid=it.getInt("uid");
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
        _binding = FragmentClientItemShowBinding.inflate(inflater, container, false)
        blurbackground(binding.blurViewCard)
        return binding.root
    }
    fun comprobateList(Size: Int) {
        if (Size == 0) {
            binding.TNotItems.visibility = View.VISIBLE
            binding.ContainerAll.visibility=View.GONE
        } else {
            binding.TNotItems.visibility = View.GONE
            binding.ContainerAll.visibility=View.VISIBLE
        }
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }
    fun update(){
        var Count: Double = 0.0
        for (a in adapter.producto) {
            Count += a.precioC
        }
        binding.TEPrecioTotal.text=Count.toString()
        binding.TEPrecio.setText(Count.toString())
        comprobateList(adapter.producto.size)
    }


    private fun addChipToGroup(person: String, chipGroup: ChipGroup, color: Int, Icon: Int) {
        val chip = Chip(context)
        chip.text = person
        chip.chipIcon = ContextCompat.getDrawable(requireContext(), Icon)
        chip.setChipBackgroundColorResource(color)
        chip.isCloseIconEnabled = true

        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.LVMylistProductComprar.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)

        ClienLocalModel.GetRelation(uid)
        ClienLocalModel.itemClientProducts.observe(viewLifecycleOwner, Observer {
            Log.i(LOGFRAGMENT,it.toString())
            adapter= ShopingProductAdapter(it.products.toMutableList(), ClienLocalModel::DeleteRelaton,uid,this::update)
            binding.LVMylistProductComprar.adapter = adapter
            update()
        })
        binding.BMiminizeLog.setOnClickListener {
            ClienLocalModel.closeViewInterface()
        }
        binding.SendItem.setOnClickListener {

        }

//        binding.LVMylistProductComprar.addOnItemTouchListener(
//            RecyclerViewItemClickListener(
//            context,binding.LVMylistProductComprar,object : ShowProducts.ClickListener {
//                    override fun onClick(view: View?, position: Int) {
//                        adapter.producto[position].uid
//                        adapter.removeItem(position)
//                        comprobateList(adapter.producto.size)
//                    }
//
//                    override fun onLongClick(view: View?, position: Int) {
//                    }
//                }
//            )
//        )

        wifiViewModel.refreshsendListP2P( )
        wifiViewModel.DeviceListObserver.observe(viewLifecycleOwner, Observer {
            val itemsAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(baseActivity, R.layout.simple_list_item_1, mutableListOf<String>())

            CategoryAdapter.clear()

            if(it!=null) {
                for (x in it.deviceList) {
                    itemsAdapter.add(x.deviceName)
                    CategoryAdapter.add(x.deviceName)
                }
                binding.TENombre?.setAdapter<ArrayAdapter<String>>(itemsAdapter)
            }
        })
        binding.TENombre.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (binding.TENombre.text.toString() in CategoryAdapter) {
                        addChipToGroup(
                            binding.TENombre.text.toString(), binding.Clients!!,
                            com.example.fromdeskhelper.R.color.md_green_600, com.example.fromdeskhelper.R.drawable.background_person
                        )
                    }
                    binding.TENombre.setText("");
                    return true
                }
                return false
            }
        })
        binding.SendItem.setOnClickListener {
            ModelSendModel.ResTFacture.postValue(adapter.producto)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View =  activity?.window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(context))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(false)
            .setHasFixedTransformationMatrix(false)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientItemShowFragment.
         */
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientItemShowFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClientItemShowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}