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
import android.widget.GridView
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.example.jan.zootainment.adapter.ProximityContentAdapter
import com.example.jan.zootainment.data.ProximityContent
import com.example.jan.zootainment.util.ProximityContentManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var proximityContentManager: ProximityContentManager? = null
    private var proximityContentAdapter: ProximityContentAdapter? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        proximityContentAdapter = ProximityContentAdapter(this)
        val gridView = findViewById<GridView>(R.id.gridView)
        gridView.adapter = proximityContentAdapter

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                {
                    Log.d(TAG, "requirements fulfilled")
                    startProximityContentManager()

                },
                { requirements ->
                    Log.e(TAG, "requirements missing: $requirements")
                }
                , { throwable ->
                    Log.e(TAG, "requirements error: $throwable")
                })

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun startProximityContentManager() {
        proximityContentManager = ProximityContentManager(this)
        proximityContentManager?.start()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        if (proximityContentManager == null) {
            Log.d(TAG, "onStart: starting proximityContentManager")
            startProximityContentManager()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: stopping proximityContentManager")
        proximityContentManager?.stop()
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

    fun setNearbyContent(nearbyContent: List<ProximityContent>) {
        proximityContentAdapter?.setNearbyContent(nearbyContent)
        proximityContentAdapter?.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
