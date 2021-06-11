package com.example.advertising

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMapScreenShotListener
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.offlinemap.OfflineMapActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var aMap: AMap
    private var type = 1
    private lateinit var uiSetting: UiSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)

        aMap = mapView.map
        aMap.isMyLocationEnabled = true

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
//                aMap.isMyLocationEnabled = false
            }

        }, 5000)

        aMap.addOnMyLocationChangeListener { location ->
            println(location.latitude)
            println(location.longitude)
        }

        aMap.showIndoorMap(true)

        //自定义infowindow
        aMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        initClick()
        initSetting()
        initCamera()
        screenshot()
        initMarker()
    }

    private fun initMarker() {
        val latLng = LatLng(39.906901, 116.397972)
        val marker =
            aMap.addMarker(
                MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker")
                    .draggable(false).infoWindowEnable(true).alpha(0f)
            )

        marker.showInfoWindow()
        //动画
//        val animation: Animation =
//            RotateAnimation(marker.rotateAngle, marker.rotateAngle + 180, 0f, 0f, 0f)
//        val duration = 5000L
//        animation.setDuration(duration)
//        animation.setInterpolator(LinearInterpolator())
//
//        marker.setAnimation(animation)
//        marker.startAnimation()
        //marker 点击事件监听
//            aMap.setOnMarkerClickListener {
//                Toast.makeText(applicationContext, "${it.snippet}", Toast.LENGTH_SHORT).show()
//                true
//            }

    }

    private fun screenshot() {
        /**
         * 对地图进行截屏
         */
        /**
         * 对地图进行截屏
         */
        aMap.getMapScreenShot(object : OnMapScreenShotListener {
            override fun onMapScreenShot(bitmap: Bitmap) {}
            override fun onMapScreenShot(bitmap: Bitmap, status: Int) {
                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                if (null == bitmap) {
                    return
                }
                try {
                    val fos = FileOutputStream(
                        Environment.getExternalStorageDirectory().toString() + "/test_"
                                + sdf.format(Date()) + ".png"
                    )
                    val b = bitmap.compress(CompressFormat.PNG, 100, fos)
                    try {
                        fos.flush()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val buffer = StringBuffer()
                    if (b) buffer.append("截屏成功 ") else {
                        buffer.append("截屏失败 ")
                    }
                    if (status != 0) buffer.append("地图渲染完成，截屏无网格") else {
                        buffer.append("地图未渲染完成，截屏有网格")
                    }
                    Toast.makeText(applicationContext, buffer.toString(), Toast.LENGTH_SHORT).show()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun initCamera() {
//        val southwestLatLng = LatLng(33.789925, 104.838326)
//        val northeastLatLng = LatLng(38.740688, 114.647472)
//        val latLngBounds = LatLngBounds(southwestLatLng, northeastLatLng)
//        aMap.setMapStatusLimits(latLngBounds)
    }

    private fun initSetting() {
        uiSetting = aMap.uiSettings
        uiSetting.apply {
            //缩放级别按钮
            isZoomControlsEnabled = false
            //指南针
            isCompassEnabled = true
            //定位当前图标
            isMyLocationButtonEnabled = true
            //定位尺寸
            isScaleControlsEnabled = true
        }
    }

    private fun initClick() {
        mapBgChange.setOnClickListener {
            when (type) {
                0 -> aMap.mapType = AMap.MAP_TYPE_NORMAL
                1 -> aMap.mapType = AMap.MAP_TYPE_SATELLITE
                2 -> aMap.mapType = AMap.MAP_TYPE_NIGHT
                3 -> aMap.mapType = AMap.MAP_TYPE_SATELLITE
                4 -> aMap.mapType = AMap.MAP_TYPE_NAVI
            }
            type++
            if (type > 4) {
                type = 0
            }
        }
        mapDownload.setOnClickListener {
            startActivity(
                Intent(
                    this.applicationContext,
                    OfflineMapActivity::class.java
                )
            )
        }

        mScreenShot.setOnClickListener {
            screenshot()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}