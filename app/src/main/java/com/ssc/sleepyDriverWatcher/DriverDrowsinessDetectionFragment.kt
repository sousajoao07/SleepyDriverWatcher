package com.ssc.sleepyDriverWatcher

import com.ssc.sleepyDriverWatcher.vision.*

class DriverDrowsinessDetectionFragment : MLVideoHelperActivity() {
    override fun setProcessor() {
        cameraSource?.setMachineLearningFrameProcessor(FaceDetectorProcessor(requireContext().applicationContext))
    }

}