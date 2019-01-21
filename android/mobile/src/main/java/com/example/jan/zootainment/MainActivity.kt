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

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    private lateinit var fragment: MainFragment
    private lateinit var fragmentTabList: MainFragmentTabList

    private lateinit var proximityContentAdapter: ProximityContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //init nav bar
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //init login reminder
        if (user!!.isAnonymous) {
            content_main_status.visibility = View.VISIBLE
            content_main_login.setOnClickListener {
                startActivity(Intent(this@MainActivity, Registration::class.java))
                finish()
            }
        }

        //init fragments
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment = MainFragment()
        fragmentTabList = MainFragmentTabList()


        //check requirements for bluetooth and location
        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                {
                    Log.d(TAG, "requirements fulfilled")
                    fragmentTransaction.replace(R.id.fragment_container, fragmentTabList).commit()
                },
                { requirements ->
                    Log.e(TAG, "requirements missing: $requirements")
                },
                { throwable ->
                    Log.e(TAG, "requirements error: $throwable")
                })

        nav_view.setNavigationItemSelectedListener(this)
    }

    //callback to update adapter
    fun setNearbyContent(nearbyContent: List<ProximityContent>) {
        proximityContentAdapter = ProximityContentAdapter(this)

        val gridView = fragmentTabList.gridView
        gridView.adapter = proximityContentAdapter

        proximityContentAdapter.setNearbyContent(nearbyContent)
        proximityContentAdapter.notifyDataSetChanged()
    }

    //give option to upgrade to permanent account or delete anon permanently before log out
    private fun convertAnonAccount(): Boolean {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Warning")
            .setMessage("If you log out as an anonymous Account you will lose all data (progress, vouchers, etc). Do you want to convert to a permanent account?")
            .setCancelable(false)
            .setPositiveButton("Yes") {dialog, id ->
                dialog.cancel()
                startActivity(Intent(this@MainActivity, Registration::class.java))
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                user!!.delete().addOnCompleteListener(this) { task ->
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // Handle action bar item clicks here.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dev_cannonControl -> {
                val intent = Intent(this@MainActivity, Controller::class.java)
                intent.putExtra("animal", "elephants")
                intent.putExtra("device", "cannon_1")
                startActivity(intent)
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Handle navigation drawer item clicks here.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
            }
            R.id.nav_events -> {
            }
            R.id.nav_settings -> {
            }
            R.id.nav_logout -> {
                //check if user is anonymous, if true ask to upgrade account to save data
                if (user!!.isAnonymous) {
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

    companion object {
        const val TAG = "MainActivity"
    }
}
