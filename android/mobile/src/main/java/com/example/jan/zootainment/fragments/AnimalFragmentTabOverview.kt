package com.example.jan.zootainment.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.jan.zootainment.R
import com.example.jan.zootainment.adapter.DeviceContentAdapter
import com.example.jan.zootainment.data.DeviceData

class AnimalFragmentTabOverview: Fragment(), PointCounter.OnPointsCounterListener, DeviceContentAdapter.DeviceAdapterCallback {

    private lateinit var deviceContentAdapter: DeviceContentAdapter
    private lateinit var deviceList: DeviceList

    private var curCount = 0

    override fun messageFromCounter(count: Int) {
        Log.d(TAG, "counter: $count")
        curCount = count
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "creating TabOverviewView")

        //init view
        val rootView: View = inflater.inflate(R.layout.content_animal_overview, container, false)

        return rootView
    }

    fun changeDeviceList(devices: List<DeviceData>, animal: String) {
        deviceContentAdapter.setDeviceContent(devices, animal, this)
        deviceContentAdapter.notifyDataSetChanged()
    }

    override fun startCounter(cost: Int, animal: String, device: String) {
        AlertDialog.Builder(context!!)
            .setTitle("Warning")
            .setMessage("You are about to spend ${cost} points. Your total points will drop to ${(curCount - cost)}")
            .setCancelable(false)
            .setPositiveButton("Continue") {dialog, id ->
                val counter: PointCounter = childFragmentManager.findFragmentById(R.id.fragment_point_counter) as PointCounter
                counter.startCounterDown(curCount, cost, animal, device)
                dialog.cancel() }
            .setNegativeButton("Stop") {dialog, id ->
                dialog.cancel() }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated:")
        val pointCounter = PointCounter()
        deviceList = DeviceList()
        childFragmentManager.beginTransaction().replace(R.id.fragment_point_counter, pointCounter).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragment_device_list, deviceList).commit()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart:")

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:")
        deviceContentAdapter = DeviceContentAdapter(context!!)

        val gridView = deviceList.view!!.findViewById<GridView>(R.id.animal_overview_devices)
        gridView?.adapter = deviceContentAdapter
    }

    companion object {
        private const val TAG = "AnimalTabOverview"
    }
}