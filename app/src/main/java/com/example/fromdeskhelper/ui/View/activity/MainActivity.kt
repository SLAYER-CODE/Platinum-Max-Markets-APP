package com.example.fromdeskhelper.ui.View.activity


import Data.Producto
import Data.listInventarioProductos
import android.R.attr.data
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.RecyclerView
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.model.Types.CameraTypes
import com.example.fromdeskhelper.databinding.ActivityMainBinding
import com.example.fromdeskhelper.domain.WifiDirectBroadcastReceived
import com.example.fromdeskhelper.io.ClientServerP2P
import com.example.fromdeskhelper.io.Receive.SendReceive
import com.example.fromdeskhelper.io.ServerClassP2P
import com.example.fromdeskhelper.ui.View.ViewModel.CameraViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ProductsP2PViewMode
import com.example.fromdeskhelper.ui.View.ViewModel.MainActiviyViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.ShowMainViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.WifiVIewModel
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.ui.View.fragment.CameraQrFragment
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.listener.TouchListenerResize
import com.example.fromdeskhelper.util.listener.TouchlistenerMove
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.net.InetAddress
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public lateinit var binding: ActivityMainBinding
    var datasize:Int=0;
    lateinit var navController: NavController
    private val wifiViewModel: WifiVIewModel by viewModels()
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
//    fun functionFabRefresh(func:()->(Unit)){
//        binding.appBarMain.refreshFab.setOnClickListener {
//            func()
//        }
//    }

    fun getActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) return context
        return if (context is ContextWrapper) getActivity(context.baseContext) else null
    }
    private val CameraView: CameraViewModel by viewModels()
    private val MainView:MainActiviyViewModel by viewModels()
    public lateinit var mIntentFilter:IntentFilter;
    public lateinit var mReceiver: WifiDirectBroadcastReceived;
    public lateinit var RecolectAdapter:  RecyclerView.Adapter<P2pClientAdapter.ClientP2PHolder>;


    private lateinit var clientClass:ClientServerP2P
    private lateinit var serverClass:ServerClassP2P
    private lateinit var sendReceived:SendReceive
    private val MainModel : ShowMainViewModel by viewModels();
    private val PP2Pproducts: ProductsP2PViewMode by viewModels()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    private var CamScannerStatus: Boolean = false;

    private var ItemsSend:MutableList<listInventarioProductos> = mutableListOf()



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
//        Toast.makeText(
//            baseContext, "Segundo!",
//            Toast.LENGTH_LONG
//        ).show()

        StrictMode.setThreadPolicy(policy)

        var jander:Handler= Handler(object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                Log.i("SEUNDITEM",msg.toString())
                when(msg.what){
                    1->{
                        var readBuff:ByteArray = msg.obj as ByteArray
                        var tempMSG:String= String(readBuff,0,msg.arg1)
                        Log.i("MEESASNACBAR",tempMSG)
                        if("PROC|" in tempMSG) {
                            var producto = tempMSG.split("|")[1]
                            PP2Pproducts.AddRecived(producto)
                        }else if("CODE:" in tempMSG){
                            MessageSnackBar(findViewById(android.R.id.content), "CODE", Color.CYAN)
                        }else {
                            MessageSnackBar(findViewById(android.R.id.content), tempMSG, Color.RED)
                        }
                    }
                }
                return true
            }
        })

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
//        println("SE CREO INICIO")
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

//        binding.drawerLayout.MoveCamera.setOnTouchListener(TouchDropListenerAction())

        navController = findNavController(R.id.nav_host_fragment_content_mains)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.FirstFragment,
//                R.id.clientsRoot,
//                R.id.analiticRoot,
//                R.id.empleadosRoot,
//                R.id.notificationsRoot,
//                R.id.showproductsClient,
//                R.id.locationClient,
//                R.id.ordersClient,
//                R.id.favoriteClient,
//                R.id.paymentsClient
//            ), drawerLayout
//        )
        appBarConfiguration= AppBarConfiguration.Builder(R.id.FirstFragment,
            R.id.clientsRoot,
            R.id.analiticRoot,
            R.id.empleadosRoot,
            R.id.notificationsRoot,
            R.id.showproductsClient,
            R.id.locationClient,
            R.id.ordersClient,
            R.id.favoriteClient,
            R.id.paymentsClient).setDrawerLayout(binding.drawerLayout).build()

//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        binding.drawerLayout.nav_view.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
        NavigationUI.setupWithNavController(binding.drawerLayout.nav_view,navController)
