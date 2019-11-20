package com.ams.expandablepanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ams.panel.OnExpandListener
import kotlinx.android.synthetic.main.item_info.*
import kotlinx.android.synthetic.main.item_sensor.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorPanel.setOnExpandListener(object :OnExpandListener{
            override fun onExpandToggle(isExpand: Boolean, expandedView: View?, mainView: View?) {
            }
        })
        infoPanel.setOnExpandListener(object :OnExpandListener{
            override fun onExpandToggle(isExpand: Boolean, expandedView: View?, mainView: View?) {
            }
        })

    }
}
