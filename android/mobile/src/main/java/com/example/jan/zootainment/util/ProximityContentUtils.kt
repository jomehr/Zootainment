package com.example.jan.zootainment.util

import com.example.jan.zootainment.R

object ProximityContentUtils {

    internal fun getColor(animalName: String): Int {
        return when (animalName) {
            "elephant" -> R.color.elephant
            "giraffe" -> R.color.giraffe
            "monkey" -> R.color.monkey

            else -> R.color.defaultContentBackground
        }
    }

    internal fun getDrawable(animalName: String): Int {
        return when (animalName) {
            //TODO load actual animal drawables
            "elephant" -> R.drawable.ic_image_default
            "giraffe" -> R.drawable.ic_image_default
            "monkey" -> R.drawable.ic_image_default

            else -> R.drawable.ic_image_default
        }
    }
}