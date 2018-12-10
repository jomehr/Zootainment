package com.example.jan.zootainment

import android.app.Application
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

class MyApplication : Application() {

    val cloudCredentials =  EstimoteCloudCredentials("zootainment-6gm", "799c864f33c81a3f8410c1d615c7ec06")
}
