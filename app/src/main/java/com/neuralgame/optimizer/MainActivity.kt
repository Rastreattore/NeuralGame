package com.neuralgame.optimizer

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGamer = findViewById<Button>(R.id.btnNeuralGamer)
        val btnOverdrive = findViewById<Button>(R.id.btnNeuralOverdrive)

        btnGamer.setOnClickListener {
            cleanRam(false)
        }

        btnOverdrive.setOnLongClickListener {
            cleanRam(true)
            startService(Intent(this, NeuralHUDService::class.java))
            true
        }
    }

    private fun cleanRam(aggressive: Boolean) {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val protected = listOf("android", "system", packageName)

        am.runningAppProcesses?.forEach {
            if (protected.none { p -> it.processName.contains(p) }) {
                am.killBackgroundProcesses(it.processName)
            }
        }

        if (aggressive) {
            Runtime.getRuntime().gc()
        }
    }
}
