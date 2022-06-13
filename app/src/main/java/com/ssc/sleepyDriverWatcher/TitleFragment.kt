package com.ssc.sleepyDriverWatcher

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ssc.sleepyDriverWatcher.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_title, container, false)
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)

        binding.startButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_driverDrowsinessDetectionFragment3)
        }

        return binding.root
    }

}