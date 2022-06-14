package com.ssc.sleepyDriverWatcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ssc.sleepyDriverWatcher.databinding.ActivityMainBinding
import com.ssc.sleepyDriverWatcher.receiver.ActivityTransitionReceiver


class MainActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var notificationIntent = intent.getBooleanExtra("notificationWalk",false)
        var isAlert = intent.getBooleanExtra("Alert",false)
        Log.e("ola", "ola" + notificationIntent.toString())

        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        if (isAlert){
        navController.navigate(R.id.driverDrowsinessAlertFragment2)
        }
        // If menuFragment is defined, then this activity was launched with a fragment selection
        // If menuFragment is defined, then this activity was launched with a fragment selection

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (notificationIntent) {
                navController.navigate(R.id.driverDrowsinessDetectionFragment3)
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}