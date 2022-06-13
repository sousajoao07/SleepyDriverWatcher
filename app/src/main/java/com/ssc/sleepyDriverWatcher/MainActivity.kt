package com.ssc.sleepyDriverWatcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ssc.sleepyDriverWatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        var menuFragment = intent.getStringExtra("main")
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        // If menuFragment is defined, then this activity was launched with a fragment selection
        // If menuFragment is defined, then this activity was launched with a fragment selection
        if (menuFragment != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment == "DriverDrowsinessDetectionFragment") {
                navController.navigate(R.id.driverDrowsinessDetectionFragment3)
            }
        } else {
            // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
            navController.navigate(R.id.titleFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}