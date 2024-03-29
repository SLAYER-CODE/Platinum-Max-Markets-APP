package com.example.fromdeskhelper.ui.View.activity


import Data.listInventarioProductos
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
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
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
import com.example.fromdeskhelper.ui.View.ViewModel.*
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ProductsP2PViewMode
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsServerViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsStoreViewModel
import com.example.fromdeskhelper.ui.View.adapter.LocalAdapter
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import com.example.fromdeskhelper.ui.View.fragment.CameraQrFragment
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.listener.TouchListenerResize
import com.example.fromdeskhelper.util.listener.TouchlistenerMove
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.net.InetAddress


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public lateinit var binding: ActivityMainBinding
    var datasize: Int = 0;
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


    fun setRefreshMain(){
        binding.appBarMain.refreshFab.setOnClickListener {
            Log.i("REFRESHFAB","SELLAMO")
            LocalModel.GetAllProducts()
            ServerModel.GetProductsAllPreview()
            StoreSendViewModel.sendAdapterItems(adapterProduct)
        }
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
    private val MainView: MainActiviyViewModel by viewModels()
    private val UtilsView: UitlsMainShowViewModel by viewModels()
    private val UtilsMainModel: UtilsShowMainViewModels by viewModels()

    public lateinit var mIntentFilter: IntentFilter;
    public lateinit var mReceiver: WifiDirectBroadcastReceived;
    public lateinit var RecolectAdapter: RecyclerView.Adapter<P2pClientAdapter.ClientP2PHolder>;


    private lateinit var clientClass: ClientServerP2P
    private lateinit var serverClass: ServerClassP2P
    private lateinit var sendReceived: SendReceive

    private val MainModel: ShowMainViewModel by viewModels();
    private val PP2Pproducts: ProductsP2PViewMode by viewModels()
    private val ServerModel: ShowServerViewModel by viewModels()
    private val LocalModel: ShowLocalViewModel by viewModels()

    private val ServerSendViewModel: SendProductsServerViewModel by viewModels()
    private val LocalSendViewModel: SendProductsLocalViewModel by viewModels()
    private val StoreSendViewModel: SendProductsStoreViewModel by viewModels()


    private var adapterProduct: ProductoAdapter = ProductoAdapter(listOf(), null, 1);
    private var adapterSever: ServerAdapter = ServerAdapter(listOf(), 1);
    private var adapterLocal: LocalAdapter = LocalAdapter(listOf(), 1)


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    private var CamScannerStatus: Boolean = false;

    private var ItemsSend: MutableList<listInventarioProductos> = mutableListOf()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
//        Toast.makeText(
//            baseContext, "Segundo!",
//            Toast.LENGTH_LONG
//        ).show()

        StrictMode.setThreadPolicy(policy)

        var jander: Handler = Handler(object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                Log.i("SEUNDITEM", msg.toString())
                when (msg.what) {
                    1 -> {
                        var readBuff: ByteArray = msg.obj as ByteArray
                        var tempMSG: String = String(readBuff, 0, msg.arg1)
                        Log.i("MEESASNACBAR", tempMSG)
                        if ("PROC|" in tempMSG) {
                            var producto = tempMSG.split("|")[1]
                            PP2Pproducts.AddRecived(producto)
                        } else if ("CODE:" in tempMSG) {
                            MessageSnackBar(findViewById(android.R.id.content), "CODE", Color.CYAN)
                        } else {
                            MessageSnackBar(findViewById(android.R.id.content), tempMSG, Color.RED)
                        }
                    }
                }
                return true
            }
        })

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRefreshMain()
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
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.FirstFragment,
            R.id.clientsRoot,
            R.id.analiticRoot,
            R.id.empleadosRoot,
            R.id.notificationsRoot,
            R.id.showproductsClient,
            R.id.locationClient,
            R.id.ordersClient,
            R.id.favoriteClient,
            R.id.paymentsClient
        ).setDrawerLayout(binding.drawerLayout).build()

//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        binding.drawerLayout.nav_view.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.drawerLayout.nav_view, navController)
//        navView.setupWithNavController(navController)


