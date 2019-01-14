package com.example.jan.zootainment.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class OverviewData (
        var cost: Int = 0,
        var limit: String = "",
        var questions: Int = 0
)