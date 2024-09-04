package com.example.practica03_22110092

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class ConsultarActivity : AppCompatActivity() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar)

        NotificationManagerCompat.from(this).apply {
            cancel(MenuActivity.notificationId)
        }

        setSupportActionBar(findViewById(R.id.barraConsultar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = getSharedPreferences("producto_prefs", MODE_PRIVATE)

        val nombreProducto = sharedPreferences.getString("nombreProducto", "")
        val precio = sharedPreferences.getString("precio", "")
        val categoria = sharedPreferences.getString("categoria", "")
        val marca = sharedPreferences.getString("marca", "")

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "Nombre del Producto: $nombreProducto\nPrecio: $precio\nCategoría: $categoria\nMarca: $marca"

        val btnEventosEspeciales: Button = findViewById(R.id.button)
        val btnCancela: Button = findViewById(R.id.btnCancela)

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, PendingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        btnEventosEspeciales.setOnClickListener {
            configurarAlarma()
        }

        btnCancela.setOnClickListener {
            cancelarAlarma()
        }
    }

    private fun configurarAlarma() {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 60 * 1000,
            60 * 1000,
            pendingIntent
        )
        Toast.makeText(applicationContext, "Alarma configurada", Toast.LENGTH_SHORT).show()
    }

    private fun cancelarAlarma() {
        alarmManager.cancel(pendingIntent)
        Toast.makeText(applicationContext, "Alarma cancelada", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}