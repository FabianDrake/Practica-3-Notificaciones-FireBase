package com.example.practica03_22110092

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MenuActivity : AppCompatActivity() {

    private lateinit var rgOption: RadioGroup
    private lateinit var date: RadioButton
    private lateinit var service: RadioButton

    private val CHANNEL_ID = "Canal_notificacion"
    private lateinit var pendingIntentSI: PendingIntent
    private lateinit var pendingIntentNO: PendingIntent

    companion object {
        const val notificationId = 200
        const val ACTION_CANCEL_NOTIFICATION = "com.example.practica01mendozareyesangelemanuel.ACTION_CANCEL_NOTIFICATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        rgOption = findViewById(R.id.rgOpcion)
        date = findViewById(R.id.rbCita)
        service = findViewById(R.id.rbServicios)

        setSupportActionBar(findViewById(R.id.barraMenu))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        date.setOnClickListener {
            if (date.isChecked) {
                service.isChecked = false
            }
        }

        service.setOnClickListener {
            if (service.isChecked) {
                date.isChecked = false
            }
        }

        val btnAplicar: View = findViewById(R.id.btnAplicar)
        btnAplicar.setOnClickListener { aplicar() }

        registerReceiver(cancelNotificationReceiver, IntentFilter(ACTION_CANCEL_NOTIFICATION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(cancelNotificationReceiver)
    }

    fun aplicar() {
        if (date.isChecked) {
            createNotificationChannel()
            configureActions()
            buildNotification()
            Toast.makeText(this, "Agregar producto", Toast.LENGTH_SHORT).show()
        } else if (service.isChecked) {
            notificacionToque()
            Toast.makeText(this, "Ver productos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun notificacionToque() {
        val intent = Intent(this, InformativaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_active_24)
            .setContentTitle("Steam")
            .setContentText("Ver productos ofrecidos por GameHub Online Store")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun buildNotification() {
        val cancelIntent = Intent(ACTION_CANCEL_NOTIFICATION)
        val cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_active_24)
            .setContentTitle("Steam")
            .setContentText("¿Te gustaría agregar un producto?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.notifications_active_24, "Aceptar", pendingIntentSI)
            .addAction(R.drawable.notifications_active_24, "Cancelar", cancelPendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun configureActions() {
        val accionSi = Intent(this, FormularioActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntentSI = PendingIntent.getActivity(this, 0, accionSi,
            PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotificationChannel() {
        val name = "Canal_IMC"
        val descriptionText = "Steam"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private val cancelNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}