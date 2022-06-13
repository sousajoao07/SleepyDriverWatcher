package com.ssc.sleepyDriverWatcher

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.ssc.sleepyDriverWatcher.databinding.FragmentActivityRecognitionBinding
import com.ssc.sleepyDriverWatcher.receiver.ActivityTransitionReceiver
import com.ssc.sleepyDriverWatcher.util.ActivityTransitionsUtil
import com.ssc.sleepyDriverWatcher.util.Constants
import kotlinx.android.synthetic.main.fragment_activity_recognition.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ActivityRecognitionFragment : Fragment(), EasyPermissions.PermissionCallbacks{
    lateinit var client: ActivityRecognitionClient
    lateinit var storage: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentActivityRecognitionBinding>(inflater,
            R.layout.fragment_activity_recognition,container,false)

        client = ActivityRecognition.getClient(requireContext().applicationContext)
        storage = PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)

        binding.switchActivityTransition.isChecked = getRadioState()

        binding.switchActivityTransition.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                    && !ActivityTransitionsUtil.hasActivityTransitionPermissions(requireContext().applicationContext)
                ) {
                    switchActivityTransition.isChecked = false
                    requestActivityTransitionPermission()
                } else {
                    saveRadioState(true)
                    requestForUpdates()
                }
            } else {
                saveRadioState(false)
                deregisterForUpdates()
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestActivityTransitionPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        switchActivityTransition.isChecked = true
        saveRadioState(true)
        requestForUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestForUpdates() {
        client
            .requestActivityTransitionUpdates(
                ActivityTransitionsUtil.getActivityTransitionRequest(),
                getPendingIntent()
            )
            .addOnSuccessListener {
                showToast("successful registration")
            }
            .addOnFailureListener { e: Exception ->
                showToast("Unsuccessful registration")
            }
    }

    private fun deregisterForUpdates() {
        client
            .removeActivityTransitionUpdates(getPendingIntent())
            .addOnSuccessListener {
                getPendingIntent().cancel()
                showToast("successful deregistration")
            }
            .addOnFailureListener { e: Exception ->
                showToast("unsuccessful deregistration")
            }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, ActivityTransitionReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            Constants.REQUEST_CODE_INTENT_ACTIVITY_TRANSITION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestActivityTransitionPermission() {
        EasyPermissions.requestPermissions(
            this,
            "You need to allow activity transition permissions in order to use this feature",
            Constants.REQUEST_CODE_ACTIVITY_TRANSITION,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun saveRadioState(value: Boolean) {
        storage
            .edit()
            .putBoolean(Constants.ACTIVITY_TRANSITION_STORAGE, value)
            .apply()
    }

    private fun getRadioState() = storage.getBoolean(Constants.ACTIVITY_TRANSITION_STORAGE, false)

}