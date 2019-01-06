package com.example.jan.zootainment

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*

class Controller : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        databaseRef = FirebaseDatabase.getInstance().reference
            .child("enclosure_1").child("devices")
            .child("cannon_1").child("movement_left")

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimaryDark)
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimary)
                    databaseRef.setValue(false)
                }
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimaryDark)
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimary)
                    databaseRef.setValue(false)
                }
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    databaseRef.setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    databaseRef.setValue(false)
                }
            }
            true
        }
    }

    private fun changeBackgroundcolor (context: Context, view: View, color: Int) {
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }
}