//        navView.setupWithNavController(navController)


//        database = AppDatabase.getDataBase(this)
        val navBuilder = NavOptions.Builder()
//        navBuilder.setEnterAnim(R.anim.slide_top).setExitAnim(R.anim.wait_anim).setPopEnterAnim(R.anim.wait_anim).setPopExitAnim(R.anim.slide_bottom)
        navBuilder.setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).setPopExitAnim(android.R.anim.fade_out)


        //SE COMENSO LA CONECTIVIDAD P2P
        var perrListener:WifiP2pManager.PeerListListener= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.i("MAINP2Plistener","Se llamo al peer de android")
            object:WifiP2pManager.P2pStateListener,
                WifiP2pManager.PeerListListener {
                override fun onP2pStateAvailable(p0: Int) {
                    Log.i("MAINP2Plistener","Se encontro estados Validos")
                }
                override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
                    Log.i("MAINP2Plistener","Se encontraron Listas")
                    if(p0!=null) {
//                        adaptador.addClientP2p(p0.deviceList ?: listOf())
//                        adaptador.addAll(p0.deviceList ?: listOf())
//                        var index = 0;
//                        for (device: WifiP2pDevice in p0?.deviceList){
//                            adaptador.addClientP2p(device)
//                            Log.i("P2PRECONECT Final",device.deviceName)
//                            index+=1;
//                        }
                        wifiViewModel.sendListP2P(p0)


//                        RecolectAdapter = P2pClientAdapter(p0.deviceList?: mutableListOf())
//                        Log.i("P2PRECONECT Conteo",index.toString())
                    }else{
                        Log.i("MAINP2Plistener","No se encontraron dispositivos")
                    }
                }
            }
        } else {
            Log.i("P2PRECONECT","Se llamo AL SEGUNDO")
            object:WifiP2pManager.PeerListListener {
                override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
                    Log.i("MAINP2Plistener","se encotraron dispositivos Validos [2 Call]")
                    if(p0!=null) {
                        var index = 0;
//                        for (device: WifiP2pDevice in p0?.deviceList){
//                            Log.i("P2PRECONECT Final",device.deviceName)
//
//                            adaptador.addClientP2p(device)
//                            index+=1;
//                        }
                        wifiViewModel.sendListP2P(p0)
//                        RecolectAdapter = P2pClientAdapter(p0.deviceList?: mutableListOf())
//                        Log.i("P2PRECONECT Conteo",index.toString())
                    }else{
                        Log.i("MAINP2Plistener","No se encontraron dispositivos [2 Call]")
                    }
                }
            }
        }
        MainModel.ItemsRouterTransmited.observe(this, Observer {
            Log.d("ENVIANDO SETEANDO LOS DATOS ",it.toString())
            ItemsSend=it
        })
        var listenerConection:WifiP2pManager.ConnectionInfoListener = object :WifiP2pManager.ConnectionInfoListener{
            override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
                Log.d("ConnectPertoPerr",p0?.isGroupOwner.toString())
                Log.d("ConnectPertoPerr",p0?.groupOwnerAddress.toString())
                Log.d("ConnectPertoPerr",p0?.groupFormed.toString())
                val gropOwnerAddres :InetAddress? = p0?.groupOwnerAddress
                if(p0!!.groupFormed && p0!!.isGroupOwner){
//                    MessageSnackBar(applicationContext as View,"SE CONECTO",Color.GREEN)
                    Log.i("MAINP2PConection","SECONECTO AL OTRO DISPOSITIVO")
//                    Toast.makeText(applicationContext,"SE CONECTO",Toast.LENGTH_LONG)
                    serverClass= ServerClassP2P(handler =jander,wifiViewModel,p0.groupOwnerAddress.hostAddress,MainModel.RequestItemFunc())
                    serverClass.start()
//                    sendReceived=serverClass.returnRecived()
//                    wifiViewModel.addDeviceConected(p0.groupOwnerAddress.hostAddress,sendReceived)
                }else if (p0!!.groupFormed){
//                    MessageSnackBar(getApplicationContext() as View,"NO SE CONECTO",Color.RED)
                    MessageSnackBar(findViewById(android.R.id.content), "SE Conecto!", Color.GREEN)
                    Log.i("MAINP2PConnection","ADDRES:"+gropOwnerAddres.toString())
//                    Log.i("MAINP2PConnection","GROPOWNER:"+isGroupOwner.toString())
//                    Log.i("MAINP2PConnection","GropFORMED"+groupFormed.toString())
                    clientClass= ClientServerP2P(gropOwnerAddres, handler = jander,wifiViewModel,p0.groupOwnerAddress.toString())
                    clientClass.start()
//                    sendReceived=clientClass.returnRecived()
                }
            }
        }
        mIntentFilter= IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        mReceiver=wifiViewModel.getregisterBroadcast(perrListener,listenerConection)

        //SE TERMINO LA CONECTIVADDD
        CameraView.CameraActivate.observe(this, Observer {
            if(it==CameraTypes.SCANER){
                binding.appBarMain.BQRScanner.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)
                binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)

                binding.appBarMain.constraintLayout.visibility=View.VISIBLE
            }else{
                binding.appBarMain.BQRScanner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
                binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
                if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.appBarMain.constraintLayout.visibility = View.GONE
                }else {
                    binding.appBarMain.constraintLayout.visibility = View.INVISIBLE
                }
                CamScannerStatus=false
            }
        })
        binding.drawerLayout.MoveCamera.setOnTouchListener(
            TouchlistenerMove(binding.drawerLayout.constraintLayout,MainView::SavePointMoveCamera)
        )
        binding.drawerLayout.ResizeCamera.setOnTouchListener(
            TouchListenerResize(binding.drawerLayout.constraintLayout,MainView::SavePointScaleCamera)
        )

        MainView.OpenCamera.observe(this, Observer { Values->

            if (this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {

                if (!(Values.movex == -1f && Values.movey == -1f)) {
                    binding.drawerLayout.constraintLayout.x = Values.movex
                    binding.drawerLayout.constraintLayout.y = Values.movey
                }
                binding.drawerLayout.constraintLayout.scaleX = Values.scalex
                binding.drawerLayout.constraintLayout.scaleY = Values.scaley
            }
        })

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                MessageSnackBar(findViewById(android.R.id.content), "WIFI ACTIVADO", Color.MAGENTA)
            }else{
                if(wifiViewModel.VerifyWifi()){
                    MessageSnackBar(findViewById(android.R.id.content), "WIFI ACTIVADO", Color.MAGENTA)
                    Log.i("WIFIRESFINAL","SE ACTIVO")
                }else{
                    MessageSnackBar(findViewById(android.R.id.content), "WIFI DESACTIVADO", Color.RED)
                    Log.i("WIFIRESFINAL","SE DESACTIVO")
                }
            }
        }

        wifiViewModel.WifiActivate.observe(this, Observer { t ->
            if(!t) {
//                val panelIntent = Intent(Settings.Panel.ACTION_WIFI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(panelIntent);
                resultLauncher.launch(Intent(Settings.Panel.ACTION_WIFI))
            }
            Log.i("WIFIRESFINAL",t.toString())
            if(wifiViewModel.VerifyWifi()){
                Log.i("WIFIRESFINAL","SE ACTIVO")
                wifiViewModel.DiscoveryActivate(this)
            }
        })
        if(wifiViewModel.VerifyWifi()){
            wifiViewModel.DiscoveryActivate(this)
        }

        wifiViewModel.WifiActivateBroadcast.observe(this, Observer {
            Log.i("P2PDesing","Se decidio" +android.os.Build.VERSION.SDK_INT)
            if(28 != android.os.Build.VERSION.SDK_INT) {
                binding.appBarMain.toolbarParent.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.background_bar
                    )
                );

            }
            val ViewDrawable = DrawableCompat.wrap(binding.appBarMain.toolbarParent.background);
            if(it) {
                DrawableCompat.setTint(ViewDrawable,Color.GREEN)
                binding.appBarMain.toolbarParent.background = ViewDrawable
            }else{
                DrawableCompat.setTint(ViewDrawable,Color.RED)
                binding.appBarMain.toolbarParent.background = ViewDrawable
            }
        })


        var Listener=View.OnClickListener {
            if(!CamScannerStatus) {
                MainView.RestoreCamera()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                CameraView.CloseInFragment(false)
                CameraView.CamaraStatus(CameraTypes.SCANER, true)
                ft.replace(R.id.FragmentCamera, CameraQrFragment());
                ft.setReorderingAllowed(true)
                ft.commit()
                CamScannerStatus=true;
            }else{
                CameraView.CloseInFragment(true)
                CameraView.CamaraStatus(CameraTypes.NULL, true)
                CamScannerStatus=false;
            }
        }
        binding.appBarMain.BQRScanner.setOnClickListener(Listener)
        binding.appBarMain.BQRScannerClient.setOnClickListener(Listener)



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



    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver,mIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
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