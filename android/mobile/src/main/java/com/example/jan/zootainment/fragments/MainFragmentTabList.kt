package com.example.jan.zootainment.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.jan.zootainment.R
import com.example.jan.zootainment.adapter.ProximityContentAdapter
import com.example.jan.zootainment.data.ProximityContent
import com.example.jan.zootainment.util.ProximityContentManager

class MainFragmentTabList : Fragment() {

    var proximityContentAdapter: ProximityContentAdapter? = null
    private var proximityContentManager: ProximityContentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, ":onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        proximityContentAdapter = ProximityContentAdapter(activity!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, ":onCreateView")
        val rootView: View = inflater.inflate(R.layout.content_main_list, container, false)

        val gridView = rootView.findViewById <GridView> (R.id.gridView)
        gridView.adapter = proximityContentAdapter

        return rootView
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, ":onStart")
        if (proximityContentManager == null) startProximityContentManager()
    }

    override fun onDestroy() {
        super.onDestroy()
        proximityContentManager?.stop()
    }

    fun setNearbyContent(nearbyContent: List<ProximityContent>) {
        Log.d(TAG, "setting content: $nearbyContent")

        proximityContentAdapter!!.setNearbyContent(nearbyContent)
        proximityContentAdapter!!.notifyDataSetChanged()
    }

    private fun startProximityContentManager() {
        proximityContentManager = ProximityContentManager(activity!!)
        proximityContentManager?.start()
    }

    companion object {
        const val TAG = "MainFragmentList"
    }
}