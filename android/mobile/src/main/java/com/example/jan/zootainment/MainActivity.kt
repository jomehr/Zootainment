package com.example.jan.zootainment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.example.jan.zootainment.adapter.ProximityContentAdapter
import com.example.jan.zootainment.data.ProximityContent
import com.example.jan.zootainment.fragments.MainFragment
import com.example.jan.zootainment.fragments.MainFragmentTabList
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main_list.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var fragment: MainFragment
    private lateinit var fragmentTabList: MainFragmentTabList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser?.isAnonymous!!) {
            content_main_status.visibility = View.VISIBLE
            content_main_login.setOnClickListener {
                startActivity(Intent(this@MainActivity, Registration::class.java))
                finish()
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        fragment = MainFragment()
        fragmentTabList = MainFragmentTabList()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                {
                    Log.d(TAG, "requirements fulfilled")
                    fragmentTransaction.add(R.id.fragment_container, fragmentTabList).commit()
                },
                { requirements ->
                    Log.e(TAG, "requirements missing: $requirements")
                },
                { throwable ->
                    Log.e(TAG, "requirements error: $throwable")
                })

        nav_view.setNavigationItemSelectedListener(this)
    }

    fun setNearbyContent(nearbyContent: List<ProximityContent>) {
        Log.d(TAG, "setting content: activity")
        val proximityContentAdapter = ProximityContentAdapter(this)

        val gridView = fragmentTabList.gridView
        gridView.adapter = proximityContentAdapter

        proximityContentAdapter.setNearbyContent(nearbyContent)
        proximityContentAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: stopping proximityContentManager")

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
            R.id.dev_cannonControl -> {
                startActivity(Intent(this@MainActivity, Controller::class.java))
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation drawer item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {

            }
            R.id.nav_events -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_logout -> {
                //check if user is anonymous, if true ask to upgrade account to save data
                if (auth.currentUser?.isAnonymous!!) {
                    Log.d(TAG, "user is anonymous: ${auth.currentUser?.uid}")
                    convertAnonAccount()
                }else {
                    Log.d(TAG, "user is not anon")
                    auth.signOut()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun convertAnonAccount(): Boolean {
         AlertDialog.Builder(this@MainActivity)
            .setTitle("Warning")
            .setMessage("If you log out as an anonymous Account you will lose all data (progress, vouchers, etc). Do you want to convert to a permanent account?")
            .setCancelable(false)
            .setPositiveButton("Yes") {dialog, id ->
                startActivity(Intent(this@MainActivity, Registration::class.java))
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                auth.currentUser!!.delete().addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "deleteAnon:success")
                        dialog.cancel()
                        startActivity(Intent(this@MainActivity, Login::class.java))
                        finish()
                    } else {
                        Log.w(TAG, "deleteAnon:failure: ${task.exception}")
                        dialog.cancel()
                    }
                }
            }
            .show()
        return true
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
