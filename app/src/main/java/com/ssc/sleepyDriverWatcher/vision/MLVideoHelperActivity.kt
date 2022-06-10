package com.ssc.sleepyDriverWatcher.vision

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ssc.sleepyDriverWatcher.R
import java.io.IOException

abstract class MLVideoHelperActivity : AppCompatActivity() {
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    protected var cameraSource: CameraSource? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_helper)
        preview = findViewById(R.id.camera_source_preview)
        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        } else {
            initSource()
            startCameraSource()
        }
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
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }
        setProcessor()
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