package com.example.fromdeskhelper.ui.View.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp

import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.databinding.ActivityInitPrecentationBinding
import com.example.fromdeskhelper.ui.main.SectionsPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class PresenatationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInitPrecentationBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitPrecentationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

//        val viewPager: ViewPager = binding.
//        viewPager.adapter = sectionsPagerAdapter


//        setSupportActionBar(binding.toolbar)

            navController = findNavController(R.id.nav_host_fragment_content_presentation_main)
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
}