package com.example.practica03_22110092

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var correo: EditText
    private lateinit var contrasena: EditText

    private val usuarios = listOf(
        Usuario("karen@gmail.com", "12345"),
        Usuario("fabian@gmail.com", "12345"),
        Usuario("arnol@gmail.com", "12345"),
        Usuario("alejandra@gmail.com", "12345"),
        Usuario("admin@gmail.com", "12345")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        correo = findViewById(R.id.edtCorreo)
        contrasena = findViewById(R.id.edtContrasena)

        val btnIngresar: View = findViewById(R.id.btnIngresar)
        btnIngresar.setOnClickListener { aceptar() }

        val btnSalir: Button = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener { salir() }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM", "FCM Registration Token: $token")
        }
    }

    private fun aceptar() {
        val inputCorreo = correo.text.toString()
        val inputContrasena = contrasena.text.toString()

        if (inputCorreo.isBlank() || inputContrasena.isBlank()) {
            Toast.makeText(this, "Falta completar los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = usuarios.find { it.correo == inputCorreo && it.contrasena == inputContrasena }
        if (usuario != null) {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Datos Erróneos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salir() {
        finishAffinity()
    }
}