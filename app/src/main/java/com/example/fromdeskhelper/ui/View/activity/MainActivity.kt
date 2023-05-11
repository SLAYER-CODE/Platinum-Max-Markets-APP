package com.example.fromdeskhelper.ui.View.activity


import Data.ClientList
import Data.ClientListGet
import Data.listInventarioProductos
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
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
import com.example.fromdeskhelper.databinding.NavHeaderMainBinding
import com.example.fromdeskhelper.domain.WifiDirectBroadcastReceived
import com.example.fromdeskhelper.io.ClientServerP2P
import com.example.fromdeskhelper.io.Receive.SendReceive
import com.example.fromdeskhelper.io.ServerClassP2P
import com.example.fromdeskhelper.ui.View.ViewModel.*
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ProductsP2PViewMode
import com.example.fromdeskhelper.ui.View.ViewModel.Client.ShowMainClientViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsLocalViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsServerViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SendItems.SendProductsStoreViewModel
import com.example.fromdeskhelper.ui.View.adapter.ClientAdapter
import com.example.fromdeskhelper.ui.View.adapter.LocalAdapter
import com.example.fromdeskhelper.ui.View.adapter.P2pClientAdapter
import com.example.fromdeskhelper.ui.View.adapter.ProductoAdapter
import com.example.fromdeskhelper.ui.View.adapter.ServerAdapter
import com.example.fromdeskhelper.ui.View.fragment.CameraQrFragment
import com.example.fromdeskhelper.ui.View.fragment.Root.Clients.ClientItemShowFragment
import com.example.fromdeskhelper.ui.View.fragment.ShowProducts
import com.example.fromdeskhelper.util.MessageSnackBar
import com.example.fromdeskhelper.util.listener.RecyclerViewItemClickListener
import com.example.fromdeskhelper.util.listener.TouchListenerResize
import com.example.fromdeskhelper.util.listener.TouchlistenerMove
import com.google.firebase.auth.FirebaseAuth
import com.leochuan.CenterSnapHelper
import com.leochuan.CircleLayoutManager
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
import java.util.Date


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


    fun setRefreshMain() {
        binding.appBarMain.refreshFab.setOnClickListener {
            MessageSnackBar(
                binding.appBarMain.cordinatorRoot,
                "Se refrezcaron todas las vistas",
                Color.LTGRAY
            )
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

    private val MainView: MainActiviyViewModel by viewModels()
    private val UtilsView: UitlsMainShowViewModel by viewModels()
    private val UtilsMainModel: UtilsShowMainViewModels by viewModels()

    public lateinit var mIntentFilter: IntentFilter;
    public lateinit var mReceiver: WifiDirectBroadcastReceived;
    public lateinit var RecolectAdapter: RecyclerView.Adapter<P2pClientAdapter.ClientP2PHolder>;


    private lateinit var clientClass: ClientServerP2P
    private lateinit var serverClass: ServerClassP2P
    private var sendReceived: SendReceive?=null

    private val MainModel: ShowMainViewModel by viewModels();
    private val PP2Pproducts: ProductsP2PViewMode by viewModels()
    private val ServerModel: ShowServerViewModel by viewModels()
    private val LocalModel: ShowLocalViewModel by viewModels()

    private val ServerSendViewModel: SendProductsServerViewModel by viewModels()
    private val LocalSendViewModel: SendProductsLocalViewModel by viewModels()
    private val StoreSendViewModel: SendProductsStoreViewModel by viewModels()
    private val ShowMainClientModel : ShowMainClientViewModel by viewModels()

    private var adapterProduct: ProductoAdapter = ProductoAdapter(listOf(), null, 1);
    private var adapterSever: ServerAdapter = ServerAdapter(listOf(), 1);
    private var adapterLocal: LocalAdapter = LocalAdapter(listOf(), 1)


    //Clientes

    private val ClienModel:ClientAddShortViewModel by viewModels()
    var clientcount:Int=1;
    private var contadorImagen = 1;

    private val ClienLocalModel: ClientLocalViewModel by viewModels()
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }


    private var ItemsSend: MutableList<listInventarioProductos> = mutableListOf()
    private var permissons:Boolean=false
    fun createAleatorieList(): ClientList {
        val NewClient = ClientList(
            fecha = Date(),
            color =Color.argb(200, (50..255).random(), (25..255).random(), (25..255).random()),
            number = clientcount
        )
        clientcount += 1
        return NewClient;
    }


    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            startScale, endScale,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 200
        v.startAnimation(anim)
    }
    val FREQ = 3f
    val DECAY = 2f

    val decayingSineWave = TimeInterpolator { input ->
        val raw = Math.sin(FREQ * input * 2 * Math.PI)
        (raw * Math.exp((-input * DECAY).toDouble())).toFloat()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val header = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        if (baseContext.resources.configuration
                .orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else if (baseContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        val policy = ThreadPolicy.Builder().permitAll().build()

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    // Precise location access granted.
                    MessageSnackBar(
                        binding.appBarMain.cordinatorRoot,
                        "Se concedio Permisos de Ubicacion",
                        Color.LTGRAY
                    )
                    permissons=true
                }
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    // Only approximate location access granted.
                    MessageSnackBar(
                        binding.appBarMain.cordinatorRoot,
                        "Se concedio Permisos de Ubicacion En la zona",
                        Color.CYAN
                    )
                }
                else -> {
                    MessageSnackBar(
                        binding.appBarMain.cordinatorRoot,
                        "No se concedieron los permisos ",
                        Color.RED
                    )
                    // No location access granted.
                }
            }
        }
        wifiViewModel.getPermissions()
        wifiViewModel.P2pPermisions.observe(this, Observer {
            if(it){
                if(!permissons) {
                    locationPermissionRequest.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        })




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
                            MessageSnackBar(binding.appBarMain.cordinatorRoot, "CODE", Color.CYAN)
                        } else if ("FACTURE|" in tempMSG ){
                            var Facture= tempMSG.split("|")[1]
                            ShowMainClientModel.AddFacture(Facture)
                        }else {
                            MessageSnackBar(binding.appBarMain.cordinatorRoot, tempMSG, Color.RED)
                        }
                    }
                }
                return true
            }
        })



        //Insertando el codigo inflado
        var adapterClient = ClientAdapter(mutableListOf<ClientListGet>())

        var radius:Int =(resources.displayMetrics.density*65f).toInt()
        var distance:Int = (resources.displayMetrics.density*65f).toInt()
        var sistem = CircleLayoutManager.Builder(baseContext)
            .setRadius(radius)
            .setAngleInterval(45)
            .setDistanceToBottom(distance)
            .setGravity(CircleLayoutManager.RIGHT).setZAlignment(6).setReverseLayout(true)
            .build()
        sistem.maxVisibleItemCount=5
        sistem.infinite=true



        binding.appBarMain.addclientitems?.layoutManager = sistem
        CenterSnapHelper().attachToRecyclerView(binding.appBarMain.addclientitems)
                //LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            //ArcClientLayout(context = this,)




        binding.appBarMain.fabClient?.setOnClickListener {
            var createClient=createAleatorieList()
            ClienModel.agregateItem(createClient)
        }

        binding.appBarMain.fabClient?.setOnLongClickListener {
            true
        }
        adapterClient= ClientAdapter(mutableListOf())
        binding.appBarMain.addclientitems?.adapter=adapterClient
        ClienModel.getItemClients().observe(this, Observer {
            adapterClient.Clients=it.reversed().toMutableList()
            adapterClient.notifyItemInserted(0)
            binding.appBarMain.addclientitems?.scrollToPosition(0);
        })
        binding.appBarMain.addclientitems?.addOnItemTouchListener(RecyclerViewItemClickListener(
            baseContext,binding.appBarMain.addclientitems!!,object : ShowProducts.ClickListener {
                override fun onClick(view: View?, position: Int) {
                    //val ViewDrawable=DrawableCompat.wrap(binding.TLMain.background);
                    //DrawableCompat.setTint(ViewDrawable,client.color)
                    var client= adapterClient.Clients[position]

                    Log.i("SETOCO",position.toString())

                    for (x in 0.. adapterClient.Clients.size){

                        if(x == position){
                            scaleView(view!!,1f,1.2f)
                        }else{
                            binding.appBarMain.addclientitems?.adapter?.notifyItemChanged(x)
                        }
                    }
                    ClienLocalModel.SendselectItem(client)
                    Log.i("SETOCOFINAL",adapterClient.toString())
                }
                override fun onLongClick(view: View?, position: Int) {
                    if (view != null) {
                        view?.animate()
                            .yBy(-60f).xBy(-20f)
                            .setInterpolator(decayingSineWave)
                            .setDuration(200)
                            .start();

                        var dialog:Dialog = Dialog(this@MainActivity,R.style.MyDialogTheme)


                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.fragment_client_item_show)
                        dialog.show()

                       /* val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out,
                        )
                        var bundle=Bundle()
                        bundle.putInt("uid", adapterClient.Clients[position].uid)
                        ft.replace(R.id.ClientFragmnetShow, ClientItemShowFragment()::class.java,bundle,"");
                        ft.setReorderingAllowed(true)
                        ft.commit()
                        binding.appBarMain.ClientFragmnetShow?.visibility = View.VISIBLE*/
                    }
                }
            }
        ))
        ClienLocalModel.closeView.observe(this, Observer {
            binding.appBarMain.ClientFragmnetShow?.visibility = View.GONE
        })


        //Finalizando la insertacio nde dcodidwqd



        setSupportActionBar(binding.appBarMain.toolbar)
        setContentView(binding.root)
        setRefreshMain()
