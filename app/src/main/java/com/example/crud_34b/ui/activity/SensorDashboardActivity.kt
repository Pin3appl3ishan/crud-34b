package com.example.crud_34b.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R
import com.example.crud_34b.databinding.ActivitySensorDashboardBinding

class SensorDashboardActivity : AppCompatActivity() {

    lateinit var sensorDashboardActivity: ActivitySensorDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorDashboardActivity = ActivitySensorDashboardBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sensor_dashboard)

        sensorDashboardActivity.btnList.setOnClickListener {
            var intent = Intent(this@SensorDashboardActivity,
                SensorListActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}