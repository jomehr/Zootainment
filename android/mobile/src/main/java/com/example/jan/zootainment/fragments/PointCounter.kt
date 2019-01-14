package com.example.jan.zootainment.fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jan.zootainment.Controller
import com.example.jan.zootainment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PointCounter : Fragment() {

    private var totalCount: Int = 0

    private lateinit var valueAnimator: ValueAnimator
    private lateinit var rootview: View
    private lateinit var points: TextView
    private lateinit var mListener: OnPointsCounterListener

    //private lateinit var userPointsRef: DatabaseReference

    private val user = FirebaseAuth.getInstance().currentUser?.uid!!
    private val firebase = FirebaseDatabase.getInstance().reference.child("users").child(user).child("points")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        rootview = inflater.inflate(R.layout.content_point_counter, container, false)

        //userPointsRef = firebase.child("users").child(user).child("points")

        points = rootview.findViewById(R.id.counter_points)
        points.text = "0"

        return rootview
    }

    fun startCounterDown(total: Int, points: Int, animal: String) {
        valueAnimator = ValueAnimator()
        val countView =  view?.findViewById<TextView>(R.id.counter_points)
        val result = total-points

        valueAnimator.setObjectValues(total, result)
        valueAnimator.addUpdateListener {
            countView?.setTextColor(ContextCompat.getColor(context!!, R.color.wrong))
            countView?.text = it.animatedValue.toString()

        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                //countView?.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                firebase.setValue(result)
                val intent = Intent(activity, Controller::class.java)
                intent.putExtra("animal", animal)
                startActivity(intent)
                activity?.finish()
            }
        })
        valueAnimator.duration = 1000
        valueAnimator.start()
    }

    fun startCounterUp(points: Int) {
        totalCount = points

        valueAnimator = ValueAnimator()

        valueAnimator.setObjectValues(0, totalCount)
        valueAnimator.addUpdateListener {
            view?.findViewById<TextView>(R.id.counter_points)?.text = it.animatedValue.toString()

        }
        valueAnimator.duration = 1000
        valueAnimator.start()
    }

    override fun onResume() {
        super.onResume()

        firebase.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadPoints:onCancelled", error.toException())
                }

                override fun onDataChange(data: DataSnapshot) {
                    val count = data.getValue(Int::class.java)!!
                    mListener.messageFromCounter(count)
                    startCounterUp(count)
                }
            }
        )
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnPointsCounterListener) mListener = context
        else throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")

        if (parentFragment is OnPointsCounterListener) mListener = parentFragment as OnPointsCounterListener
        else throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
    }

    interface OnPointsCounterListener {
        fun messageFromCounter(count: Int)
    }

    companion object {
        private const val TAG = "PointCounter"
    }
}