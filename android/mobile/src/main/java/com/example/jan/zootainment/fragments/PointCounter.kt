package com.example.jan.zootainment.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jan.zootainment.R

class PointCounter: Fragment() {

    var totalCount: Int = 450

    private lateinit var valueAnimator: ValueAnimator
    private lateinit var points: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootview: View = inflater.inflate(R.layout.content_point_counter, container, false)

        points= rootview.findViewById(R.id.counter_points)

        return rootview
    }

    override fun onResume() {
        super.onResume()

        valueAnimator = ValueAnimator()
        valueAnimator.setObjectValues(0, totalCount)
        valueAnimator.addUpdateListener {
            points.text = it.animatedValue.toString()
        }
        valueAnimator.duration = 2000
        valueAnimator.start()
    }
}