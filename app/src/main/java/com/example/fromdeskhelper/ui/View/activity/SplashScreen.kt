package com.example.fromdeskhelper.ui.View.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.fromdeskhelper.databinding.ActivitySplashScreenBinding
import com.example.fromdeskhelper.ui.View.ViewModel.InitViewModel
import com.example.fromdeskhelper.ui.View.ViewModel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()
    private val loginViewModel: InitViewModel by viewModels();
    private val SplashModel : SplashScreenViewModel by viewModels();

  /*  @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
           fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }*/
    private var isFullscreen: Boolean = false


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    override fun onStart() {

        super.onStart()
    }
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //isFullscreen = true
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
        loginViewModel();

//        runOnUiThread {
//            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(AuthorizationInterceptor(baseContext))
//                .build()
//            var primero = ApolloClient.Builder().serverUrl("http://192.168.0.13:2016/graphql")
//                .okHttpClient(okHttpClient)
//                .build()
//            lifecycleScope.launch {
//                try {
//                    var res = primero.query(ComprobateUserQuery("ss")).execute().toString()
//                    Toast.makeText(baseContext,res,Toast.LENGTH_LONG)
//                }catch(e:Exception) {
//                    e.printStackTrace(System.out);
//                }
//            }
//        }
        SplashModel(baseContext);


        SplashModel.initLogin.observe(this, Observer {
            startActivity(Intent(baseContext,LoginActivity::class.java))
            Animatoo.animateZoom(this);
            finish()
        })
        SplashModel.initLogOperator.observe(this, Observer {
           if(it) {
               binding.TEInformation.text = "Iniciando...";
               startActivity(Intent(baseContext,MainActivity::class.java))
               Animatoo.animateZoom(this);
               finish()
           }else{

              binding.TEInformation.text = "Error al iniciar Session";
               startActivity(Intent(baseContext,LoginActivity::class.java))
               Animatoo.animateShrink(this);
               finish()
//               while (true) {
//                   if (isConnected(baseContext)) {
//                       SplashModel(baseContext);
//                   }
//               }
           }
        })
        SplashModel.initLogMessage.observe(this, Observer {
            binding.TEInformation.text = it;
        })

        SplashModel.initLoginAnonime.observe(this, Observer {
            //SE ESTA INICIANDO 2 VECES BUSCANDO EL ERROR.. Solucionado faltaba un break al terminal el analisis
            binding.TEInformation.text="Anonimo!"
            startActivity(Intent(baseContext,MainActivity::class.java))
            Animatoo.animateZoom(this);
            finish()
        })

        SplashModel.initPrecentation.observe(this, Observer {
            startActivity(Intent(baseContext,PresenatationActivity::class.java))
            Animatoo.animateDiagonal(this);
            finish()
        })

        loginViewModel.registerresult.observe(this, Observer {
            binding.TEInformation.text = "Comprando Autenticacion"
            if (it) {
//                runOnUiThread {
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    finish()
//                }
            }
        })
//        loginViewModel.initLog.observe(this, Observer {
//            Log.i("VISTA","Se actualizao")
//        })

//        if (changepreferensd()) {
//            binding.PBSS.visibility = View.VISIBLE
//            if (changeverifyuser()) {
//                var dato: Connection? = null;
//                val con = ConnectionController()
//                GlobalScope.launch(Dispatchers.IO) {
//                    val join = launch { dato = con.Conn() }
//                    join.join()
//                    if (join.isCompleted) {
//                        if (dato != null) {
//                            val newConsult: HashMap<Intent, String> =
//                                ConnectionController().verifysession(
//                                    dato,
//                                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
//                                    baseContext
//                                )
//                            runOnUiThread {
//                                val new = newConsult.entries.iterator().next()
//                                Toast.makeText(baseContext, new.value, Toast.LENGTH_SHORT).show()
//                                startActivity(new.key)
//                                finish()
//                            }
//                        }
//                    }
//                }
//            } else {
////                startActivity(Intent(this, LoginActivity::class.java))
////                finish()
//            }
//        } else {
//            startActivity(Intent(this, PresenatationActivity::class.java))
//            finish()
//        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(1000)
    }



   /* private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = true


        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }*/

//    private fun changepreferensd(): Boolean {
//
//        val Mypreferencia: SharedPreferences = getSharedPreferences("init", Context.MODE_PRIVATE)
//        return Mypreferencia.getBoolean("initialpresentation", false)
//    }

//    private fun changeverifyuser(): Boolean {
//        val Provider: SharedPreferences = getSharedPreferences("logins", Context.MODE_PRIVATE)
//        val email = Provider.getString("email", null)
//        val provedor = Provider.getString("provider", null)
//        if (email == null) {
//            Log.i("Init", "Provedor iniciado en $provedor")
//            return false
//        } else {
//            return true
//        }
//    }


    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
//        hideHandler.removeCallbacks(hideRunnable)
//        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */

        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}