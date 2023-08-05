package com.example.fromdeskhelper.ui.View.fragment

import Data.HomeListClient
import android.content.Context
import android.content.res.Configuration
import android.location.GnssAntennaInfo.Listener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fromdeskhelper.GetRolesAdminQuery
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.data.model.objects.User
import com.example.fromdeskhelper.databinding.FragmentHomeBinding
import com.example.fromdeskhelper.databinding.ItemMenuHomeListBinding
import com.example.fromdeskhelper.ui.View.ViewModel.AuthenticationUserViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.MainActiviyViewModel
import com.example.fromdeskhelper.ui.View.activity.EmployedMainActivity
import com.example.fromdeskhelper.ui.View.adapter.Home.HomeAdapter
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.launch
//import kotlinx.android.synthetic.main.item_menu_home_list.view.lottieAnimationView
import java.util.Objects

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

var LOG_FRAGMENT_HOME = "FragmentHome"

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    //    private var param2: String? = null
    private lateinit var baseActivity: EmployedMainActivity
    private lateinit var contextFragment: Context
    private var _binding: FragmentHomeBinding? = null
    private val AutenticationModel: AuthenticationUserViewModel by viewModels(ownerProducer = { requireActivity() });

    private val binding get() = _binding!!
    var adapter_user: HomeAdapter = HomeAdapter(mutableListOf(), null)
    var adapter_admin: HomeAdapter = HomeAdapter(mutableListOf(), null)
    var adapter_anonime: HomeAdapter = HomeAdapter(mutableListOf(), null)
    private val MainView: MainActiviyViewModel by viewModels(ownerProducer = { requireActivity() })
    private var CamScannerStatus: Boolean = false;
    private val CameraView: CameraViewModel by viewModels(ownerProducer = {
        requireActivity()
    })
    private lateinit var appBarConfiguration: AppBarConfiguration


    //Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val navcontroller = findNavController()
        appBarConfiguration = AppBarConfiguration(navcontroller.graph)
        binding.toolbar.setupWithNavController(navcontroller, appBarConfiguration)
        baseActivity.setSupportActionBar(binding.toolbar)
//        var navController = baseActivity.findNavController(R.id.nav_host_fragment_content_mains)
//        appBarConfiguration = AppBarConfiguration.Builder(
//            R.id.HomeFragmnet,
//            R.id.clientsRoot,
//            R.id.analiticRoot,
//            R.id.empleadosRoot,
//            R.id.notificationsRoot,
//            R.id.showproductsClient,
//            R.id.locationClient,
//            R.id.ordersClient,
//            R.id.favoriteClient,
//            R.id.paymentsClient
//        ). setDrawerLayout(baseActivity?.binding?.drawerLayout).build()
//        baseActivity.setupActionBarWithNavController(navController,appBarConfiguration)
        Log.i("Añandiendo instrucciones", "se realizo un tolbar a la barra")

//        appBarConfiguration =AppBarConfiguration.Builder(
//            R.id.HomeFragmnet,
//            R.id.clientsRoot,
//            R.id.analiticRoot,
//            R.id.empleadosRoot,
//            R.id.notificationsRoot,
//            R.id.showproductsClient,
//            R.id.locationClient,
//            R.id.ordersClient,
//            R.id.favoriteClient,
//            R.id.paymentsClient
//        ). setDrawerLayout(baseActivity.binding.drawerLayout).build()

//        navcontroller = findNavController(R.id.nav_host_fragment_content_mains)

//        appBarConfiguration = AppBarConfiguration(navController.graph).

        //        AutenticationModel.getCUser()
        //Listener camara de indentado
        fun toogleCamer() {
            if (!CamScannerStatus) {
                //ENABLE CAMERA
                MainView.RestoreCamera()
                CameraView.CloseInFragment(false)
                CameraView.CamaraStatus(CameraTypes.SCANER, true)
                CamScannerStatus = true;
            } else {
                //CLOSE CAMERA
                CameraView.CloseInFragment(true)
                CameraView.CamaraStatus(CameraTypes.NULL, true)
                CamScannerStatus = false;
            }
        }



        var ListenerAnimation = object : HomeAdapter.OnItemListener {
            override fun OnItemTouch(view: View?, motion: MotionEvent?): Boolean {
                when (motion?.action) {
                    MotionEvent.ACTION_DOWN -> {
//                        item_menu_home_list
                        if (!ItemMenuHomeListBinding.bind(view!!).lottieAnimationView.isAnimating) {
                            ItemMenuHomeListBinding.bind(view!!).lottieAnimationView.playAnimation()
                        }
                        return true
                    }
                }
                return true
            }
        }



        AutenticationModel.CConnection.observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                baseActivity,
                R.string.splash_connect_pending, Toast.LENGTH_SHORT
            ).show()
        })


        AutenticationModel.CDataAsociate.observe(viewLifecycleOwner, Observer {
            binding.RoleSuper.text = getString(R.string.titleAdmin)
            binding.RoleSeudo.text = getString(R.string.titleAdminSeudo)
            binding.TVNick.text = "(" + it.getDataAsociate.user_id.email + ")"
//            binding.toolbar.title=it.getDataAsociate.nik
//            baseActivity.setTitle("pwd")

            if (it.getDataAsociate.user_id.photo != null) {
                Picasso.get()
                    .load(it.getDataAsociate.user_id.photo).fit()
                    .transform(CropCircleTransformation())
//                    .into(binding.drawerLayout.nav_view.getHeaderView(0).findViewById(R.id.ImageUserPresentation) as ImageView)
                    .into(binding.IVLogo)
            }
        })

        lifecycleScope.launch {
            AutenticationModel.CObject.collect { it ->
                Log.i("CALLBACKSTACK", "SE LLAMO OTRA VEZ")
                if (it == User.ADMIN) {
                    binding.lottieAnimationView.setAnimation(R.raw.administracion)
                    AutenticationModel.getDataUser()
                } else if (it == User.EMPLOYED) {
                    binding.lottieAnimationView.setAnimation(R.raw.employed)
                } else {
                    binding.lottieAnimationView.setAnimation(R.raw.dashboard)
                }
                AutenticationModel.getFunctions()
            }
        }

