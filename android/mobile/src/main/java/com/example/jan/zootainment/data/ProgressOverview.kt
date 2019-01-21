package com.example.jan.zootainment.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ProgressOverview (
    var counter: Int? = 0,
    var timer: Int? = 0
)