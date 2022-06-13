package com.ssc.sleepyDriverWatcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ssc.sleepyDriverWatcher.databinding.FragmentActivityRecognitionBinding
import com.ssc.sleepyDriverWatcher.databinding.FragmentDriverDrowsinessAlertBinding

class DriverDrowsinessAlertFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentDriverDrowsinessAlertBinding>(inflater,
            R.layout.fragment_driver_drowsiness_alert,container,false)
        return binding.root;
    }
}