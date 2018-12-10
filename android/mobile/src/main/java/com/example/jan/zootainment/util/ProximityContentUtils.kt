package com.example.jan.zootainment.util

import android.graphics.Color
import com.example.jan.zootainment.R

object ProximityContentUtils {

    internal fun getColor(animalName: String): Int {
        return when (animalName) {
            "elephant" -> Color.rgb(130,130,130)
            "giraffe" -> Color.rgb(190,180,70)
            "monkey" -> Color.rgb(140,100,60)

            else -> R.color.defaultContentBackground
        }
    }

    internal fun getDrawable(animalName: String): Int {
        return when (animalName) {
            //TODO load actual animal drawables
            "elephant" -> R.drawable.animal_default
            "giraffe" -> R.drawable.animal_default
            "monkey" -> R.drawable.animal_default

            else -> R.drawable.animal_default
        }
    }
}