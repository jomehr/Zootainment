package com.example.jan.zootainment.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class OverviewData (
        var cost: String? = "",
        var limit: String? = "",
        var questions: String? = ""
)