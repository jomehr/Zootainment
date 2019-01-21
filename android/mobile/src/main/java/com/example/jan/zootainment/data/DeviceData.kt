package com.example.jan.zootainment.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DeviceData (
    var title: String? = "",
    var cost: Int? = 0,
    var limit: String? = ""
)