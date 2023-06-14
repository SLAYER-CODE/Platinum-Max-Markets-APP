package com.example.fromdeskhelper.ui.View.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.fromdeskhelper.R
import android.R as themedefault
import com.example.fromdeskhelper.databinding.ActivityLoginMainBinding


import androidx.navigation.NavOptions
import android.util.DisplayMetrics

import android.os.Build

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fromdeskhelper.ui.View.ViewModel.LoginViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.Util.ShowConnectedViewModel
import com.example.fromdeskhelper.util.MessageSnackBar
//import com.facebook.FacebookSdk
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

//import android.R
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginMainBinding
    lateinit var navController: NavController
    private val connectedViewModel : ShowConnectedViewModel by viewModels()

//    private lateinit var database: AppDatabase;

//    override fun onResume() {
//        super.onResume()
//        binding.toolbar.title = "INICIO"
//    }
//    override fun setActionBar(toolbar: Toolbar?) {
//        supportActionBar?.setTitle("Inicio")
//        super.setActionBar(toolbar)
//    }
@TargetApi(VERSION_CODES.N)
private val ORDERED_DENSITY_DP_N: IntArray? = intArrayOf(
    DisplayMetrics.DENSITY_LOW,
    DisplayMetrics.DENSITY_MEDIUM,
    DisplayMetrics.DENSITY_TV,
    DisplayMetrics.DENSITY_HIGH,
    DisplayMetrics.DENSITY_280,
    DisplayMetrics.DENSITY_XHIGH,
    DisplayMetrics.DENSITY_360,
    DisplayMetrics.DENSITY_400,
    DisplayMetrics.DENSITY_420,
    DisplayMetrics.DENSITY_XXHIGH,
    DisplayMetrics.DENSITY_560,
    DisplayMetrics.DENSITY_XXXHIGH
)

    @TargetApi(VERSION_CODES.N_MR1)
    private val ORDERED_DENSITY_DP_N_MR1 = intArrayOf(
        DisplayMetrics.DENSITY_LOW,
        DisplayMetrics.DENSITY_MEDIUM,
        DisplayMetrics.DENSITY_TV,
        DisplayMetrics.DENSITY_HIGH,
        DisplayMetrics.DENSITY_260,
        DisplayMetrics.DENSITY_280,
        DisplayMetrics.DENSITY_XHIGH,
        DisplayMetrics.DENSITY_340,
        DisplayMetrics.DENSITY_360,
        DisplayMetrics.DENSITY_400,
        DisplayMetrics.DENSITY_420,
        DisplayMetrics.DENSITY_XXHIGH,
        DisplayMetrics.DENSITY_560,
        DisplayMetrics.DENSITY_XXXHIGH
    )

    @TargetApi(VERSION_CODES.P)
    private val ORDERED_DENSITY_DP_P = intArrayOf(
        DisplayMetrics.DENSITY_LOW,
        DisplayMetrics.DENSITY_MEDIUM,
        DisplayMetrics.DENSITY_TV,
        DisplayMetrics.DENSITY_HIGH,
        DisplayMetrics.DENSITY_260,
        DisplayMetrics.DENSITY_280,
        DisplayMetrics.DENSITY_XHIGH,
        DisplayMetrics.DENSITY_340,
        DisplayMetrics.DENSITY_360,
        DisplayMetrics.DENSITY_400,
        DisplayMetrics.DENSITY_420,
        DisplayMetrics.DENSITY_440,
        DisplayMetrics.DENSITY_XXHIGH,
        DisplayMetrics.DENSITY_560,
        DisplayMetrics.DENSITY_XXXHIGH
    )

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Agrandar la pantalla Modo inmersivo


//        setTheme(R .style.Theme_NoTitleBar_Fullscreen);
//        setTheme(themedefault.style.Theme_NoTitleBar_Fullscreen)
//        setTheme(themedefault.style.Theme_Material_Light_NoActionBar_Fullscreen)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
//        binding.toolbar.setBackgroundColor()
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var reconect = Snackbar.make(
            binding.containerloginroot, "Iniciar sin conexion (Modo de uso en dispositivo)",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Ir", object :View.OnClickListener{
            override fun onClick(p0: View?) {

            }
        })

        connectedViewModel(baseContext)
        connectedViewModel.connected.observe(this, Observer {
            if(it){
                reconect.dismiss()
            }else{
                reconect.show()
            }
        })

//        navController = findNavController(R.id.nav_host_fragment_content_login)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        blurbackground(binding.ToolBarBlurLogin)
//        database = AppDatabase.getDataBase(this)

        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.slide_top).setExitAnim(R.anim.wait_anim)
            .setPopEnterAnim(R.anim.wait_anim).setPopExitAnim(R.anim.slide_bottom)
    }

    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View =  window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }
    override fun attachBaseContext(baseContext: Context) {
        var newContext: Context? = baseContext

        // Screen zoom is supported from API 24+
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            val resources: Resources = baseContext.resources
            val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
            val configuration: Configuration = resources.getConfiguration()
            Log.v(
                "TESTS", "attachBaseContext: currentDensityDp: " + configuration.densityDpi
                    .toString() + " widthPixels: " + displayMetrics.widthPixels.toString() + " deviceDefault: " + DisplayMetrics.DENSITY_DEVICE_STABLE
            )
            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                // display_size_forced exists for Samsung Devices that allow user to change screen resolution
                // (screen resolution != screen zoom.. HD, FHD, WQDH etc)
                // This check can be omitted.. It seems this code works even if the device supports screen zoom only
                if (Settings.Global.getString(
                        baseContext.contentResolver,
                        "display_size_forced"
                    ) != null
                ) {
                    Log.v(
                        "TESTS",
                        "attachBaseContext: This device supports screen resolution changes"
                    )

                    // density is densityDp / 160
                    val defaultDensity =
                        DisplayMetrics.DENSITY_DEVICE_STABLE / DisplayMetrics.DENSITY_DEFAULT.toFloat()
                    val defaultScreenWidthDp = displayMetrics.widthPixels / defaultDensity
                    Log.v(
                        "TESTS",
                        "attachBaseContext: defaultDensity: $defaultDensity defaultScreenWidthDp: $defaultScreenWidthDp"
                    )
                    configuration.densityDpi =
                        findDensityDpCanFitScreen(defaultScreenWidthDp.toInt())
                } else {
                    // If the device does not allow the user to change the screen resolution, we can
                    // just set the default density
                    configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
                }
                Log.v("TESTS", "attachBaseContext: result: " + configuration.densityDpi)
                newContext = baseContext.createConfigurationContext(configuration)
            }
        }
        super.attachBaseContext(newContext)
    }


    @TargetApi(VERSION_CODES.N)
    private fun findDensityDpCanFitScreen(densityDp: Int): Int {
        val orderedDensityDp: IntArray
        orderedDensityDp = if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
            ORDERED_DENSITY_DP_P
        } else if (Build.VERSION.SDK_INT >= VERSION_CODES.N_MR1) {
            ORDERED_DENSITY_DP_N_MR1
        } else {
            ORDERED_DENSITY_DP_N!!
        }
        var index = 0
        while (densityDp >= orderedDensityDp[index]) {
            index++
        }
        return orderedDensityDp[index]
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

//        menuInflater.inflate(R.menu.menu_initial, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
//            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}