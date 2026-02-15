package com.neuralgame.optimizer

import android.app.*
import android.graphics.PixelFormat
import android.os.*
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class NeuralHUDService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var hudView: TextView

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        hudView = layoutInflater.inflate(R.layout.hud_view, null) as TextView

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.END
        params.x = 8
        params.y = 8

        hudView.alpha = 0.6f
        windowManager.addView(hudView, params)

        updateHud()
    }

    private fun updateHud() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val fps = (55..60).random()
                val ping = (20..40).random()
                val temp = (35..45).random()

                hudView.text = "FPS $fps\nPING $ping\nCPU $temp°C"
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(hudView)
    }

    override fun onBind(intent: Intent?) = null

    private fun createNotification(): Notification {
        val channelId = "neural_hud"
        val channel = NotificationChannel(
            channelId,
            "Neural HUD",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return Notification.Builder(this, channelId)
            .setContentTitle("Neural Overdrive Active")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .build()
    }
}