//        println("SE CREO INICIO")
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView

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
            R.id.HomeFragmnet,
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
//            Log.i("image",it.toString())
            if (it.toString() != "null" && it.toString() != null) {
//                val transformation: Transformation = RoundedCornersTransformation(radius, margin)

                Picasso.get()
                    .load("https://lh3.googleusercontent.com/a-/AOh14GjsFbwZhd41PQPxQoljtFkB92WkNfa6P5LTjAo_WQ=s96-c").fit()
                    .transform(CropCircleTransformation())
//                    .into(binding.drawerLayout.nav_view.getHeaderView(0).findViewById(R.id.ImageUserPresentation) as ImageView)
                    .into(header.ImageUserPresentation)
            }
        })

        header.TNameEmail?.text=(FirebaseAuth.getInstance().currentUser?.email?:R.string.anonyme).toString()
        header.TName?.text=(FirebaseAuth.getInstance().currentUser?.displayName?:R.string.nav_header_title).toString()

        wifiViewModel.conexSendResponse.observe(this, Observer {
            sendReceived=it
            try {
                for (x in ItemsSend) {
                    Log.i("Enviando Datos", ItemsSend.toString())
//                    var res =("PROC|" + x.uid + ";" + x.nombre + ";" + x.precioC + ";" + x.precioU + ";"+java.util.Base64.getEncoder().encodeToString(x.imageBit))
                    var qr=x.qr
                    if(qr==null || qr==""){
                        qr="Sin qr"
                    }
                    var res =("PROC|" + x.uid + ";" + x.nombre + ";" + x.precioC + ";" + x.precioU + ";"+qr+";"+null)
                    it.write(res.toByteArray())
                    Log.i("Enviando DATOS ZERIALIZADOS",res.toString())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        })


        MainModel.ResT.observe(this, Observer {x->
            if(sendReceived!=null){
                var qr=x.qr
                if(qr==null || qr==""){
                    qr="Sin qr"
                }
                var res =("PROC|" + x.uid + ";" + x.nombre + ";" + x.precioC + ";" + x.precioU + ";"+qr+";"+null)
                sendReceived?.write(res.toByteArray())
                Log.i("Enviando DATOS ZERIALIZADOS",res.toString())            }
        })


        MainModel.ResTFacture.observe(this, Observer {x->
            if(sendReceived!=null){
                var res="FACTURE|"
                for (s in x){
                    res+=s.nombre+":"+s.precioC+";"
                }
                sendReceived?.write(res.toByteArray())
                Log.i("Enviando Datos Zerializados [Factura]",res.toString())
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
                        )
                        serverClass.start()
//                    sendReceived=serverClass.returnRecived()
//                    wifiViewModel.addDeviceConected(p0.groupOwnerAddress.hostAddress,sendReceived)
                    } else if (p0!!.groupFormed) {
//                    MessageSnackBar(getApplicationContext() as View,"NO SE CONECTO",Color.RED)
                        MessageSnackBar(
                            binding.appBarMain.cordinatorRoot,
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
                        binding.appBarMain.cordinatorRoot,
                        "WIFI ACTIVADO",
                        Color.MAGENTA
                    )
                } else {
                    if (wifiViewModel.VerifyWifi()) {
                        MessageSnackBar(
                            binding.appBarMain.cordinatorRoot,
                            "WIFI ACTIVADO",
                            Color.MAGENTA
                        )
                        Log.i("WIFIRESFINAL", "SE ACTIVO")
                    } else {
                        MessageSnackBar(
                            binding.appBarMain.cordinatorRoot,
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

        //COnfigurando la barra por si existe , si o si tiene que existir lo que hace el primer fragmento
        wifiViewModel.WifiActivateBroadcast.observe(this, Observer {
            Log.i("P2PDesing", "Se decidio" + android.os.Build.VERSION.SDK_INT)
            if (28 != android.os.Build.VERSION.SDK_INT) {
                binding.appBarMain.toolbar?.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.background_bar
                    )
                );
            }

            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val ViewDrawable = DrawableCompat.wrap(binding.appBarMain.toolbar!!.background).mutate();
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
//                    DrawableCompat.setTint(ViewDrawable, Color.BLACK)
//                    binding.appBarMain.toolbar.background = ViewDrawable
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.rgb(0,200,0))
                        binding.appBarMain.toolbar?.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.rgb(220,0,0))
                        binding.appBarMain.toolbar?.background = ViewDrawable
                    }
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_NO -> {
                    if (it) {
                        DrawableCompat.setTint(ViewDrawable, Color.CYAN)
                        binding.appBarMain.toolbar?.background = ViewDrawable
                    } else {
                        DrawableCompat.setTint(ViewDrawable, Color.GRAY)
                        binding.appBarMain.toolbar?.background = ViewDrawable
                    }
                } // Night mode is active, we're using dark theme
            }
        })


        //blurbackground(binding.BlurVIewDrawable)


        val navigationMenuView = nav_view.getChildAt(0)
        if (navigationMenuView != null) {
            navigationMenuView.isVerticalScrollBarEnabled = true
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
        binding.appBarMain.fab.setOnLongClickListener {
            binding.appBarMain.cordinaterClient?.visibility=View.VISIBLE
            binding.appBarMain.fab.visibility = View.GONE
            binding.appBarMain.fabClient?.visibility=View.VISIBLE
            true

        }

        binding.appBarMain.fabClient?.setOnLongClickListener {
            binding.appBarMain.cordinaterClient?.visibility=View.GONE
            binding.appBarMain.fabClient?.visibility = View.GONE
            binding.appBarMain.fab.visibility=View.VISIBLE

            true
        }

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

        binding.appBarMain.SVProducts?.setOnQueryTextListener(object :
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
            Log.i("Segundo", binding.appBarMain.SVProducts?.query.toString())

            adapterProduct = ProductoAdapter(it, MainModel, 1, UtilsMainModel)
            adapterProduct.filter.filter(binding.appBarMain.SVProducts?.query,
                object : Filter.FilterListener {
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
            adapterLocal.filter.filter(binding.appBarMain.SVProducts?.query,
                object : Filter.FilterListener {
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
            adapterSever.filter.filter(binding.appBarMain.SVProducts?.query,
                object : Filter.FilterListener {
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
        wifiViewModel.onCreate()
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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_mains)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}