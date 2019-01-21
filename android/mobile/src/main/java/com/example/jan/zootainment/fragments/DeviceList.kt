package com.example.jan.zootainment.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.jan.zootainment.R
import com.example.jan.zootainment.adapter.DeviceContentAdapter

class DeviceList: Fragment() {

    private var deviceContentAdapter: DeviceContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, ":onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        deviceContentAdapter = DeviceContentAdapter(context!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, ":onCreateView")
        val rootView: View = inflater.inflate(R.layout.content_animal_overview_list, container, false)

        val gridView = rootView.findViewById<GridView>(R.id.animal_overview_devices)
        gridView.adapter = deviceContentAdapter

        return rootView
    }

    companion object {
        const val TAG = "DeviceList"
    }
}