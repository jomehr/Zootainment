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
import com.example.jan.zootainment.AnimalActivity
import com.example.jan.zootainment.MainActivity
import com.example.jan.zootainment.MyApplication
import com.example.jan.zootainment.R
import com.example.jan.zootainment.data.ProgressOverview
import com.example.jan.zootainment.data.ProximityContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ProximityContentManager(private val context: Context) {

    private var proximityObserverHandler: ProximityObserver.Handler? = null
    private var dateMillis: Long = 0

    private lateinit var progressReference: DatabaseReference
    private lateinit var progressOverviewReference: DatabaseReference

    private val user = FirebaseAuth.getInstance().currentUser?.uid!!
    private val firebase = FirebaseDatabase.getInstance().reference.child("users").child(user)

    fun start() {

        val proximityObserver =
            ProximityObserverBuilder(context, (context.applicationContext as MyApplication).cloudCredentials)
                .withTelemetryReportingDisabled()
                .withEstimoteSecureMonitoringDisabled()
                .withAnalyticsReportingDisabled()
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
                Log.d(TAG, "count: ${contexts.size}")

                dateMillis = Date().time
                Log.d(TAG, "Time: $dateMillis")
                for (data in contexts) {
                    val animal: String = data.attachments["animal"] ?: "unknown"

                    progressReference = firebase.child(animal)
                    progressOverviewReference = progressReference.child("overview")

                    progressOverviewReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.w(TAG, "loadProgress:onCancelled", databaseError.toException())
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val questions: Int?
                            if (dataSnapshot.exists()) {
                                val progressData = dataSnapshot.getValue(ProgressOverview::class.java)
                                val timeDif = (((((dateMillis / 1000) - progressData?.timer!!) / 60) / 60) / 24).toDouble()
                                Log.d(TAG, "timer: " + timeDif.toString())
                                if (timeDif <= 1) {
                                    questions = progressData.counter
                                    nearbyContent.add(ProximityContent(animal, questions!!))
                                } else {
                                    Log.d(TAG, "older then 24h")
                                    questions = 0
                                    nearbyContent.add(ProximityContent(animal, questions))
                                    progressReference.removeValue()
                                }
                            } else {
                                questions = 0
                                nearbyContent.add(ProximityContent(animal, questions))
                            }
                            (context as MainActivity).setNearbyContent(nearbyContent)
                        }
                    })
                    Log.d(TAG, "attachment: ${data.attachments}")
                }
            }
            .build()

        val animal = ProximityZoneBuilder()
            .forTag("zootainment-6gm")
            .inNearRange()
            .onContextChange { contexts ->
                for (data in contexts) {
                    val nearbyAnimal = data.attachments["animal"] ?: "unknown"

                    val intent = Intent(context, AnimalActivity::class.java).putExtra("animal", nearbyAnimal)
                    val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(intent)
                        getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
                    }

                    val notification = NotificationCompat.Builder(context, "animal_close")
                        .setSmallIcon(R.mipmap.logo_round)
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