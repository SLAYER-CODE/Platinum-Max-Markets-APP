package com.example.fromdeskhelper.ui.View.fragment.User.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.databinding.FragmentHomeBinding
import com.example.fromdeskhelper.databinding.FragmentUserHomeBinding
import com.example.fromdeskhelper.ui.View.ViewModel.AuthenticationUserViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.Users.ClientHomeViewModel
import com.example.fromdeskhelper.ui.View.adapter.Client.BrandsAdapter
import com.example.fromdeskhelper.ui.View.adapter.Client.CategoryesAdapter
import com.example.fromdeskhelper.ui.View.adapter.Client.OffertsAdapter
import com.example.fromdeskhelper.ui.View.adapter.Client.SloganAdapter
import com.example.fromdeskhelper.ui.View.adapter.Home.HomeAdapter
import uk.co.deanwild.flowtextview.models.Line

class HomeUserFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeClient: ClientHomeViewModel by viewModels(ownerProducer = { requireParentFragment().requireActivity() });
    var adapter_slogan: SloganAdapter = SloganAdapter(mutableListOf())
    var adapter_categoryes: SloganAdapter = SloganAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.i("INFO","SELLAMOHOMEHOMEHOME")
        binding.PreviewView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.ListCategoryes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.listOfferts.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        binding.RVBrands.layoutManager=
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        homeClient.getSloganItems()
        homeClient.SloganAllPreview.observe(viewLifecycleOwner, Observer {
            Log.e("SELLAMO SLOGAN observer","No hay conexion:"+it.toString())
            if (it != null) {
                adapter_slogan = SloganAdapter(it.sloganImagesGet.toMutableList())
                binding.PreviewView.adapter = adapter_slogan
            }
        })

        homeClient.getCategoryes()
        homeClient.ListCategories.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.ListCategoryes.adapter = CategoryesAdapter(it.categoria.toMutableList())
            }
        })

        homeClient.getOfferts()
        homeClient.OffertsProduct.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.listOfferts.adapter = OffertsAdapter(it.productos.toMutableList())
            }
        })

        homeClient.getbrands()
        homeClient.ListBrands.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                binding.RVBrands.adapter = BrandsAdapter(it.brands.toMutableList())
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}