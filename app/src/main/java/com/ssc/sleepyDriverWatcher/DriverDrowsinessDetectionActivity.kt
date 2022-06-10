package com.ssc.sleepyDriverWatcher

import android.content.Intent
import android.os.Bundle
import com.ssc.sleepyDriverWatcher.vision.FaceDetectorProcessor
import com.ssc.sleepyDriverWatcher.vision.MLVideoHelperActivity


class DriverDrowsinessDetectionActivity : MLVideoHelperActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setProcessor() {
        cameraSource?.setMachineLearningFrameProcessor(FaceDetectorProcessor(this))
    }

    override fun onBackPressed() {
        val intent = Intent(this.applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}