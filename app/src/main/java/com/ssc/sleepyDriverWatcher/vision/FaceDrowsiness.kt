package com.ssc.sleepyDriverWatcher.vision

import com.google.mlkit.vision.face.Face
import java.util.*


class FaceDrowsiness {
    private var lastCheckedAt: Long = 0

    private val history = ArrayDeque<Boolean>()

    fun isDrowsy(face: Face?): Boolean {
        var isDrowsy = true
        lastCheckedAt = System.currentTimeMillis()
        if (face?.leftEyeOpenProbability == null
            || face.rightEyeOpenProbability == null
        ) {
            return false
        }
        if (face.leftEyeOpenProbability!! < DROWSINESS_THRESHOLD
            && face.rightEyeOpenProbability!! < DROWSINESS_THRESHOLD
        ) {
            history.addLast(true)
        } else {
            history.addLast(false)
        }
        if (history.size > MAX_HISTORY) {
            history.removeFirst()
        }
        if (history.size == MAX_HISTORY) {
            for (instance in history) {
                isDrowsy = isDrowsy and instance
            }
        } else {
            return false
        }
        return isDrowsy
    }

    companion object {
        private const val DROWSINESS_THRESHOLD = 0.3f
        private const val MAX_HISTORY = 10
    }
}