package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        database = FirebaseDatabase.getInstance().reference

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        action_moveLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("movement_left").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("movement_left").setValue(false)
            }
            true
        }

        action_moveRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("movement_right").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("movement_right").setValue(false)
            }
            true
        }

        action_shoot.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> database.child("cannon_actions").child("shoot").setValue(true)
                MotionEvent.ACTION_UP -> database.child("cannon_actions").child("shoot").setValue(false)
            }
            true
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {

            }
            R.id.nav_events -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_logout -> {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                //check if user is anonymous, if true ask to upgrade account to save data
                if (auth.currentUser?.isAnonymous!!) {
                    //TODO ask to upgrade to permanent account//show warning of data loss otherwise
                    auth.currentUser?.delete()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }else {
                    auth.signOut()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
