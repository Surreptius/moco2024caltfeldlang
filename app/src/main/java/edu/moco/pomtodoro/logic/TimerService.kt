package edu.moco.pomtodoro.logic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.data.TimerRepository
import edu.moco.pomtodoro.utils.formatTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    // Setting up supervisors for proper managing of coroutines
    private val serviceSupervisor = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceSupervisor)
    private var timerJob: Job? = null

    /**
     * Predefined interactions for @[onStartCommand]
     */
    enum class Action {
        START, STOP, RESTART
    }

    // Service Functions

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not really needed to my understanding, but feel free")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.START.toString() -> {
                launchTimer()
            }

            Action.STOP.toString() -> {
                stopTimer()
            }

            Action.RESTART.toString() -> {
                TimerRepository.restart()
                stopTimer()
                stopForeground(STOP_FOREGROUND_REMOVE)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Called on creation of the TimerService
     * Creates notification channels via calling
     * @[createNotificationChannels]
     */
    override fun onCreate() {
        createNotificationChannels()
        super.onCreate()
    }

    // Helper Functions

    /**
     * Creates notification channels
     * @[timerChannel]
     * @[alarmChannel]
     */
    private fun createNotificationChannels() {
        val timerChannel = object {
            private val ID = getString(R.string.timer_channel_id)
            private val NAME = "Pomtodoro Timer Channel"
            private val DESCRIPTION = "Notification Channel for remaining time"
            private val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(ID, NAME, IMPORTANCE).apply {
                description = DESCRIPTION
                setSound(null, null)
            }

        }

        val alarmChannel = object {
            private val ID = getString(R.string.alarm_channel_id)
            private val NAME = "Pomtodoro Alarm Channel"
            private val DESCRIPTION = "Notification Channel for 'time is up' alarms"
            private val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(ID, NAME, IMPORTANCE).apply {
                description = DESCRIPTION
                enableVibration(true)
                setSound(
                    Uri.parse("android.resource://${packageName}/${R.raw.sirene}"),
                    Notification.AUDIO_ATTRIBUTES_DEFAULT
                )
            }
        }

        val notificationManger: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManger.createNotificationChannel(timerChannel.channel)
        notificationManger.createNotificationChannel(alarmChannel.channel)
    }

    /**
     * Streamline the build process of notifications
     */
    private fun buildNotification(
        channelId: String,
        notificationTitle: String,
        notificationText: String
    ) = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.hourglass)
        .setContentTitle(notificationTitle)
        .setContentText(notificationText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOnlyAlertOnce(true)
        .build()

    /**
     * Launches coroutine that manages timer functions
     * and sets the corresponding flags in the @[TimerRepository]
     */
    private fun launchTimer() {
        TimerRepository.toggleTimer()

        timerJob = serviceScope.launch {
            timerController()
        }
    }

    /**
     * Handles the safe cancellation of the coroutine handling the timer
     * and sets corresponding flags in the @[TimerRepository]
     */
    private fun stopTimer() {
        if (timerJob?.isActive == true) {
            timerJob?.cancel()
            timerJob = null
            TimerRepository.toggleTimer()
        }
    }

    /**
     * Timer Logic
     * in which the @[TimerRepository.timerData] is being observed and handled
     * correspondingly. This happens by checking certain flags of the @[TimerRepository]
     * and adjusting these and the @[timerData.currentSeconds] accordingly
     */
    private suspend fun timerController() {
        TimerRepository.timerData.collect { timerData ->
            // Case: timer shouldn't be running
            if (!timerData.isRunning) {
                stopTimer()
                return@collect // breaks flow collection
            } else {
                // Case: timer should be running and processing the time
                if (timerData.currentSeconds > 0) {
                    TimerRepository.reduceTime()

                    startForeground(
                        1024,
                        buildNotification(
                            channelId = getString(R.string.timer_channel_id),
                            notificationTitle = "Pomodoro Timer",
                            notificationText = "Seconds left: ${formatTimer(timerData.currentSeconds)}"
                        )
                    )

                    delay(1000L)
                }
                // Case: timer is running but reached the end
                if (timerData.currentSeconds == 0L) {
                    TimerRepository.switchMode()

                    startForeground(
                        1025,
                        buildNotification(
                            channelId = getString(R.string.alarm_channel_id),
                            notificationTitle = "Alarm",
                            notificationText = "You are done with your interval"
                        )
                    )

                    // Short grace period, in order to let the alarm sound ring
                    if (timerData.isWorking) delay(5000L)
                    else {
                        stopTimer()
                        return@collect // breaks flow collection
                    }
                }
            }
        }
    }
}