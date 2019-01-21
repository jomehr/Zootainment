package com.example.jan.zootainment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.jan.zootainment.R
import com.example.jan.zootainment.data.DeviceData

class DeviceContentAdapter(private val context: Context) : BaseAdapter() {

    private var deviceContent: List<DeviceData> = ArrayList()
    private var animal: String = "unkown"

    private lateinit var mCallback: DeviceAdapterCallback
    private lateinit var device: String

    fun setDeviceContent(deviceContent: List<DeviceData>, animal: String, callback: DeviceAdapterCallback) {
        this.deviceContent = deviceContent
        this.animal = animal
        mCallback = callback
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            convertView = inflater.inflate(R.layout.content_item_device, parent, false)
        }

        val content = deviceContent[position]

        val title = convertView!!.findViewById<TextView>(R.id.device_title)
        val cost = convertView.findViewById<TextView>(R.id.feeding_cost)
        val limit = convertView.findViewById<TextView>(R.id.feeding_limit)
        val button = convertView.findViewById<Button>(R.id.device_button)

        title.text = content.title
        cost.text = content.cost.toString()
        limit.text = content.limit


        when (content.title) {
            "Feeding Cannon" -> {
                button.text = "pay and feed"
                device = "cannon_1"
            }
            "Water Sprinkler" -> {
                button.text = "pay and sprinkle"
                device = "sprinkler_1"
            }
        }

        button.setOnClickListener {
            mCallback.startCounter(content.cost!!, animal, device)
        }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return deviceContent[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return deviceContent.size
    }

    interface DeviceAdapterCallback {
        fun startCounter(cost: Int, animal: String, device: String)
    }
}