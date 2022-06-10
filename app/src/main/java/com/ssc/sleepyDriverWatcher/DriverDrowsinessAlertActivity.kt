package com.ssc.sleepyDriverWatcher

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button

class DriverDrowsinessAlertActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        val initial_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        var max_volume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        setContentView(R.layout.activity_driver_sleep_alert)

        am.setStreamVolume(AudioManager.STREAM_MUSIC, max_volume, 0);

        mp = MediaPlayer.create(this, R.raw.alarm)
        with(mp) { this?.start() }

        val button = findViewById<View>(R.id.stop_button) as Button
        button.setOnClickListener { v ->
            am.setStreamVolume(AudioManager.STREAM_MUSIC, initial_volume, 0);
            val intent = Intent(v.context, DriverDrowsinessDetectionActivity::class.java)
            v.context.startActivity(intent)
            finish()
        }
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