//        database = AppDatabase.getDataBase(this)


        //SE COMENSO LA CONECTIVIDAD P2P
        var perrListener: WifiP2pManager.PeerListListener =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.i("MAINP2Plistener", "Se llamo al peer de android")
                object : WifiP2pManager.P2pStateListener,
                    WifiP2pManager.PeerListListener {
                    override fun onP2pStateAvailable(p0: Int) {
                        Log.i("MAINP2Plistener", "Se encontro estados Validos")
                    }

                    override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
                        Log.i("MAINP2Plistener", "Se encontraron Listas")
                        if (p0 != null) {
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
                        } else {
                            Log.i("MAINP2Plistener", "No se encontraron dispositivos")
                        }
                    }
                }
            } else {
                Log.i("P2PRECONECT", "Se llamo AL SEGUNDO")
                object : WifiP2pManager.PeerListListener {
                    override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
                        Log.i("MAINP2Plistener", "se encotraron dispositivos Validos [2 Call]")
                        if (p0 != null) {
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
                        } else {
                            Log.i("MAINP2Plistener", "No se encontraron dispositivos [2 Call]")
                        }
                    }
                }
            }
        MainModel.GetImageResource()
        MainModel.ImageReturn.observe(this, Observer {
            Log.i("segundo", it.toString())
            if (it != "null" || it != null) {
//                val transformation: Transformation = RoundedCornersTransformation(radius, margin)

                Picasso.get()
                    .load(it).fit()
                    .transform(CropCircleTransformation())
//                    .into(binding.drawerLayout.nav_view.getHeaderView(0).findViewById(R.id.ImageUserPresentation) as ImageView)
                    .into(binding.drawerLayout.ImageUserPresentation)
            }
        })


        MainModel.ItemsRouterTransmited.observe(this, Observer {
            Log.d("ENVIANDO SETEANDO LOS DATOS ", it.toString())
            ItemsSend = it
        })
        var listenerConection: WifiP2pManager.ConnectionInfoListener =
            object : WifiP2pManager.ConnectionInfoListener {
                override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
                    Log.d("ConnectPertoPerr", p0?.isGroupOwner.toString())
                    Log.d("ConnectPertoPerr", p0?.groupOwnerAddress.toString())
                    Log.d("ConnectPertoPerr", p0?.groupFormed.toString())
                    val gropOwnerAddres: InetAddress? = p0?.groupOwnerAddress
                    if (p0!!.groupFormed && p0!!.isGroupOwner) {
//                    MessageSnackBar(applicationContext as View,"SE CONECTO",Color.GREEN)
                        Log.i("MAINP2PConection", "SECONECTO AL OTRO DISPOSITIVO")
//                    Toast.makeText(applicationContext,"SE CONECTO",Toast.LENGTH_LONG)
                        serverClass = ServerClassP2P(
                            handler = jander,
                            wifiViewModel,
                            p0.groupOwnerAddress.hostAddress,
                            MainModel.RequestItemFunc()
                        )
                        serverClass.start()
//                    sendReceived=serverClass.returnRecived()
//                    wifiViewModel.addDeviceConected(p0.groupOwnerAddress.hostAddress,sendReceived)
                    } else if (p0!!.groupFormed) {
//                    MessageSnackBar(getApplicationContext() as View,"NO SE CONECTO",Color.RED)
                        MessageSnackBar(
                            findViewById(android.R.id.content),
                            "SE Conecto!",
                            Color.GREEN
                        )
                        Log.i("MAINP2PConnection", "ADDRES:" + gropOwnerAddres.toString())
//                    Log.i("MAINP2PConnection","GROPOWNER:"+isGroupOwner.toString())
//                    Log.i("MAINP2PConnection","GropFORMED"+groupFormed.toString())
                        clientClass = ClientServerP2P(
                            gropOwnerAddres,
                            handler = jander,
                            wifiViewModel,
                            p0.groupOwnerAddress.toString()
                        )
                        clientClass.start()
//                    sendReceived=clientClass.returnRecived()
                    }
                }
            }
        mIntentFilter = IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        mReceiver = wifiViewModel.getregisterBroadcast(perrListener, listenerConection)

        //SE TERMINO LA CONECTIVADDD
        CameraView.CameraActivate.observe(this, Observer {
            if (it == CameraTypes.SCANER) {
                binding.appBarMain.BQRScanner.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)
                binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_close_24_showproduct)

                binding.appBarMain.constraintLayout.visibility = View.VISIBLE
            } else {
                binding.appBarMain.BQRScanner.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
                binding.appBarMain.BQRScannerClient.setBackgroundResource(R.drawable.ic_baseline_qr_code_scanner_showproduct)
                if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.appBarMain.constraintLayout.visibility = View.GONE
                } else {
                    binding.appBarMain.constraintLayout.visibility = View.INVISIBLE
                }
                CamScannerStatus = false
            }
        })




        CameraView.QRBindig.observe(this, Observer {
            Log.i("QRRESIVED", it)
            binding.appBarMain.SVProducts.setQuery(it, false)
            binding.appBarMain.SVProducts.clearFocus()
        })
        binding.drawerLayout.MoveCamera.setOnTouchListener(
            TouchlistenerMove(binding.drawerLayout.constraintLayout, MainView::SavePointMoveCamera)
        )
        binding.drawerLayout.ResizeCamera.setOnTouchListener(
            TouchListenerResize(
                binding.drawerLayout.constraintLayout,
                MainView::SavePointScaleCamera
            )
        )

        MainView.OpenCamera.observe(this, Observer { Values ->

            if (this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {

                if (!(Values.movex == -1f && Values.movey == -1f)) {
                    binding.drawerLayout.constraintLayout.x = Values.movex
                    binding.drawerLayout.constraintLayout.y = Values.movey
                }
                binding.drawerLayout.constraintLayout.scaleX = Values.scalex
                binding.drawerLayout.constraintLayout.scaleY = Values.scaley
            }
        })

        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    MessageSnackBar(
                        findViewById(android.R.id.content),
                        "WIFI ACTIVADO",
                        Color.MAGENTA
                    )
                } else {
                    if (wifiViewModel.VerifyWifi()) {
                        MessageSnackBar(
                            findViewById(android.R.id.content),
                            "WIFI ACTIVADO",
                            Color.MAGENTA
                        )
                        Log.i("WIFIRESFINAL", "SE ACTIVO")
                    } else {
                        MessageSnackBar(
                            findViewById(android.R.id.content),
                            "WIFI DESACTIVADO",
                            Color.RED
                        )
                        Log.i("WIFIRESFINAL", "SE DESACTIVO")
                    }
                }
            }

        wifiViewModel.WifiActivate.observe(this, Observer { t ->
            if (!t) {
//                val panelIntent = Intent(Settings.Panel.ACTION_WIFI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(panelIntent);
                resultLauncher.launch(Intent(Settings.Panel.ACTION_WIFI))
            }
            Log.i("WIFIRESFINAL", t.toString())
            if (wifiViewModel.VerifyWifi()) {
                Log.i("WIFIRESFINAL", "SE ACTIVO")
                wifiViewModel.DiscoveryActivate(this)
            }
        })
        if (wifiViewModel.VerifyWifi()) {
            wifiViewModel.DiscoveryActivate(this)
        }

        wifiViewModel.WifiActivateBroadcast.observe(this, Observer {
            Log.i("P2PDesing", "Se decidio" + android.os.Build.VERSION.SDK_INT)
            if (28 != android.os.Build.VERSION.SDK_INT) {
                binding.appBarMain.toolbar.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.background_bar
                    )
                );
            }

            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val ViewDrawable = DrawableCompat.wrap(binding.appBarMain.toolbar.background);
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.GREEN)
                        binding.appBarMain.toolbar.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.RED)
                        binding.appBarMain.toolbar.background = ViewDrawable
                    }
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_NO -> {
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.CYAN)
                        binding.appBarMain.toolbar.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.GRAY)
                        binding.appBarMain.toolbar.background = ViewDrawable
                    }
                } // Night mode is active, we're using dark theme
            }
        })


        var Listener = View.OnClickListener {
            if (!CamScannerStatus) {
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
                CamScannerStatus = true;
            } else {
                CameraView.CloseInFragment(true)
                CameraView.CamaraStatus(CameraTypes.NULL, true)
                CamScannerStatus = false;
            }
        }
        binding.appBarMain.BQRScanner.setOnClickListener(Listener)
        binding.appBarMain.BQRScannerClient.setOnClickListener(Listener)


        blurbackground(binding.BlurVIewDrawable)


        val navigationMenuView = nav_view.getChildAt(0)
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false)
        }

        binding.drawerLayout.setScrimColor(Color.argb(0.15f, 0f, 0f, 0f))
        val actionBarDrawerToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = (drawerView.width * (slideOffset / 5f))
                    app_bar_main.setTranslationX(slideX)
                    app_bar_main.setScaleX(1 - (slideOffset / 7f));
                    app_bar_main.setScaleY(1 - (slideOffset / 7f));
                }
            }



        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        binding.appBarMain.fab.setOnClickListener {
//            findNavController(0).navigate(R.id.action_agregateProducts3_to_FirstFragment)
            datasize += 1

            val bundle = bundleOf("titlenumber" to datasize.toString())
            val navBuilder = NavOptions.Builder()
            navBuilder.setEnterAnim(R.anim.slide_in_right).setExitAnim(android.R.anim.fade_out)
                .setPopExitAnim(R.anim.slide_out_left)
            navController.navigate(R.id.agregateProducts3, bundle, navBuilder.build())

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

        binding.appBarMain.SVProducts.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterLocal.filter.filter(newText)
                adapterProduct.filter.filter(newText)
                adapterSever.filter.filter(newText)
                return false
            }
        })




        MainModel.AllProducts().observe(this, Observer {
            Log.i("Segundo", "CLaro que si")
            Log.i("Segundo", binding.appBarMain.SVProducts.query.toString())

            adapterProduct = ProductoAdapter(it, MainModel, 1, UtilsMainModel)
            adapterProduct.filter.filter(binding.appBarMain.SVProducts.query,object :Filter.FilterListener{
                override fun onFilterComplete(count: Int) {
                    StoreSendViewModel.sendAdapterItems(adapterProduct)
                }
            })
//            binding.LVMylist.adapter = adaptador
//            comprobateList(it.size)
        })


        //LocalList
        LocalModel.GetAllProducts()
        LocalModel.AdapterSend.observe(this, Observer {
            adapterLocal = LocalAdapter(it.reversed(), 1, UtilsMainModel)
            adapterLocal.filter.filter(binding.appBarMain.SVProducts.query,object :Filter.FilterListener{
                override fun onFilterComplete(count: Int) {
                    LocalSendViewModel.sendAdapterItems(adapterLocal)
                }
            })
//            binding.LVMylist.adapter=adapter
//            comprobateList(it.size)
//            MessageSnackBar(view,"Se actualizo!",Color.CYAN)
//            binding.LVMylist.startLayoutAnimation()
        })
        //ServerList
        ServerModel.GetProductsAllPreview()

        ServerModel.ProductsAllPreview.observe(this, Observer {
            adapterSever = ServerAdapter(it?.productos!!.reversed(), 1, UtilsMainModel)
            adapterSever.filter.filter(binding.appBarMain.SVProducts.query,object :Filter.FilterListener{
                override fun onFilterComplete(count: Int) {
                    ServerSendViewModel.sendAdapterItems(adapterSever)
                }
            })
//            binding.LVMylist.adapter=adaptador
//            comprobateList(it?.productos.size)
//            binding.LVMylist.startLayoutAnimation()
        })


