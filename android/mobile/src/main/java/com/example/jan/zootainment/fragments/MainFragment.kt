package com.example.jan.zootainment.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jan.zootainment.R
import com.example.jan.zootainment.adapter.FragmentTabAdapter

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.content_main, container, false)

        val viewPager: ViewPager = rootView.findViewById(R.id.content_main_viewpager)
        setupViewPager(viewPager)

        val tabLayout: TabLayout = rootView.findViewById(R.id.content_main_tabs)
        tabLayout.setupWithViewPager(viewPager)

        return rootView
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = FragmentTabAdapter(childFragmentManager)
        adapter.addFragment(MainFragmentTabMap(), "Map")
        adapter.addFragment(MainFragmentTabList(), "List")
        viewPager.adapter = adapter
    }
}