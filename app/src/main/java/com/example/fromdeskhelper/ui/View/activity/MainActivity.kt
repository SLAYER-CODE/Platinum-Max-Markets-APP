package com.example.fromdeskhelper.ui.View.activity


import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ActivityMainBinding
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.fragment.CameraQrFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.nav_header_main.view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public lateinit var binding: ActivityMainBinding
    var datasize:Int=0;
    lateinit var navController: NavController
//    private lateinit var database: AppDatabase;

//    override fun onResume() {
//        super.onResume()
//        binding.toolbar.title = "INICIO"
//    BIShowP}
//    override fun setActionBar(toolbar: Toolbar?) {
//        supportActionBar?.setTitle("Inicio")
//        super.setActionBar(toolbar)
//    }
@TargetApi(Build.VERSION_CODES.N)
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

    @TargetApi(Build.VERSION_CODES.N_MR1)
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

    @TargetApi(Build.VERSION_CODES.P)
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
    fun hidefabrefresh(){
        binding.appBarMain.refreshFab.hide();

    }
    fun showfabrefresh(){
        binding.appBarMain.refreshFab.show();
    }



    fun returnbinding():ActivityMainBinding{
        return binding
    }
    fun functionFabRefresh(func:()->(Unit)){
        binding.appBarMain.refreshFab.setOnClickListener {
            func()
//            CameraView.ActivateCamera()

        }
    }


    private val CameraView: CameraViewModel by viewModels()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
//        println("SE CREO INICIO")
        val drawerLayout: DrawerLayout = binding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment_content_mains)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_calls,R.id.nav_estadisticas,R.id.nav_empresa,R.id.nav_notifications
            ), drawerLayout
        )

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
//        database = AppDatabase.getDataBase(this)

        val navBuilder = NavOptions.Builder()

//        navBuilder.setEnterAnim(R.anim.slide_top).setExitAnim(R.anim.wait_anim).setPopEnterAnim(R.anim.wait_anim).setPopExitAnim(R.anim.slide_bottom)


        val menuItem: MenuItem = binding.navView.menu.getItem(0)
        menuItem.isChecked = true


        navBuilder.setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).setPopExitAnim(android.R.anim.fade_out)


        binding.appBarMain.BQRScanner?.setOnClickListener {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

            ft.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )

            CameraView.closeCameraChildren(false)
            ft.replace(R.id.FragmentCamera, CameraQrFragment());
            ft.setReorderingAllowed(true)
            ft.commit()

        }



        binding.appBarMain.fab.setOnClickListener {
//            findNavController(0).navigate(R.id.action_agregateProducts3_to_FirstFragment)
            datasize+=1

            val bundle = bundleOf("titlenumber" to datasize.toString())
            navController.navigate(R.id.agregateProducts3,bundle,navBuilder.build())

//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//            Snackbar.make(view, "Funciona El boton", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            .navigate(R.id.action_FirstFragment_to_SecondFragment)
//            val actividad = Intent(this,AddProductos::class.java)
//            startActivity(actividad)
//            findNavController(0).navigate(R.id.agregateProducts2)
//            findNavController(0).navigateUp()
//            findNavController(0).navigate(R.id.agregateProducts)

//            val intent = Intent(this, AgregateProducts::class.java)
//            startActivity(intent)

//              navController.navigate(R.id.agregateProducts);

        }

//Imagen
//        var imageUrl = FirebaseAuth.getInstance().currentUser?.photoUrl
//        if(imageUrl!=null) {
//            var ulti = imageUrl.toString()
////            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
////            StrictMode.setThreadPolicy(policy)
//            val aURL = URL(ulti)
//            val conn: URLConnection = aURL.openConnection()
//            conn.connect()
//            val isval: InputStream = conn.getInputStream()
//            val bis = BufferedInputStream(isval)
//            val bm = BitmapFactory.decodeStream(bis)
//            binding.drawerLayout.ImageUserPresentation.setImageBitmap(bm)
//            bis.close()
//            isval.close()
//        }


    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        return super.onCreateView(parent, name, context, attrs)
    }

//    override fun attachBaseContext(baseContext: Context) {
//        var newContext: Context? = baseContext
//
//        // Screen zoom is supported from API 24+
//        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
//            val resources: Resources = baseContext.resources
//            val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
//            val configuration: Configuration = resources.getConfiguration()
//            Log.v(
//                "TESTS", "attachBaseContext: currentDensityDp: " + configuration.densityDpi
//                    .toString() + " widthPixels: " + displayMetrics.widthPixels.toString() + " deviceDefault: " + DisplayMetrics.DENSITY_DEVICE_STABLE
//            )
//
//
//            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
//                // display_size_forced exists for Samsung Devices that allow user to change screen resolution
//                // (screen resolution != screen zoom.. HD, FHD, WQDH etc)
//                // This check can be omitted.. It seems this code works even if the device supports screen zoom only
//                if (Settings.Global.getString(
//                        baseContext.contentResolver,
//                        "display_size_forced"
//                    ) != null
//                ) {
//                    Log.v(
//                        "TESTS",
//                        "attachBaseContext: This device supports screen resolution changes"
//                    )
//
//                    // density is densityDp / 160
//                    val defaultDensity =
//                        DisplayMetrics.DENSITY_DEVICE_STABLE / DisplayMetrics.DENSITY_DEFAULT.toFloat()
//                    val defaultScreenWidthDp = displayMetrics.widthPixels / defaultDensity
//                    Log.v(
//                        "TESTS",
//                        "attachBaseContext: defaultDensity: $defaultDensity defaultScreenWidthDp: $defaultScreenWidthDp"
//                    )
//                    configuration.densityDpi =
//                        findDensityDpCanFitScreen(defaultScreenWidthDp.toInt())
//                } else {
//                    // If the device does not allow the user to change the screen resolution, we can
//                    // just set the default density
//                    configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
//                }
//                Log.v("TESTS", "attachBaseContext: result: " + configuration.densityDpi)
//                newContext = baseContext.createConfigurationContext(configuration)
//            }
//
//
//        }
//        super.attachBaseContext(newContext)
//    }
//
//
//    @TargetApi(VERSION_CODES.N)
//    private fun findDensityDpCanFitScreen(densityDp: Int): Int {
//        val orderedDensityDp: IntArray
//        orderedDensityDp = if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
//            ORDERED_DENSITY_DP_P
//        } else if (Build.VERSION.SDK_INT >= VERSION_CODES.N_MR1) {
//            ORDERED_DENSITY_DP_N_MR1
//        } else {
//            ORDERED_DENSITY_DP_N!!
//        }
//        var index = 0
//        while (densityDp >= orderedDensityDp[index]) {
//            index++
//        }
//        return orderedDensityDp[index]
//    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_mains)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}