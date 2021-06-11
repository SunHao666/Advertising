package com.example.advertising

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker

class CustomInfoWindowAdapter(val context:Context):AMap.InfoWindowAdapter {
    override fun getInfoWindow(p0: Marker?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.view_info_window,null)
        return view
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }
}