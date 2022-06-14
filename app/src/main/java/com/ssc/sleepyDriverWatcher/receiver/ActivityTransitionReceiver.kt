package com.ssc.sleepyDriverWatcher.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.ActivityTransitionResult
import com.ssc.sleepyDriverWatcher.DriverDrowsinessDetectionActivity
import com.ssc.sleepyDriverWatcher.MainActivity
import com.ssc.sleepyDriverWatcher.util.ActivityTransitionsUtil
import com.ssc.sleepyDriverWatcher.util.Constants
import io.karn.notify.Notify
import java.text.SimpleDateFormat
import java.util.*

class ActivityTransitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            result?.let {
                result.transitionEvents.forEach { event ->
                    //Info for debugging purposes
                    val info =
                        "Transition: " + ActivityTransitionsUtil.toActivityString(event.activityType) +
                                " (" + ActivityTransitionsUtil.toTransitionType(event.transitionType) + ")" + "   " +
                                SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())

                    if (ActivityTransitionsUtil.toActivityString(
                            event.activityType
                        ) == "IN_VEHICLE"
                    ) {
                        Notify
                            .with(context)
                            .meta { // this: Payload.Meta
                                // Launch the MainActivity once the notification is clicked.
                                clickIntent = PendingIntent.getActivity(
                                    context,
                                    0,
                                    Intent(context, DriverDrowsinessDetectionActivity::class.java),
                                    0
                                )
                            }
                            .content {
                                title = "Activity Detected"
                                text =
                                    "I can see you are in ${
                                        ActivityTransitionsUtil.toActivityString(
                                            event.activityType
                                        )
                                    } state"
                            }
                            .show(id = Constants.ACTIVITY_TRANSITION_NOTIFICATION_ID)

                        Toast.makeText(context, info, Toast.LENGTH_LONG).show()
                    } else {
                        Notify
                            .with(context)
                            .meta { // this: Payload.Meta
                                // Launch the MainActivity once the notification is clicked.
                                clickIntent = PendingIntent.getActivity(
                                    context,
                                    0,
                                    Intent(context, MainActivity::class.java),
                                    0
                                )
                            }
                            .content {
                                title = "Activity Detected"
                                text =
                                    "I can see you are in ${
                                        ActivityTransitionsUtil.toActivityString(
                                            event.activityType
                                        )
                                    } state"
                            }
                            .show(id = Constants.ACTIVITY_TRANSITION_NOTIFICATION_ID)
                    }
                }
            }
        }
    }
}