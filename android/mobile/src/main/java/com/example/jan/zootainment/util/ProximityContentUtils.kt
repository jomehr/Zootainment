package com.example.jan.zootainment.util

import com.example.jan.zootainment.R

object ProximityContentUtils {

    internal fun getColor(animalName: String): Int {
        return when (animalName) {
            "elephants" -> R.color.elephant
            "giraffes" -> R.color.giraffe
            "baboons" -> R.color.monkey

            else -> R.color.defaultContentBackground
        }
    }

    internal fun getDrawable(animalName: String): Int {
        return when (animalName) {
            "elephants" -> R.drawable.ic_icon_elephant
            "giraffes" -> R.drawable.ic_icon_giraffe
            "baboons" -> R.drawable.ic_icon_monkey

            else -> R.drawable.ic_image_default
        }
    }
}