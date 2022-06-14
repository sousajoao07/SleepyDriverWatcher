package com.ssc.sleepyDriverWatcher

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ssc.sleepyDriverWatcher.databinding.FragmentDriverDrowsinessAlertBinding

class DriverDrowsinessAlertFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDriverDrowsinessAlertBinding>(inflater,
            R.layout.fragment_driver_drowsiness_alert,container,false)

        val am = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val initial_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        var max_volume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        am.setStreamVolume(AudioManager.STREAM_MUSIC, max_volume, 0)

        mp = MediaPlayer.create(context,R.raw.alarm)
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max_volume, 0)

        with(mp) { this?.start() }

        binding.stopButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.driverDrowsinessDetectionFragment3)
            am.setStreamVolume(AudioManager.STREAM_MUSIC, initial_volume, 0)
        }

        return binding.root;
    }

    override fun onPause() {
        super.onPause()
        if (mp!!.isPlaying) {
            mp!!.stop()
        }
    }

    companion object {
        var mp: MediaPlayer? = null
    }

}