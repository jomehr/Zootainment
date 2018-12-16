package com.example.jan.zootainment.util

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.example.jan.zootainment.MainActivity
import com.example.jan.zootainment.MyApplication
import com.example.jan.zootainment.QuizIntro
import com.example.jan.zootainment.R
import com.example.jan.zootainment.data.ProximityContent

class ProximityContentManager(private val context: Context) {

    private var proximityObserverHandler: ProximityObserver.Handler? = null

    fun start() {

        val proximityObserver = ProximityObserverBuilder(context, (context.applicationContext as MyApplication).cloudCredentials)
            .withTelemetryReportingDisabled()
            .withEstimoteSecureMonitoringDisabled()
            .onError { throwable ->
                Log.d(TAG, "proximity observer error: $throwable")
            }
            .withBalancedPowerMode()
            .build()

        val observer = ProximityZoneBuilder()
            .forTag("zootainment-6gm")
            .inCustomRange(9.0)
            .onContextChange { contexts ->
                val nearbyContent = ArrayList<ProximityContent>(contexts.size)
                Log.d(TAG, "${contexts.size}")

                for (data in contexts) {
                    Log.d(TAG, "attachment: ${data.attachments}")
                    val animal: String = data.attachments["animal"] ?: "unknown"
                    val subtitle = "questions: "
                    val questions = "x"
                    nearbyContent.add(ProximityContent(animal, subtitle, questions))
                }
                (context as MainActivity).setNearbyContent(nearbyContent)
            }
            .build()

        val animal = ProximityZoneBuilder()
            .forTag("zootainment-6gm")
            .inNearRange()
            .onContextChange { contexts ->
                for (data in contexts) {
                    val nearbyAnimal = data.attachments["animal"] ?: "unknown"

                    val intent = Intent(context, QuizIntro::class.java).putExtra("animal", nearbyAnimal)
                    val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(intent)
                        getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
                    }

                    val notification = NotificationCompat.Builder(context, "animal_close")
                        .setSmallIcon(R.drawable.animal_default)
                        .setContentTitle("$nearbyAnimal enclosure")
                        .setContentText("There is a quiz and feeding cannon available")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(context)) {
                        // id is a unique int for each notification that you must define
                        notify(1, notification.build())
                    }
                }
            }
            .onExit {
                with(NotificationManagerCompat.from(context)) {
                    cancel(1)
                }
            }
            .build()

        proximityObserverHandler = proximityObserver.startObserving(observer, animal)
    }

    fun stop() {
        proximityObserverHandler?.stop()
    }

    companion object {
        private const val TAG = "ProximityContentManager"
    }
}