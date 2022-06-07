package com.example.fromdeskhelper.ui.View.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.viewpager.widget.ViewPager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ActivityInitPrecentationBinding
import com.example.fromdeskhelper.ui.View.Presentations.UnoPresentacionFragment
import com.example.fromdeskhelper.ui.View.adapter.ViewPagerPresentation
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.fragment_dos_precentacion.view.*
import kotlinx.android.synthetic.main.fragment_tres_presentacion.view.*
import kotlinx.android.synthetic.main.fragment_uno_precentacion.view.*


@AndroidEntryPoint

class PresenatationActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInitPrecentationBinding
    lateinit var navController: NavController
    private var myViewList: MutableList<View> = mutableListOf()


    private fun blurbackground(view: BlurView) {
        val radius = 5f
        val decorView: View =  window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view.setupWith(binding.viewPagerPresentation)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
//            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityInitPrecentationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val actionBar: ActionBar? = supportActionBar
//        if (actionBar != null) actionBar.hide()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val layoutInflater = LayoutInflater.from(this)
        val view1: View = layoutInflater.inflate(R.layout.fragment_uno_precentacion, null)
        val view2: View = layoutInflater.inflate(R.layout.fragment_dos_precentacion, null)
        val view3: View = layoutInflater.inflate(R.layout.fragment_tres_presentacion, null)
        val radius = 5f
        val decorView: View =  window!!.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        view1.blurView.setupWith(binding.viewPagerPresentation)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)

        view2.blurView2.setupWith(binding.viewPagerPresentation)
//            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)

        view3.blurView3.setupWith(view3.sss)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
        view3.BNext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Animatoo.animateZoom(this);  //fire the zoom animation
            finish()
        }

//        view1.textViewUnoe.text="sss"
//        view2.textViewdow.text="sasass"
        myViewList.add(view1)
        myViewList.add(view2)
        myViewList.add(view3)


        val adapter = ViewPagerPresentation(myViewList)
        binding.viewPagerPresentation.adapter=adapter
//        binding.viewPagerPresentation.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//        })

//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

//        val viewPager: ViewPager = binding.
//        viewPager.adapter = sectionsPagerAdapter


//        setSupportActionBar(binding.toolbar)

//            navController = findNavController(R.id.nav_host_fragment_content_presentation_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
            val navBuilder = NavOptions.Builder()
            navBuilder.setEnterAnim(R.anim.slide_top).setExitAnim(R.anim.wait_anim)
                .setPopEnterAnim(R.anim.wait_anim).setPopExitAnim(R.anim.slide_bottom)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
        val navController = findNavController(R.id.nav_host_fragment_content_mains)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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

        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): UnoPresentacionFragment {
            return UnoPresentacionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }

    }

}