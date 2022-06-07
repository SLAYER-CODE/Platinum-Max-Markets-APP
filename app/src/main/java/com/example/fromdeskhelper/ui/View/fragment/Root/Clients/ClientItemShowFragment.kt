package com.example.fromdeskhelper.ui.View.fragment.Root.Clients

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.FragmentCameraQrBinding
import com.example.fromdeskhelper.databinding.FragmentClientItemShowBinding
import com.example.fromdeskhelper.ui.View.activity.MainActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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