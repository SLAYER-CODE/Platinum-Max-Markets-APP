package com.example.fromdeskhelper.ui.View.fragment.Client.ChildFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.databinding.FragmentShowProductsP2PBinding
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ProductsP2PViewMode
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.Client.ProductoRecivedP2PAdapter
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowProductsP2PFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val LOGFRAGMENT: String = "SHOWPRODUCTSFRAGMENTSP2P"
@AndroidEntryPoint
class ShowProductsP2PFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentShowProductsP2PBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context
    private val PP2Pproducts: ProductsP2PViewMode by viewModels(ownerProducer = {requireActivity()})
    var adaptador: ProductoRecivedP2PAdapter = ProductoRecivedP2PAdapter(listOf(),null);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onResume() {
        baseActivity.binding.appBarMain.TitleClient?.text="Productos en Oferta"
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowProductsP2PBinding.inflate(inflater, container, false)
        binding.LVMylist.layoutManager=
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)

        PP2Pproducts.RequestItem()
        PP2Pproducts.ItemsRecivedTransmited.observe(viewLifecycleOwner, Observer {
            adaptador= ProductoRecivedP2PAdapter(it,null)
            adaptador.notifyItemRangeInserted(0,it.size)
            binding.LVMylist.adapter=adaptador
            comprobateList(it.size)
        })

//        PP2Pproducts.ItemsRecived.observe(viewLifecycleOwner, Observer {
//            adaptador= ProductoRecivedP2PAdapter(it,null)
//            adaptador.notifyItemRangeInserted(0,it.size)
//            comprobateList(it.size)
////            binding.LVMylist.startLayoutAnimation()
//        })



        return binding.root
    }


    fun comprobateList(Size:Int){
        if(Size==0) {
            binding.TNotItems.visibility=View.VISIBLE
        }else{
            binding.TNotItems.visibility=View.INVISIBLE
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
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
         * @return A new instance of fragment ShowProductsP2PFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowProductsP2PFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}