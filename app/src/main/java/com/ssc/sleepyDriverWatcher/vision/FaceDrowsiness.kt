package com.ssc.sleepyDriverWatcher.vision

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import java.util.*


class FaceDrowsiness {
    private var lastCheckedAt: Long = 0

    private val history = ArrayDeque<Boolean>()

    fun isDrowsy(face: Face?): Boolean {
        var isDrowsy = true
        val cBottomMouthX = face!!.getLandmark(FaceLandmark.MOUTH_BOTTOM)!!.position.x
        val cBottomMouthY = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)!!.position.y
        //canvas.drawCircle(cBottomMouthX, cBottomMouthY, 10f, facePositionPaint)
        val cLeftMouthX = face.getLandmark(FaceLandmark.MOUTH_LEFT)!!.position.x
        val cLeftMouthY = face.getLandmark(FaceLandmark.MOUTH_LEFT)!!.position.y
        //canvas.drawCircle(cLeftMouthX, cLeftMouthY, 10f, facePositionPaint)
        val cRightMouthX = face.getLandmark(FaceLandmark.MOUTH_RIGHT)!!.position.x
        val cRightMouthY = face.getLandmark(FaceLandmark.MOUTH_RIGHT)!!.position.y
        //canvas.drawCircle(cRightMouthX, cRightMouthY, 10f, facePositionPaint)

        Log.i(TAG,"draw: Condition Bottom mouth >> cBottomMouthX >> $cBottomMouthX    cBottomMouthY >> $cBottomMouthY")

        val centerPointX = (cLeftMouthX + cRightMouthX) / 2
        val centerPointY = (cLeftMouthY + cRightMouthY) / 2 - 20

        //canvas.drawCircle(centerPointX, centerPointY, 10f, facePositionPaint)

        val differenceX = centerPointX - cBottomMouthX
        val differenceY = centerPointY - cBottomMouthY

        Log.i(TAG,"draw: difference X >> $differenceX     Y >> $differenceY")

        if (differenceY < -35) {
            Log.i(ContentValues.TAG, "draw: difference - Mouth is OPENED ")
        } else {
            Log.i(ContentValues.TAG, "draw: difference - Mouth is CLOSED ")
        }


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