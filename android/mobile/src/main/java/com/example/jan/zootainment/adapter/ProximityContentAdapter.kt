package com.example.jan.zootainment.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.jan.zootainment.AnimalActivity
import com.example.jan.zootainment.R
import com.example.jan.zootainment.data.ProximityContent
import com.example.jan.zootainment.util.ProximityContentUtils

class ProximityContentAdapter(private val context: Context) : BaseAdapter() {

    private var nearbyContent: List<ProximityContent> = ArrayList()

    fun setNearbyContent(nearbyContent: List<ProximityContent>) {
        this.nearbyContent = nearbyContent
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            convertView = inflater.inflate(R.layout.content_item_proximity, parent, false)
        }

        val content = nearbyContent[position]

        val title = convertView!!.findViewById<TextView>(R.id.proximity_title)
        val questions = convertView.findViewById<TextView>(R.id.proximity_var)
        val image = convertView.findViewById<ImageView>(R.id.proximity_animal)

        title.text = content.title
        questions.text = content.questions.toString()
        image.setImageResource(ProximityContentUtils.getDrawable(content.title))

        convertView.setBackgroundColor(ContextCompat.getColor(context, ProximityContentUtils.getColor(content.title)))

        convertView.setOnClickListener {
            val intent = Intent(context, AnimalActivity::class.java)
            intent.putExtra("animal", content.title)
            intent.putExtra("progress", content.questions)
            context.startActivity(intent) }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return nearbyContent[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return nearbyContent.size
    }
}