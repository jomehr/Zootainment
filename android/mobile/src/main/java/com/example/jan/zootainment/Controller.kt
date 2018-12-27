package com.example.jan.zootainment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_controler.*
import kotlinx.android.synthetic.main.app_bar_main.*

class Controller : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controler)

        database = FirebaseDatabase.getInstance().reference

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimaryDark)
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("movement_left").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimary)
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("movement_left").setValue(false)
                }
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimaryDark)
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("movement_right").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    changeBackgroundcolor(this, v, R.color.colorPrimary)
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("movement_right").setValue(false)
                }
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("shoot").setValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    database.child("enclosure_1").child("devices").child("cannon1")
                        .child("shoot").setValue(false)
                }
            }
            true
        }
    }

    private fun changeBackgroundcolor (context: Context, view: View, color: Int) {
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }
}