//        AutenticationModel.CObject.observe(viewLifecycleOwner, Observer {
//
//        })


        AutenticationModel.CFuctionsAnonime.observe(viewLifecycleOwner, Observer {
            Log.i("Añandiendo instrucciones", "se inserto nuevamanete el adaptardo")
            var adapter = HomeAdapter(it, context,::toogleCamer)
            adapter.setOnItemListenerListener(ListenerAnimation)
            binding.RVMenuHomeAnoime.adapter = adapter
            binding.RVMenuHomeAnoime.layoutManager =
                GridLayoutManager(baseActivity, 2, LinearLayoutManager.VERTICAL, false)
        })
        binding.RVMenuUser.layoutManager =
            GridLayoutManager(baseActivity, 2, LinearLayoutManager.VERTICAL, false)
        binding.RVMenuHomeAnoime.layoutManager =
            GridLayoutManager(baseActivity, 2, LinearLayoutManager.VERTICAL, false)
        binding.RVMenuHome.layoutManager =
            GridLayoutManager(baseActivity, 2, LinearLayoutManager.VERTICAL, false)

        AutenticationModel.CFuctions.observe(viewLifecycleOwner, Observer {
            var admin_functions = mutableListOf<GetRolesAdminQuery.Fuction>()
            var user_functions = mutableListOf<GetRolesAdminQuery.Fuction>()
            var faste_or_anonime = mutableListOf<GetRolesAdminQuery.Fuction>()
            if (it != null) {
                binding.LLError.visibility = View.GONE
                binding.LAError.cancelAnimation()
                binding.BRlogin.isEnabled = true

                binding.CVPrimary.visibility = View.VISIBLE

                for (fc in it.getRoles.fuctions) {
                    if (!fc.anonime_acces) {
                        if (fc.user_acces) {
                            user_functions.add(fc)
                        } else {
                            admin_functions.add(fc)
                        }
                    } else {
                        faste_or_anonime.add(fc)
                    }
                }
                if (it.getRoles.user) {
                    adapter_user = HomeAdapter(user_functions, context,::toogleCamer)
                    binding.TVUserSecond.visibility = View.VISIBLE
                    binding.RVMenuUser.visibility = View.VISIBLE
                    binding.CVSecondary.visibility = View.VISIBLE
//                    it.getRoles.fuctions
                    adapter_user.setOnItemListenerListener(ListenerAnimation)
                    binding.RVMenuUser.adapter = adapter_user

                }

                adapter_admin = HomeAdapter(admin_functions, context,::toogleCamer)
                adapter_anonime = HomeAdapter(faste_or_anonime, context,::toogleCamer)
                adapter_admin.setOnItemListenerListener(ListenerAnimation)
                adapter_anonime.setOnItemListenerListener(ListenerAnimation)
                binding.RVMenuHomeAnoime.adapter = adapter_anonime


                binding.RVMenuHome.adapter = adapter_admin

                binding.SFChangeFuctions.stopShimmer()
                binding.SFChangeFuctions.visibility = View.GONE
            } else {
                //Ubo un error en a la hora de pedir lso filtros
                binding.BRlogin.isEnabled = true
                binding.LLError.visibility = View.VISIBLE
            }
        })

        CameraView.CameraActivate.observe(viewLifecycleOwner, Observer {
            if (it == CameraTypes.SCANER) {
                //                MainView.RestoreCamera()
//                val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
//                ft.setCustomAnimations(
//                    android.R.anim.fade_in,
//                    android.R.anim.fade_out,
//                    android.R.anim.slide_in_left,
//                    android.R.anim.slide_out_right
//                )
//                ft.replace(R.id.FLragmentCamera , CameraQrFastFragment());
//                ft.setReorderingAllowed(true)
//                ft.commit()

                binding.BQRScanner?.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)
//                binding.BQRScannerClient?.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)


            } else {

                binding.BQRScanner?.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
//                binding.BQRScannerClient?.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
//                if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    binding.constraintLayout.visibility = View.GONE
//                } else {
//                    binding.constraintLayout.visibility = View.INVISIBLE
//                }
                CamScannerStatus = false
            }
        })



        binding.BQRScanner.setOnClickListener {
            toogleCamer()
        }
        //Asignando

        binding.BRlogin.setOnClickListener {
            AutenticationModel.getFunctions()
            binding.LAError.playAnimation()
            binding.BRlogin.isEnabled = false
        }

//        binding.Testing.setOnClickListener {
//            findNavController().navigate(R.id.action_HomeFragmnet_to_agregateProducts3)
//        }


        binding.SVProducts.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter_admin.filter.filter(newText)
                adapter_anonime.filter.filter(newText)
                adapter_user.filter.filter(newText)
                return false
            }
        })
        return binding.root
    }

}