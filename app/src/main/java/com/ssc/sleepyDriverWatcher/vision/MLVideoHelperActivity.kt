package com.ssc.sleepyDriverWatcher.vision

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ssc.sleepyDriverWatcher.MainActivity
import com.ssc.sleepyDriverWatcher.R
import com.ssc.sleepyDriverWatcher.databinding.FragmentDriverDrowsinessDetectionBinding
import java.io.IOException

abstract class MLVideoHelperActivity : Fragment() {
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    protected var cameraSource: CameraSource? = null
    var thiscontext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDriverDrowsinessDetectionBinding>(inflater,
            R.layout.fragment_driver_drowsiness_detection,container,false)

        preview = binding.cameraSourcePreview
        graphicOverlay = binding.graphicOverlay

        if (ContextCompat.checkSelfPermission(requireContext().applicationContext,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        } else {
            initSource()
            startCameraSource()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource!!.release()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initSource()
            startCameraSource()
        }
    }

    private fun initSource() {
        if (isAdded) {
            if (cameraSource == null) {
                cameraSource = CameraSource(requireActivity(), graphicOverlay)
            }
            setProcessor()
            Log.d(TAG, "ola" + requireActivity())
        }
        else{
            Log.d(TAG, "ERROR - NULL CONTEXT")
        }
    }

    protected abstract fun setProcessor()
    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA = 1001
        private const val TAG = "MLVideoHelperActivity"
    }

}