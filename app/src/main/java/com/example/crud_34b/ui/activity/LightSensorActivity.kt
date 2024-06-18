package com.example.crud_34b.ui.activity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R
import com.example.crud_34b.databinding.ActivityAccelerometerBinding
import com.example.crud_34b.databinding.ActivityLightSensorBinding

class LightSensorActivity : AppCompatActivity(), SensorEventListener{

    lateinit var sensor: Sensor
    lateinit var sensorManager: SensorManager
    lateinit var lightSensorBinding: ActivityLightSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lightSensorBinding = ActivityLightSensorBinding.inflate(layoutInflater)
        setContentView(lightSensorBinding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (!checkSensor()) {
            Toast.makeText(applicationContext,
                "Accelerometer not supported",
                Toast.LENGTH_LONG).show()
            return
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun checkSensor() : Boolean {
        var sensor = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            sensor = false
            return sensor
        }
        return sensor
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var values = event!!.values[0]
        if (values < 1000) {
            lightSensorBinding.imgLightSensor.setImageResource(R.drawable.light_off)
        } else {
            lightSensorBinding.imgLightSensor.setImageResource(R.drawable.light_on)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}