//
//        UtilsViewProduct.DesingItemListVIew.observe(viewLifecycleOwner, Observer {
////            ListViewDesingLayout
//            if(it==1){
//                binding.LVMylist.layoutManager=
//                    LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)
//                adaptador.setItem(3)
//                binding.LVMylist.adapter = adaptador
//            }else if(it==2){
//                binding.LVMylist.layoutManager=GridLayoutManager(baseActivity,2)
//                adaptador.setItem(4)
//                binding.LVMylist.adapter = adaptador
////                NormalDesingLayout
//            }else if(it==0){
//                binding.LVMylist.layoutManager=
//                    LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL,false)
//                adaptador.setItem(1)
//                binding.LVMylist.adapter = adaptador
//            }else if(it==3){
//                binding.LVMylist.layoutManager=GridLayoutManager(baseActivity,2)
//                adaptador.setItem(3)
//                binding.LVMylist.adapter = adaptador
//            }
//        })
    }


    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View = window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
//        val rootView = drawer_layout
        val windowBackground = decorView.background
        view.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(false)
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(mReceiver, mIntentFilter)
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
        when (item.itemId) {
            R.id.DetailsChecked -> {
//                UtilsView.NormalDesingLayout()
                adapterLocal.setItem(1)
                adapterProduct.setItem(1)
                adapterSever.setItem(1)
                UtilsView.NormalDesingLayout()
            }
            R.id.ListChecked -> {
                adapterLocal.setItem(3)
                adapterProduct.setItem(3)
                adapterSever.setItem(3)
//                UtilsView.RequestLayout()
                UtilsView.ListViewDesingLayout()
            }
            R.id.GridDetalles -> {
                adapterLocal.setItem(4)
                adapterProduct.setItem(4)
                adapterSever.setItem(4)
                UtilsView.GirdLayout()
            }
            R.id.GridDetallesListView -> {
                adapterLocal.setItem(3)
                adapterProduct.setItem(3)
                adapterSever.setItem(3)
                UtilsView.GirdLayoutListView()
            }
        }
//        adapterProduct.notifyDataSetChanged()
//        adapterLocal.notifyDataSetChanged()
//        adapterSever.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_mains)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}