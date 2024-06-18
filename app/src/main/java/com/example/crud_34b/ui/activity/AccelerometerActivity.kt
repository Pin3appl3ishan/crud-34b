package com.example.crud_34b.ui.activity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R
import com.example.crud_34b.databinding.ActivityAccelerometerBinding
import kotlin.math.sqrt

class AccelerometerActivity : AppCompatActivity(), SensorEventListener {
    lateinit var sensor: Sensor
    lateinit var sensorManager: SensorManager
    lateinit var accelerometerBinding: ActivityAccelerometerBinding
    private var lastShakeTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        accelerometerBinding = ActivityAccelerometerBinding.inflate(layoutInflater)
        setContentView(accelerometerBinding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (!checkSensor()) {
            Toast.makeText(applicationContext,
                "Accelerometer not supported",
                Toast.LENGTH_LONG).show()
            return
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun detectShake(x: Float, y: Float, z: Float) {
        val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val shakeThreshold = 12.0f
        val currentTime = System.currentTimeMillis()
        if (accelerationMagnitude > shakeThreshold) {
            if (currentTime - lastShakeTime > 500) { // 500 ms delay to prevent multiple triggers
                lastShakeTime = currentTime
                showAlert()
            }
        }
    }
    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle("Shake Detected")
            .setMessage("You have shaken the device!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun checkSensor() : Boolean {
        var sensor = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            sensor = false
            return sensor
        }
        return sensor
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var values = event!!.values
        var xAxis = values[0]
        var yAxis =values[1]
        var zAxis = values[2]

        accelerometerBinding.textAxis.text = "xAxis: $xAxis yAxis: $yAxis zAxis: $zAxis"
        detectShake(xAxis,yAxis,zAxis)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}