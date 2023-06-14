package com.example.fromdeskhelper.ui.View.fragment.Client

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fromdeskhelper.databinding.FragmentLocationClientBinding
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val LOGFRAGMENT: String = "FRAGMENTCLIENTFRAGMENT"
@AndroidEntryPoint
class LocationClientFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLocationClientBinding? = null
    private val binding get() = _binding!!
    protected lateinit var baseActivity: EmployedMainActivity
    protected lateinit var contextFragment: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart() {
        if(baseActivity.binding.appBarMain.toolbarParent?.visibility==View.VISIBLE){
            baseActivity.binding.appBarMain.toolbarParent?.visibility=View.GONE
        }
//        if(baseActivity.binding.appBarMain.fab.visibility==View.VISIBLE){
//            baseActivity.binding.appBarMain.fab.visibility=View.GONE
//        }
//        if(baseActivity.binding.appBarMain.refreshFab.visibility==View.VISIBLE){
//            baseActivity.binding.appBarMain.refreshFab.visibility=View.GONE
//        }

        if(baseActivity.binding.appBarMain.toolbarParentClient!!.visibility==View.GONE){
            baseActivity.binding.appBarMain.toolbarParentClient!!.visibility=View.VISIBLE
        }
        super.onStart()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationClientBinding.inflate(inflater, container, false)
        activity?.title="Ubicaciones "
        return binding.root
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
         * @return A new instance of fragment LocationClientFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}