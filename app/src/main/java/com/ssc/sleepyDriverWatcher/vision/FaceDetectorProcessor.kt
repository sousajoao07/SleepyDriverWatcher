/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssc.sleepyDriverWatcher.vision

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.ssc.sleepyDriverWatcher.MainActivity

class FaceDetectorProcessor(context: Context?) : VisionProcessorBase<List<Face?>?>(context) {
    private val detector: FaceDetector
    private val drowsinessHashMap = HashMap<Int?, FaceDrowsiness>()

    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<List<Face?>?>? {
        return detector.process(image)
    }

    override fun onSuccess(results: List<Face?>, graphicOverlay: GraphicOverlay) {
        for (face in results) {
            var faceDrowsiness = drowsinessHashMap[face?.trackingId]
            if (faceDrowsiness == null) {
                faceDrowsiness = FaceDrowsiness()
                drowsinessHashMap[face?.trackingId] = faceDrowsiness
            }
            val isDrowsy = faceDrowsiness.isDrowsy(face)
            graphicOverlay.add(FaceGraphic(graphicOverlay, face, isDrowsy))

            if(isDrowsy){
                val intent = Intent(graphicOverlay.context, MainActivity::class.java).putExtra("Alert",true)
                graphicOverlay.context.startActivity(intent)
                stop()
            }
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

    init {
        val faceDetectorOptions = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
        Log.v(MANUAL_TESTING_LOG, "Face detector options: $faceDetectorOptions")
        detector = FaceDetection.getClient(faceDetectorOptions)
    }
}