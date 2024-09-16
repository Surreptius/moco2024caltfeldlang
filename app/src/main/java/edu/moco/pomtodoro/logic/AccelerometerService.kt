package edu.moco.pomtodoro.logic

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.data.TimerRepository
import kotlin.math.abs

class AccelerometerService : Service() {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var previousValues: FloatArray? = null

    private val accelerometerListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]


                if (previousValues == null) {
                    previousValues = floatArrayOf(x, y, z)
                    return
                }

                val diffX = abs(previousValues!![0] - x)
                val diffY = abs(previousValues!![1] - y)
                val diffZ = abs(previousValues!![2] - z)

                val threshold = 1.5

                if ((diffX > threshold || diffY > threshold || diffZ > threshold)) {
                    startForeground(1206, buildNotification())
                }

                previousValues!![0] = x
                previousValues!![1] = y
                previousValues!![2] = z
            }
        }
    }

    enum class Action {
        START, STOP
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        createNotificationChannel()

        super.onCreate()
    }

    override fun onDestroy() {
        stopConcentration()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.START.toString() -> {
                startConcentration()
            }

            Action.STOP.toString() -> {
                stopConcentration()
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val concentrationChannel = object {
            private val ID = getString(R.string.alarmConcentration_channel_id)
            private val NAME = "Pomtodoro Concentration Alarm"
            private val DESCRIPTION = "Notification Channel for alarming you when you move"
            private val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(ID, NAME, IMPORTANCE).apply {
                description = DESCRIPTION
            }
        }

        val notificationManger: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManger.createNotificationChannel(concentrationChannel.channel)
    }

    private fun buildNotification() =
        NotificationCompat.Builder(this, getString(R.string.alarmConcentration_channel_id))
            .setSmallIcon(R.drawable.bad_mood)
            .setContentTitle("Bewegung Erkannt")
            .setContentText("Konzentrier dich!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

    private fun startConcentration() {
        TimerRepository.switchConcentration()
        accelerometer?.let {
            sensorManager?.registerListener(
                accelerometerListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }


    private fun stopConcentration() {
        TimerRepository.switchConcentration()
        sensorManager?.unregisterListener(accelerometerListener)
    }
}