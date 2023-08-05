package com.example.fromdeskhelper.ui.View.fragment.modules.combine

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentBuyFastDashboardBinding
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.fragment.CameraBaseQrFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuyFastDashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuyFastDashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val binding get() = _binding!!
    private lateinit var baseActivity: EmployedMainActivity
    private lateinit var contextFragment: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var _binding: FragmentBuyFastDashboardBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmployedMainActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyFastDashboardBinding.inflate(inflater, container, false)
        val ft: FragmentTransaction = baseActivity.supportFragmentManager.beginTransaction()
        ft.replace(R.id.FLragmentCameraTool , CameraBaseQrFragment());
        ft.commit()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuyFastDashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}