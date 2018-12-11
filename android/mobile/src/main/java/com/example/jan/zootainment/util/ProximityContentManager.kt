package com.example.jan.zootainment.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.example.jan.zootainment.MainActivity
import com.example.jan.zootainment.MyApplication
import com.example.jan.zootainment.QuizIntro
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
            .inFarRange()
            .onContextChange { contexts ->
                val nearbyContent = ArrayList<ProximityContent>(contexts.size)
                Log.d(TAG, "${contexts.size}")

                for (context in contexts) {
                    Log.d(TAG, "attachment: ${context.attachments}")
                    val animal: String = context.attachments["animal"] ?: "unknown"
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
            .onEnter { contexts ->
                Log.d(TAG, "on enter: ${contexts.attachments["animal"]}")
                val intent = Intent(context as MainActivity, QuizIntro::class.java)
                intent.putExtra("animal", contexts.attachments["animal"] ?: "unknown")
                context.startActivity(intent)
                stop()
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