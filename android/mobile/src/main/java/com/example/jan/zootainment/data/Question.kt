package com.example.jan.zootainment.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Question(
    var question: String? = "",
    var solution: Int? = 0,
    var answer1: String? = "",
    var answer2: String? = "",
    var answer3: String? = "",
    var answer4: String? = ""
)