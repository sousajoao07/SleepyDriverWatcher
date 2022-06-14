package com.ssc.sleepyDriverWatcher.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityTransitionResult
import com.ssc.sleepyDriverWatcher.ActivityRecognitionFragment
import com.ssc.sleepyDriverWatcher.DriverDrowsinessDetectionFragment
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
                    Log.i(ContentValues.TAG, "ola" + ActivityTransitionsUtil.toActivityString(
                        event.activityType
                    ) )
                    if (ActivityTransitionsUtil.toActivityString(
                            event.activityType
                        ) == "WALKING"
                    ) {
                        Notify
                            .with(context)
                            .meta { // this: Payload.Meta
                                // Launch the MainActivity once the notification is clicked.

                                clickIntent = PendingIntent.getActivity(
                                    context,
                                    0,
                                    Intent(context, MainActivity::class.java).
                                    putExtra("notificationWalk", true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                                    0,
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
                                    Intent(context, MainActivity::class.java).
                                            putExtra("notificationWalk", false),
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