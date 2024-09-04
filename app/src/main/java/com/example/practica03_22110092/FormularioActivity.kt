package com.example.practica03_22110092

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.practica03_22110092.MenuActivity.Companion.notificationId

class FormularioActivity : AppCompatActivity() {

    private lateinit var edtNombreProducto: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var spCategorias: Spinner
    private lateinit var spMarcas: Spinner
    private lateinit var btnAgregarProducto: Button

    private val productos = mutableListOf<Producto>()
    private val categorias = listOf("Accesorios", "Consolas", "Juegos", "Merchandising")
    private val marcas = listOf("Sony", "Microsoft", "Nintendo", "Ubisoft", "Activision")

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        NotificationManagerCompat.from(this).apply {
            cancel(notificationId)
        }

        setSupportActionBar(findViewById(R.id.barraFormulario))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = getSharedPreferences("producto_prefs", MODE_PRIVATE)

        edtNombreProducto = findViewById(R.id.edtNombreProducto)
        edtPrecio = findViewById(R.id.edtPrecio)
        spCategorias = findViewById(R.id.spCategorias)
        spMarcas = findViewById(R.id.spMarcas)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)

        setupCategoriasSpinner()
        setupMarcasSpinner()

        btnAgregarProducto.setOnClickListener { onAgregarProductoClick() }
    }

    private fun setupCategoriasSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategorias.adapter = adapter
    }

    private fun setupMarcasSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMarcas.adapter = adapter
    }

    private fun onAgregarProductoClick() {
        val nombreProducto = edtNombreProducto.text.toString()
        val precioString = edtPrecio.text.toString()
        val categoria = spCategorias.selectedItem.toString()
        val marca = spMarcas.selectedItem.toString()

        if (nombreProducto.isEmpty() || precioString.isEmpty() || categoria.isEmpty() || marca.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
        } else {
            val precio = precioString.toDoubleOrNull()
            if (precio == null) {
                Toast.makeText(this, "Por favor, ingresa un precio válido.", Toast.LENGTH_SHORT).show()
            } else {
                val producto = Producto(
                    nombreProducto = nombreProducto,
                    precio = precio,
                    categoria = categoria,
                    marca = marca
                )

                productos.add(producto)

                Toast.makeText(this, "Producto agregado con éxito.", Toast.LENGTH_SHORT).show()

                edtNombreProducto.text.clear()
                edtPrecio.text.clear()
                spCategorias.setSelection(0)
                spMarcas.setSelection(0)

                with(sharedPreferences.edit()) {
                    putString("nombreProducto", nombreProducto)
                    putString("precio", precioString)
                    putString("categoria", categoria)
                    putString("marca", marca)
                    apply()
                }

                val intent = Intent(this, ConsultarActivity::class.java)
                startActivity(intent)
            }
        }
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